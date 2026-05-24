package com.powerManager.service;

import com.powerManager.bin.AugmentBin;
import com.powerManager.bin.PowerBin;
import com.powerManager.dto.*;
import com.powerManager.dto.Class;
import com.powerManager.mapper.AugmentMapper;
import com.powerManager.mapper.PowerMapper;
import com.powerManager.repository.*;
import com.powerManager.util.HtmlUtil;
import com.powerManager.util.PowerUtil;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PowersService {
    private static final Log _log = LogFactory.getLog(PowersService.class);
    private static final String BASE_URL = "https://www.psionics.info/powers/";
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 5000;
    private static final int MAX_ATTEMPTS = 5;
    private static final long TIMEOUT = 8;

    @Autowired
    private PowerRepository powerRepository;
    @Autowired
    private AugmentRepository augmentRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private PowerClassRepository powerClassRepository;
    @Autowired
    private ClassLevelRepository classLevelRepository;
    @Autowired
    private LevelService levelService;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private PowerMapper powerMapper;
    @Autowired
    private AugmentMapper augmentMapper;

    private final RestTemplate restTemplate;

    public PowersService(RestTemplate restTemplate) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT);
        factory.setReadTimeout(READ_TIMEOUT);
        this.restTemplate = new RestTemplate(factory);
    }

    public PowersService() {
        this.restTemplate = new RestTemplate();
    }

    public Set<String> readMainPage() {
        String response = restTemplate.getForObject(BASE_URL, String.class);

        Pattern pattern = Pattern.compile(HtmlUtil.powerNameRegex);
        Matcher matcher = pattern.matcher(response);

        Set<String> powerList = new HashSet<>();
        while (matcher.find()) {
            String result = matcher.group(1);
            if (!result.equals("/powers/")) {
                String powerUrl = BASE_URL + result.trim();
                try {
                    PowerBin power = readPowerPage(powerUrl);
                    if (power != null) {
                        powerList.add(power.getPowerName());
                    }
                } catch (Exception e) {
                    _log.error("Error reading power: " + powerUrl, e);
                }
            }
        }
        return powerList;
    }

    private PowerBin readPowerPage(String powerUrl) {
        String powerResponse = callAPI(powerUrl);
        if(powerResponse == null) {
            return null;
        }

        PowerBin power = new PowerBin();
        power.setPowerName(PowerUtil.extractName(powerResponse));
        if(!powerRepository.findPowerFromName(power.getPowerName()).isEmpty()) {
            _log.info("Power already saved: " + power.getPowerName());
            return null;
        }
        String powerCostFound = PowerUtil.extractCost(powerResponse);

        power.setPowerDescription(PowerUtil.extractDescription(powerResponse.replaceAll("\\r|\\n", "").replaceAll("\t\t\t", "")));
        Power savedPower = savePower(power);
        _log.info("Power saved: " + savedPower.getPowerName());

        if(!powerCostFound.isEmpty()) {
            processPowerCost(power, powerCostFound, powerResponse, savedPower);
        }

        if(powerResponse.contains("augment")) {
            processAugments(savedPower, powerResponse);
        }

        return power;
    }

    private void processPowerCost(PowerBin power, String powerCostFound, String powerResponse, Power savedPower) {
        List<String> powerCostList = PowerUtil.buildPowerCostList(powerCostFound.replace(", XP", ""));
        _log.info("Saving class...");

        for (String powerCost : powerCostList) {
            try {
                Map<String, Long> classLevelList = PowerUtil.extractClassList(powerResponse);
                for (Map.Entry<String, Long> entry : classLevelList.entrySet()) {
                    saveClassLevel(entry.getKey(), entry.getValue(), savedPower);
                }
            } catch (RuntimeException e) {
                _log.error("Error saving class for power: " + power.getPowerName());
            }
        }
        _log.info("Class saved");
    }

    private void saveClassLevel(String className, Long levelValue, Power power) {
        Level levelDto = levelService.findById(levelValue).orElseThrow(() -> new RuntimeException("Level not valid"));
        Class classDto = classRepository.findClassByClassName(className);
        if(classDto == null) {
            classDto = new Class();
            classDto.setClassName(className);
        }

        ClassLevel classLevelDto = new ClassLevel(classDto,
            levelDto);
        levelRepository.save(levelDto);
        classRepository.save(classDto);
        classLevelRepository.save(classLevelDto);
        PowerClass powerClass = new PowerClass(classLevelDto, power);
        powerClassRepository.save(powerClass);
    }

    private void processAugments(Power savedPower, String powerResponse) {
        AugmentBin augmentBin = new AugmentBin();
        String noSpace = powerResponse.replaceAll("\\r|\\n", "").replaceAll("\t\t\t", "");
        String augmentBlock = HtmlUtil.extractText(noSpace
                , "<div id=\"augments\">(.*?)<\\/div>");
        if(augmentBlock.contains("li")) {
            List<String> augmentList = Arrays.asList(augmentBlock.replaceAll("<h2>(.*?)<\\/p><ol>", "")
                    .replaceAll("</li>", "").replaceAll("</ol>", "").replaceFirst("<li>", "").replaceAll("\t", "").split("<li>"));
            for (String augment : augmentList) {
                augmentBin.setAugmentText(augment);
                saveAugmentPower(savedPower, augmentBin);
            }
        } else {
                augmentBin.setAugmentText(HtmlUtil.extractText(augmentBlock, "<p>(.*?)<\\/p>"));
            saveAugmentPower(savedPower, augmentBin);
        }
    }

    private String callAPI(String powerUrl) {
        int attempt = 0;
        boolean success = false;
        String powerResponse = null;

        while (attempt < MAX_ATTEMPTS && !success) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(() -> restTemplate.getForObject(powerUrl, String.class));
            try {
                powerResponse = future.get(TIMEOUT, TimeUnit.SECONDS);
                success = true;
            } catch (Exception e) {
                attempt++;
                _log.error("Error reading power: " + powerUrl + ", attempt " + attempt);
                if(attempt >= MAX_ATTEMPTS) {
                    return null;
                }
            } finally {
                executor.shutdown();
            }
        }

        return powerResponse;
    }

    @Transactional
    public Power savePower(PowerBin power) {
        Power powerDto = powerMapper.toDto(power);
        return powerRepository.save(powerDto);
    }

    @Transactional
    public AugmentBin saveAugmentPower(Power power, AugmentBin augmentBin) {
        Augment augmentDto = augmentMapper.toDto(augmentBin);
        augmentDto.setPower(power);
        return augmentMapper.toBin(augmentRepository.save(augmentDto));
    }

    public List<PowerBin> getPowersFromDescription(String description) {
        List<Power> powerList = powerRepository.findPowerFromDescription(description);
        return buildPowerBinList(powerList);
    }
    @Transactional
    public void deleteAll() {
        augmentRepository.deleteAll();
        powerClassRepository.deleteAll();
        classLevelRepository.deleteAll();
        classRepository.deleteAll();
        powerRepository.deleteAll();

    }
    public List<PowerBin> getPowersFromName(String name) {
        List<Power> powerList = powerRepository.findPowerFromName(name);
        _log.info("Modifica effettuata");
        return buildPowerBinList(powerList);
    }


    private List<PowerBin> buildPowerBinList(List<Power> powerList) {
        List<ClassLevel> classLevel;
        List<PowerBin> powerBinList = new ArrayList<>();
        for (Power power : powerList) {
            classLevel = classLevelRepository.getClassLevelByPowerName(power.getPowerName());
            powerBinList.add(powerMapper.toBin(power, classLevel));
        }
        return powerBinList;
    }
}