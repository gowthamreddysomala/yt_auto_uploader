package com.youtubeuploader.yt_auto_uploader.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class YouTubeService {

    private static final String APPLICATION_NAME = "Gemini YouTube Uploader";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/client_secret.json";
    private static final List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/youtube.upload");

    // Directory to store authorization tokens for a user
    private static final File CREDENTIALS_DIRECTORY = new File(System.getProperty("user.home"), ".credentials/youtube-uploader");

    private Credential getCredentials() throws IOException, GeneralSecurityException {
        var in = YouTubeService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_DIRECTORY))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void uploadVideo(String videoFilePath, String title, String description) throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials();

        YouTube youtubeService = new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Video video = new Video();

        VideoSnippet snippet = new VideoSnippet();
        snippet.setTitle(title);
        snippet.setDescription(description);
        video.setSnippet(snippet);

        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus("private"); // Can be "private", "public", or "unlisted"
        video.setStatus(status);

        File mediaFile = new File(videoFilePath);
        InputStreamContent mediaContent = new InputStreamContent("video/*", new FileInputStream(mediaFile));

        YouTube.Videos.Insert insertRequest = youtubeService.videos()
                .insert(Collections.singletonList("snippet,status"), video, mediaContent);

        System.out.println("Starting upload for: " + title);
        Video returnedVideo = insertRequest.execute();
        System.out.println("Upload complete! Video ID: " + returnedVideo.getId());
    }
}