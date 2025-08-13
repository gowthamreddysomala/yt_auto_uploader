package com.youtubeuploader.yt_auto_uploader.service; // Make sure this package name is correct

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {

    private final GeminiService geminiService;
    private final YouTubeService youTubeService;

    // Spring automatically provides the services here
    public AppRunner(GeminiService geminiService, YouTubeService youTubeService) {
        this.geminiService = geminiService;
        this.youTubeService = youTubeService;
    }

    @Override
    public void run(String... args) throws Exception {
        // =================================================================
        // ==     IMPORTANT: CHANGE THE TWO LINES BELOW                   ==
        // =================================================================
        String videoFilePath = "C:/path/to/your/downloaded_video.mp4"; // <-- SET THE FULL PATH TO YOUR VIDEO FILE
        String videoTopic = "a short clip of a cat playing with a laser pointer"; // <-- DESCRIBE YOUR VIDEO'S CONTENT HERE
        // =================================================================


        System.out.println("--> Asking Gemini to generate a title and description...");

        // This prompt asks Gemini for a specific format to make it easy to read
        String prompt = String.format(
                "Generate a catchy YouTube title and a short, engaging description for a video about '%s'. " +
                        "Format the output exactly like this, with no extra text:\n" +
                        "Title: [Your Title Here]\n" +
                        "Description: [Your Description Here]",
                videoTopic
        );

        String geminiResponse = geminiService.generateVideoMetadata(prompt);
        System.out.println("--> Gemini response received.");

        // This code parses the Title and Description from Gemini's response
        String title = geminiResponse.lines()
                .filter(s -> s.startsWith("Title:"))
                .findFirst()
                .map(s -> s.substring(7).trim())
                .orElse("My Awesome Video");

        String description = geminiResponse.lines()
                .filter(s -> s.startsWith("Description:"))
                .findFirst()
                .map(s -> s.substring(13).trim())
                .orElse("Check out this cool video I uploaded.");

        System.out.println("--> Title: " + title);
        System.out.println("--> Description: " + description);
        System.out.println("--> Starting YouTube upload process for: " + videoFilePath);

        youTubeService.uploadVideo(videoFilePath, title, description);

        System.out.println("\n--> PROCESS COMPLETE.");
    }
}