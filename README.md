# FRIDAY Android AI Assistant — Starter Project

This is the **starter Android project**, not a finished AI assistant. It provides an orange-and-black chat UI and the Android project structure to build on.

## Included now
- Native Android Kotlin + Jetpack Compose project
- Orange/black FRIDAY chat interface
- Message composer and placeholder attach/mic controls
- Internet, microphone and camera permissions declared
- Termux helper script

## Not implemented yet
Gemini Live voice streaming, ElevenLabs text-to-speech, camera streaming, Android screen capture, long-term memory, accessibility-based UI automation, earbuds/media-button launch, and media upload are future development phases. The buttons are placeholders and do not perform those actions yet.

## API key safety
Do not paste API keys into public source code, GitHub, screenshots, or chat messages. Keep keys private. A production app should call a small backend so provider secrets are not shipped inside the APK. `secrets.properties.example` is only a template.

## Build requirements
- Android SDK / Android build tools
- JDK 17
- Gradle wrapper (generate with `gradle wrapper` if not present)

## Build on a computer
From the project root:
```bash
gradle wrapper
./gradlew :app:assembleDebug
```
The APK will be at:
`app/build/outputs/apk/debug/app-debug.apk`

## Termux note
Termux alone does not include the Android SDK or a complete Android build environment. The script checks tools and prints the next steps; building an APK directly on a phone may require extra SDK setup and can be unreliable. You can edit the source in Termux and build with Android Studio or a configured remote build environment.

## Development roadmap
1. Connect Gemini text chat through a backend.
2. Add ElevenLabs speech output using the user's own voice ID.
3. Add Gemini Live session/audio pipeline.
4. Add Android runtime permission flow and camera preview/sharing.
5. Add MediaProjection screen sharing with Android's system consent dialog.
6. Add opt-in accessibility actions for safe, user-confirmed UI automation.
7. Add encrypted local memory and user controls to view/delete it.
8. Add supported Bluetooth/headset shortcut handling; behavior depends on the headset and Android version.
9. Add image/video picker and safe upload flow.
