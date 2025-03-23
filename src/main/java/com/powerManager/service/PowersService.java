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
import com.test.powerManager.dto.*;
import com.test.powerManager.repository.*;
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
    @Autowired
    private PowerRepository powerRepository;
    @Autowired
    private AugmentRepository augmentRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private ClassLevelRepository classLevelRepository;
    @Autowired
    private LevelService levelService;
    @Autowired
    LevelRepository levelRepository;
    @Autowired
    private PowerMapper powerMapper;
    @Autowired
    private AugmentMapper augmentMapper;

    private Log _log = LogFactory.getLog(PowersService.class);
    private final RestTemplate restTemplate;
    private static final String _url = "https://www.psionics.info/powers/";
    public PowersService(RestTemplate restTemplate) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 seconds
        factory.setReadTimeout(5000); // 5 seconds
        this.restTemplate = new RestTemplate(factory);    }
    public PowersService() {
        this.restTemplate = new RestTemplate();
    }

    public Set<String> callExternalAPI(Integer powerLevel) {

        return readMainPage(_url,powerLevel);

    }
    public Set<String> readMainPage(String url, Integer powerLevel) {

        String response = restTemplate.getForObject(url, String.class);
        String regexPowerName = "(?<=href=\\\"\\/powers\\/)(.*)(?=\\\" title=)";

        Pattern pattern = Pattern.compile(regexPowerName);
        Matcher matcher = pattern.matcher(response);

        Set<String> powerList = new HashSet<>();
        while (matcher.find()) {
            String result = matcher.group(1);

            if (!result.equals("/powers/")) {

                String powerUrl = url + result.trim();
                //_log.info("powerURL : " + powerUrl);

                callAPI(powerUrl);


                try {
                        PowerBin power = readPowerPage(powerUrl, powerLevel);
                        if (power != null) {
                            powerList.add(power.getPowerName());
                        }

                }
                catch (Exception e) {
                    _log.error("Error reading power: " + powerUrl);
                }


            }
        }

        return powerList;
    }
    private PowerBin readPowerPage(String powerUrl, Integer powerLevel) {
        String powerCostFound = "";
        AugmentBin augmentBin = new AugmentBin();
        PowerBin power = new PowerBin();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        String powerResponse = callAPI(powerUrl);


        List<String> powerCostList;
        powerCostFound = PowerUtil.extractCost(powerResponse);
        String powerName = PowerUtil.extractName(powerResponse);
        _log.info("Power name = " + powerName);
        power.setPowerName(powerName);
        if(!powerRepository.findPowerFromName(powerName).isEmpty()) {
            _log.info("Power already saved: " + powerName);
            return null;
        }
        if(!powerCostFound.equals("")) {
            if(powerCostFound.contains(", XP")) {
                powerCostFound = powerCostFound.replace(", XP", "");
            }
            powerCostList = PowerUtil.buildPowerCostList(powerCostFound);

            power.setPowerCost(Integer.parseInt(powerCostList.get(0)));
            _log.info("Saving class...");
            for(String powerCost : powerCostList) {

                try {

                    //TODO logica di salvataggio classe/livello
                  Map<String, Long>  classLevelList = PowerUtil.extractClassList(powerResponse);
                    for (Map.Entry<String, Long> entry : classLevelList.entrySet()) {

                        _log.info("Class = " + entry.getKey() + ", Level = " + entry.getValue());

                        String className = entry.getKey();
                        Long levelValue = entry.getValue();

                        Level levelDto = levelService.findById(levelValue).
                                orElseThrow(() -> new RuntimeException("Level not valid"));

                        Class classDto = classRepository.findClassByClassName(className);

                        if(classDto == null) {
                            classDto = new Class();
                            classDto.setClassName(className);
                        }

                        classDto.setClassName(className);

                        classRepository.save(classDto);
                        levelRepository.save(levelDto);
                        ClassLevel classLevel = new ClassLevel(classDto, levelDto);

                        classLevelRepository.save(classLevel);
                    }

                    if(powerCostFound.equals("1")) {
                        power.setPowerLevel(1);
                    } else {
                        power.setPowerLevel(PowerUtil.getPowerLevel(powerCost));
                    }
                } catch (NumberFormatException e) {
                    _log.error("ERROR SAVING CLASS FOR POWER: " + powerName);
                    return null;
                } catch (RuntimeException e) {
                    _log.error("ERROR LEVEL NOT VALID FOR POWER: " + powerName);
                    return null;
                }


            }
            _log.info("Class saved");
            power.setPowerName(PowerUtil.extractName(powerResponse));

            String noSpace = powerResponse.replaceAll("\\r|\\n", "").replaceAll("\t\t\t", "");
            _log.info("Setting power Description...");
            power.setPowerDescription(PowerUtil.extractDescription(noSpace));
            _log.info("Saving power: " + power.getPowerName());
            Power savedPower = savePower(power);
            _log.info("Power saved: " + savedPower.getPowerName());
            if(powerResponse.contains("augment")) {

                String augmentBlock = HtmlUtil.extractText(noSpace, "<div id=\"augments\">(.*?)<\\/div>");
                if(augmentBlock.contains("li")) {
                    augmentBlock = augmentBlock.replaceAll("<h2>(.*?)<\\/p><ol>", "")
                            .replaceAll("</li>", "")
                            .replaceAll("</ol>", "")
                            .replaceFirst("<li>", "").replaceAll("\t", "");
                    _log.info("Power: " + power.getPowerName() + ", augmentBlock: " + augmentBlock);
                    List<String> augmentList = Arrays.asList(augmentBlock
                            .split("<li>"));
                    for(String augment : augmentList) {
                        augmentBin.setAugmentText(augment);
                        saveAugmentPower(savedPower, augmentBin);
                    }
                }
                else {
                    augmentBin.setAugmentText(HtmlUtil.extractText(augmentBlock,"<p>(.*?)<\\/p>"));
                    saveAugmentPower(savedPower, augmentBin);
                }

                return null;
            }

        }
        return power;
    }

    private String callAPI(String powerUrl) {
        int maxAttemps = 5;
        int attempt = 0;
        boolean success = false;
        String powerResponse = null;
        long timeout = 8;

        while (attempt < maxAttemps && !success) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(() -> restTemplate.getForObject(powerUrl, String.class));
            try {
                powerResponse = future.get(timeout, TimeUnit.SECONDS);
                success = true;
            } catch (Exception e) {
                attempt++;
                _log.error("Error reading power: " + powerUrl + ", attempt " + attempt);
                if (attempt >= maxAttemps) {
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
        List<PowerBin> powerBinList = new ArrayList<PowerBin>();
        List<Power> powerList = powerRepository.findPowerFromDescription(description);

        for (Power power : powerList) {
            powerBinList.add(powerMapper.toBin(power, power.getAugments()));
        }
        return powerBinList;
    }
    public List<PowerBin> getPowersFromName(String name) {
        List<PowerBin> powerBinList = new ArrayList<PowerBin>();
        List<Power> powerList = powerRepository.findPowerFromName(name);
        for (Power power : powerList) {
            powerBinList.add(powerMapper.toBin(power, power.getAugments()));
        }
        return powerBinList;
    }
}


