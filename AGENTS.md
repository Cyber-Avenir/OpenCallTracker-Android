# OpenCallTracker

Android app (Kotlin, Jetpack Compose, no backend): auto-answers incoming calls, sends the `#` DTMF tone, hangs up, and keeps a local history. See `README.md` for the full context and the points of attention (default dialer role, minSdk).

## Architecture

MVVM, single `app` module split by package: `ui/main` and `ui/theme` (Compose screens, ViewModels, one composable per file under `components/`), `domain/model` and `domain/repository`, `data/history` (Room) and `data/settings` (DataStore), `service/` (`InCallService`), `di/` (`AppContainer`, manual injection).

- Composables hold no business logic and never touch Room, DataStore or the call service; they observe a ViewModel `StateFlow`.
- ViewModels and the call service depend on `domain` interfaces only.
- The detection on/off state is a single persisted value (DataStore) read by both the UI and the call service.

## Commands

- `./gradlew spotlessApply` formats the code, `./gradlew spotlessCheck` verifies it (run before each commit)
- `./gradlew assembleDebug` builds the debug APK
- `./gradlew test` runs unit tests

## Rules

- Keep it minimal: do only what is asked, no extra features.
- Formatting is defined by `.editorconfig` (ktlint, `android_studio` style, no line width limit).
- Dependencies go through `gradle/libs.versions.toml`.
