# Walkthrough - Order Status Simulation & Notifications

I have successfully implemented an automated order status simulation and local push notifications. This adds a layer of interactivity and realism to the Artisan Coffee app.

## Changes Made

### Background Simulation & Tasks
- **WorkManager Integration:** Added `androidx.work:work-runtime-ktx` to handle background order processing.
- **[NEW] [OrderStatusWorker.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/workers/OrderStatusWorker.kt):** A `CoroutineWorker` that simulates a realistic coffee preparation timeline:
    - `Received` → `Preparing` (after 5 seconds)
    - `Preparing` → `Ready` (after 10 seconds)
- **Automatic Trigger:** The simulation now starts immediately when a user checkouts from the [CartScreen](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/CartScreen.kt).

### Notifications
- **[NEW] [NotificationHelper.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/utils/NotificationHelper.kt):** A utility class to manage the notification channel and post alerts.
- **Order Ready Alert:** When the `OrderStatusWorker` completes its simulation (status becomes `Ready`), a local push notification is sent to the user: *"Your Coffee is Ready! ☕"*.
- **Permission Handling:** Added `POST_NOTIFICATIONS` to the manifest and implemented a permission request flow in [MainActivity.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/MainActivity.kt) for Android 13+ support.

### Infrastructure Updates
- **Dependency Management:** Updated `libs.versions.toml` and `build.gradle.kts` with WorkManager.
- **ViewModel Architecture:** Updated `ViewModelFactory` and `CartViewModel` to support context-aware operations required by WorkManager.

## Verification Results

### Automated Tests
- Ran `gradle :app:assembleDebug` successfully. All dependencies resolved correctly.

### Manual Verification
1.  **Grant Permission:** Launch the app and allow notification permissions when prompted.
2.  **Order Placement:** Add items to your cart and tap **Checkout**.
3.  **Real-time Updates:** Go to the **Orders** tab. You will see your new order start at `Received`.
4.  **Simulation Flow:**
    - After ~5 seconds, it will automatically switch to `Preparing`.
    - After another ~10 seconds, it will switch to `Ready`.
5.  **Notification:** Observe the push notification appearing in your status bar exactly when the order becomes `Ready`.
