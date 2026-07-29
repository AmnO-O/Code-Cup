# Implementation Plan - Project Size Reduction (<100MB)

This plan aims to reduce the project's disk footprint to meet the 100MB submission limit by removing redundant files, optimizing build configurations, and cleaning up unused resources.

## User Review Required

> [!CAUTION]
> - **Permanent Deletion:** I will delete `24125015.zip` (142MB) and the `24125015/` folder. These appear to be backups.
> - **Build Cache:** I will run a `clean` task to remove the `build/` and `.gradle/` folders which are not needed for submission and consume significant space.
> - **Resource Optimization:** Enabling R8/Minification will reduce the final app size but won't affect the *source code* zip size directly. However, removing unused assets will.

## Proposed Changes

### 1. Massive File Cleanup (Immediate impact)
- [DELETE] `24125015.zip` (142MB)
- [DELETE] `24125015/` directory (redundant project copy)
- [DELETE] `screenshot_wg68fneb.png` (unused screenshot)

### 2. Build & Cache Cleanup
- Run `./gradlew clean` to remove all generated `build` folders.
- Advise the user to exclude the `.gradle` and `.idea` folders when zipping for submission (these should not be submitted).

### 3. Source Code Optimization
- [MODIFY] `app/build.gradle.kts`:
    - Enable `isMinifyEnabled = true` and `isShrinkResources = true` for release builds.
- [MODIFY] `OrderDao`: Add `VACUUM` support to ensure the database file stays small after data is cleared.

### 4. Asset Audit
- I will check the `app/src/main/res/font` and `drawable` folders for unnecessarily high-resolution assets and recommend removals if they are not used.

## Verification Plan

### Manual Verification
- Re-run `ls -l` to verify the total size of the project directory.
- Verify the app still builds successfully after the cleanup.
