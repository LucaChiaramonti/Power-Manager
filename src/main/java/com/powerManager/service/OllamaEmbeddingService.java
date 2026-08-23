package com.powerManager.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class OllamaEmbeddingService implements AutoCloseable {

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String ollamaUrl = "http://localhost:11434/api/embeddings";
    private final String modelName = "nomic-embed-text";

    public OllamaEmbeddingService() {
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
    }

    //generate embeddings for the given text using Ollama's nomic-embed-text model.
    public float[] generateEmbedding(String text) throws IOException {
        if (text == null || text.trim().isEmpty()) {
            return new float[0];
        }

        String escapedText;
        try {
            escapedText = objectMapper.writeValueAsString(text);
        } catch (Exception e) {
            throw new IOException("Failed to escape text for JSON: " + text, e);
        }

        String jsonPayload = String.format("{\"model\": \"%s\", \"prompt\": %s}", modelName,escapedText);
        HttpPost post = new HttpPost(ollamaUrl);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(jsonPayload, "UTF-8"));

    
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            String jsonResponse = EntityUtils.toString(response.getEntity());

            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode embeddingNode = rootNode.get("embedding");

            if (embeddingNode != null && embeddingNode.isArray()) {
                float[] embeddings = new float[embeddingNode.size()];
                for (int i = 0; i < embeddingNode.size(); i++) {
                    embeddings[i] = (float) embeddingNode.get(i).asDouble();
                }
                return embeddings;
            } else {
                throw new IOException("Invalid response from Ollama: " + jsonResponse);
            }
        }
    }

    //calculates cosine similarity between two embedding vectors
    public double calculateCosineSimilarity(float[] vector1, float[] vector2) {
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

    //Normalizes a vector to unit length
    public float[] normalize(float[] vector) {
        double norm = 0.0;
        for (float v : vector) {
            norm += v * v;
        }
        norm = Math.sqrt(norm);

        if (norm == 0) {
            return vector;
        }

        float[] normalized = new float[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalized[i] = (float) (vector[i] / norm);
        }
        return normalized;
    }

    @Override
    public void close() throws IOException {
        if (httpClient != null) {
            httpClient.close();
        }
    }
}