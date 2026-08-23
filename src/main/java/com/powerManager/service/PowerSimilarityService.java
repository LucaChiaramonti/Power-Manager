package com.powerManager.service;

import com.powerManager.dto.Power;
import com.powerManager.dto.SituationPowerResponse;
import com.powerManager.repository.PowerRepository;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

//Service for calculating similarity between powers based on semantic embeddings
//  and performs semantic similarity search using vector operations./
@Service
public class PowerSimilarityService {

    private static final Log _log = LogFactory.getLog(PowerSimilarityService.class);

    private final PowerRepository powerRepository;
    private final OllamaEmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;


    private boolean initialized = false;

    

    public PowerSimilarityService(PowerRepository powerRepository,
                                  OllamaEmbeddingService embeddingService,
                                  VectorStoreService vectorStoreService) {
        this.powerRepository = powerRepository;
        this.embeddingService = embeddingService;    //embeddingService is the service for create embeddings
        this.vectorStoreService = vectorStoreService;    //vectorStoreService is the service for storing and searching embeddings
    }


    //during application startup initializes the service by merging the power text so that become easier to load it into the vector store
    @PostConstruct
    private void initialize() {
        try {
            //Load all powers into the vector store for the semantic search
            Iterable<Power> powers = powerRepository.findAll();

            int c = 0;
            for (Power power : powers) {
                 try {
                    //merging Name and description of each power for simplify embedding
                    String powerText = createPowerTextRepresentation(power);
                    //generate embedding for the power
                    float[] embedding = embeddingService.generateEmbedding(powerText);
                    //store the embeddings in vector
                    vectorStoreService.storeEmbedding(power.getIdPower(), embedding, powerText);

                    c++;
                } catch (Exception e) {
                    _log.warn("failed to create embedding for power with Id: " + power.getIdPower(), e);
                }
            }
        
            this.initialized = true;
            _log.info("PowerSimilarityService initialized successfully with " + c + " powers out of " + powers.spliterator().getExactSizeIfKnown());
        } catch (Exception e) {
            _log.warn("Cannot initialize PowerSimilarityService", e);
            this.initialized = false;
        }
    }


    private String createPowerTextRepresentation(Power power) {
        StringBuilder sb = new StringBuilder();
        if (power.getPowerName() != null) {
            sb.append("Power Name: ").append(power.getPowerName()).append(". ");
        }
        if (power.getPowerDescription() != null) {
            sb.append("Description: ").append(power.getPowerDescription());
        }
        return sb.toString();
    }


    //Finds powers similar to the given power based on semantic similarity.
    public List<Long> findSimilarPowers(Long powerId, int maxResults) {
        if (!initialized) {
            _log.warn("Service not fully initialized, returning empty similarity results");
            return new ArrayList<>();
        }

        try {
            Power referencePower = powerRepository.findById(powerId).orElse(null);
            if (referencePower == null) {
                return new ArrayList<>();
            }
            String searchText = createPowerTextRepresentation(referencePower);

            float[] queryEmbedding = embeddingService.generateEmbedding(searchText);

            List<VectorStoreService.SimilarityResult> results = vectorStoreService.findSimilar(queryEmbedding, maxResults + 1);

            
            List<Long> similarPowerIds = results.stream()
                .filter(result -> !result.getPowerId().equals(powerId)) //extract the results, excluding the same power
                .limit(maxResults)
                .map(VectorStoreService.SimilarityResult::getPowerId)
                .collect(Collectors.toList());

            return similarPowerIds;
        } catch (Exception e) {
            _log.error("Error computing similarities for power ID: " + powerId, e);
            return new ArrayList<>();
        }
    }


    public List<SituationPowerResponse> findPowersForSituation(String situation, int maxResults) {
        if (!initialized) {
            _log.warn("Service not fully initialized, returning empty situation results");
            return new ArrayList<>();
        }

        try {
            // Generate embedding for the situation
            float[] queryEmbedding = embeddingService.generateEmbedding(situation);

            //search it in the vector store for similar powers
            //TODO improve the search by adding a threshold for similarity and filtering out low-similarity results
            List<VectorStoreService.SimilarityResult> results = vectorStoreService.findSimilar(queryEmbedding, maxResults);

            List<SituationPowerResponse> responseList = results.stream()
                .map(result -> {
                    Long powerId = result.getPowerId();
                    double similarity = result.getSimilarity();

                    Power power = powerRepository.findById(powerId).orElse(null);

                    String powerName = power != null ? power.getPowerName() : "Unknown Power";
                    String description = power != null ? power.getPowerDescription() : "Description not available";

                    return new SituationPowerResponse(
                        powerId,
                        powerName,
                        description,
                        similarity
                    );
                })
                .collect(Collectors.toList());

            return responseList;
        } catch (Exception e) {
            _log.error("Error finding powers for situation: " + situation, e);
            return new ArrayList<>();
        }
    }

    public void reinitialize() {
        try {
            vectorStoreService.clear();
            initialize();
        } catch (Exception e) {
            _log.error("Error reinitializing PowerSimilarityService", e);
        }
    }

  
    @PreDestroy
    private void cleanup() {
        try {
            if (embeddingService instanceof AutoCloseable) {
                ((AutoCloseable) embeddingService).close();
            }
        } catch (Exception e) {
            _log.warn("Error closing embedding service", e);
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public int getLoadedPowerCount() {
        return vectorStoreService.size();
    }
}