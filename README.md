# Installation
Download this repository and open it in android studio.
Get your private key to access the ChatGPT API from Open AI.
Add `GPT_API_KEY="pk-your_key"` to the `local.properties` file.
Add a path to your `python.exe` in your module-level `build.gradle` file under build Python, like this:
```
defaultConfig {
 build Python("your path\\python.exe")
}
```
Rebuild and run the project.
