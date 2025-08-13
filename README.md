🤖 Gemini YouTube Uploader
Yo! So basically, this thing is a lifesaver if you upload vids to YouTube.

📋 What's Inside
The Lowdown

Cool Stuff It Does

The Tech Stuff

Getting It Set Up

How to Use It

The Legal Stuff
🌟 The Lowdown
Like, if you grab clips from your other Insta account or whatever and wanna post 'em quick, this app's got you. It uses Gemini AI to actually watch your video and cook up a sick title and description for you. Then, bam, it uploads the whole thing to YouTube. Done.

We're keeping it semi-auto, so you still gotta download the video yourself. That way, we're not breaking any Insta rules, ya know?

✨ Cool Stuff It Does
AI-Powered Titles & Descriptions: Gemini literally watches your video and writes the title/desc for you. So cool.

Straight to YouTube: Uploads your stuff right to your channel, no fuss.

Easy Peasy Setup: Just gotta drop a couple files in and you're good to go.

Solid AF: It's built with Spring Boot, so it just works.

🛠️ The Tech Stuff
Backend: Java 17, Spring Boot

Build Tool: Apache Maven

APIs:

Google Gemini API (the brain)

YouTube Data API v3 (the uploader)

Auth: Google OAuth 2.0

🚀 Getting It Set Up
Aight, let's get this thing running.

1. Stuff you need first
   Java 17 (or newer)

Maven

A Google Cloud project (it's free to start)

2. Get Your Keys & Stuff
   Turn on the APIs: In your Google Cloud project, you gotta enable YouTube Data API v3 and Vertex AI API.

Gemini API Key: Go to Google AI Studio and just grab a key. Easy.

OAuth Screen: In Google Cloud, find the "OAuth consent screen". Make a new one.

Pick "External" for user type.

Add your own Gmail as a "Test User" so you can use the app.

Get the Client ID:

Go to "Credentials" and create a new "OAuth client ID".

Choose "Desktop app".

Download the client_secret.json file it gives you. You'll need this!

3. Set Up the Project
   Clone the repo:

git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name

Drop in the secret file:

Take that client_secret.json file and stick it in the src/main/resources/ folder.

Update the properties file:

Open src/main/resources/application.properties and paste your Gemini key in there:

gemini.api.key=YOUR_GEMINI_API_KEY

One last change:

Open GeminiService.java and put your Google Cloud project ID in:

String projectId = "your-gcp-project-id"; // <-- CHANGE THIS

4. Build it!
   Just run this in your terminal:

mvn clean install

▶️ How to Use It
Alright, time for the fun part.

Tell it where the video is:

Go find AppRunner.java.

Change this line to the actual path of your video file.

String videoFilePath = "C:/path/to/your/video.mp4"; // <-- SET THIS PATH

Run it:

Pop open your terminal and run:

mvn spring-boot:run

Log in:

A browser window will pop up. Just log in with the Google account you wanna upload to.

Click "Allow" or whatever to give it permission.

That's literally it. The app will do its thing and you'll see the progress in the terminal. GG.

📄 The Legal Stuff
It's under the MIT License. Basically, do whatever you want with it. Check the LICENSE file for the boring legal stuff.