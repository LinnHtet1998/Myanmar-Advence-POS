# Myanmar Advanced POS (Android starter project)

မြန်မာဘာသာသုံး ဆိုင်တစ်ဆိုင်စာ POS Android project အခြေခံဖွဲ့စည်းပုံ။

## Included
- Jetpack Compose dashboard shell (Burmese labels)
- Room local database entities/DAO for items, invoices, expenses
- Product image URI field (photo picker/camera UI still needs wiring)
- Google Drive `appDataFolder` JSON backup payload and upload transport

## Important: not a finished APK
This is a starter source project, not a compiled or tested APK. The sales workflow, invoice line items, purchase/supplier/customer screens, reports, restore/import, photo picker, and OAuth sign-in/authorization UI still need implementation and testing.

## Google Drive setup still required
1. Create/select a Google Cloud project.
2. Configure OAuth consent screen and Android OAuth client for package `com.myanmaradvancedpos` with your app signing SHA-1.
3. Request Drive app data scope: `https://www.googleapis.com/auth/drive.appdata` using Google Identity AuthorizationClient.
4. Obtain a short-lived access token after the user approves access, then call `GoogleDriveBackup.upload(token, backupJson)`.
5. Implement download/restore and test on a real device. Do not embed client secrets or store Google passwords in the app.

## Build
Open this folder in Android Studio (recent stable version), allow Gradle sync, then Build > Build Bundle(s) / APK(s) > Build APK(s).

The current source has not been compiled in this environment. Resolve any Android Studio/Gradle compatibility warnings before distributing it.

## Build APK using only an Android phone (GitHub Actions)
1. Create a GitHub repository in the browser and upload all files/folders inside this project (including `.github/workflows/android-build.yml`).
2. Open the repository's Actions tab. Select `Android APK Build` and press `Run workflow` (or push to `main`).
3. Wait for the workflow to finish successfully. Open the completed run and download the artifact named `MyanmarAdvancedPOS-debug-apk`.
4. Extract the artifact ZIP on your phone, tap the APK, and allow installation from that source if Android asks.

This workflow builds a debug APK. It does not complete the unfinished POS screens or Google Drive OAuth/restore implementation; see the limitations above.
