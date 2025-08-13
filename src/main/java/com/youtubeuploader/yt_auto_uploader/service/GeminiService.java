package com.youtubeuploader.yt_auto_uploader.service;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.google.protobuf.ByteString;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    // This method now accepts a file path instead of a text prompt
    public String generateVideoMetadata(String videoFilePath) throws IOException {
        String projectId = "your-gcp-project-id"; // <-- CHANGE THIS
        String location = "us-central1";
        // Use a model that supports video input, like 1.5 Pro or 1.5 Flash
        String modelName = "gemini-1.5-flash-001";

        try (VertexAI vertexAI = new VertexAI.Builder().setProjectId(projectId).setLocation(location).setApiKey(geminiApiKey).build()) {
            GenerativeModel model = new GenerativeModel(modelName, vertexAI);

            // 1. Read the video file into memory
            byte[] videoBytes = Files.readAllBytes(Path.of(videoFilePath));

            // 2. Create the prompt that tells the model what to do
            String textPrompt = "Analyze the content of this video. Generate a catchy YouTube title and a short, " +
                    "engaging description for it. Format the output exactly like this, with no extra text:\n" +
                    "Title: [Your Title Here]\n" +
                    "Description: [Your Description Here]";

            // 3. Create the video data part
            Part videoPart = Part.newBuilder()
                    .setMimeType("video/mp4") // Assuming the video is an MP4
                    .setData(ByteString.copyFrom(videoBytes))
                    .build();

            // 4. Send both the text prompt and the video to Gemini
            GenerateContentResponse response = model.generateContent(
                    Collections.singletonList(Content.newBuilder().addParts(Part.fromText(textPrompt)).addParts(videoPart).build())
            );

            return ResponseHandler.getText(response);
        }
    }
}