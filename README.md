# INFO 448: QuizDroid
Lauren Wong

## HW Tasks
Refactor the TopicRepository to read a JSON file (data/questions.json) to use as the source of the Topics and Questions. Use a hard-coded file (available at http://tednewardsandbox.site44.com/questions.json) pushed to the device with adb for now; do NOT include it as part of the application's "assets", as a future version will be replacing the file after the application has been deployed.

The application should provide a "Preferences" action bar item that brings up a "Preferences" activity containing the application's configurable settings: URL to use for question data, and how often to check for new downloads measured in minutes. If a download is currently under way, these settings should not take effect until the next download starts.

## Extra Credit
My custom JSON file can be found here: https://raw.githubusercontent.com/lwong121/quizdroid-data/main/quizdroid-questions.json
All the screenshots can be found in the /screenshots directory.