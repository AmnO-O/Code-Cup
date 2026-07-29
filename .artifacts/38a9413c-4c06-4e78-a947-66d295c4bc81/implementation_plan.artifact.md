# Implementation Plan - Project Reset & Deep Clean

This plan focuses on reverting the "Reset" feature code (as requested) and performing a thorough cleanup of the project structure to ensure it is under the 100MB limit for submission with a "fresh" state.

## User Review Required

> [!IMPORTANT]
> - I will **revert** the code changes made previously (Danger Zone UI, DAO/Repository methods) because you mentioned you don't need the code.
> - I will perform a **deep clean** of the `app/build` and other temporary folders which are currently taking up >180MB.

## Proposed Changes

### 1. Revert Code Changes (Clean Source)
- [MODIFY] `Daos.kt`: Remove `clearAll()` method.
- [MODIFY] `OrderRepository.kt`: Remove `clearOrderHistory()` method.
- [MODIFY] `ProfileViewModel.kt`: Remove `resetOrderHistory()` method and `orderRepository` property.
- [MODIFY] `ProfileScreen.kt`: Remove the **Danger Zone** UI section and confirmation dialog.

### 2. Deep Project Cleanup
- Run `./gradlew clean` to wipe `app/build`.
- Check `Midterm Report` folder for very large image files and list them for your review (it's currently 67MB).

### 3. App Data Reset (Environment)
- Attempt to clear app data on the connected device via ADB: `adb shell pm clear com.example.codecup`.

## Verification Plan

### Manual Verification
- Verify the `app/build` folder is deleted.
- Check the final size of the project directory.
- Ensure the app starts with a "fresh" state (no previous orders/points) when run.
