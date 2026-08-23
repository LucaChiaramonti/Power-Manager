package com.powerManager.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VectorStoreService {

    //in-memory storage for power embeddings
    private final Map<Long, float[]> powerEmbeddings = new ConcurrentHashMap<>();
    private final Map<Long, String> powerTexts = new ConcurrentHashMap<>();

    public void storeEmbedding(Long powerId, float[] embedding, String text) {
        if (powerId != null && embedding != null) {
            powerEmbeddings.put(powerId, embedding);
            powerTexts.put(powerId, text);
        }
    }

    
    //finds similar powers based  on embedding similarity
    public List<SimilarityResult> findSimilar(float[] queryEmbedding, int topK) {
        List<SimilarityResult> results = new ArrayList<>();

        for (Map.Entry<Long, float[]> entry : powerEmbeddings.entrySet()) {
            Long powerId = entry.getKey();
            float[] embedding = entry.getValue();

            try {
                double similarity = calculateCosineSimilarity(queryEmbedding, embedding);
                results.add(new SimilarityResult(powerId, similarity));

            } catch (Exception e) {
                continue;
            }
        }
        
        results.sort((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()));

        return results.subList(0, Math.min(topK, results.size()));
    }

    //finds similarity between two vectors
    private double calculateCosineSimilarity(float[] vector1, float[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += vector1[i] * vector1[i];
            norm2 += vector2[i] * vector2[i];
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }


    public String getTextForPower(Long powerId) {
        return powerTexts.get(powerId);
    }


    public void clear() {
        powerEmbeddings.clear();
        powerTexts.clear();
    }


    public int size() {
        return powerEmbeddings.size();
    }

    public static class SimilarityResult {
        private final Long powerId;
        private final double similarity;

        public SimilarityResult(Long powerId, double similarity) {
            this.powerId = powerId;
            this.similarity = similarity;
        }

        public Long getPowerId() {
            return powerId;
        }

        public double getSimilarity() {
            return similarity;
        }

        @Override
        public String toString() {
            return "SimilarityResult{powerId=" + powerId + ", similarity=" + similarity + '}';
        }
    }
}