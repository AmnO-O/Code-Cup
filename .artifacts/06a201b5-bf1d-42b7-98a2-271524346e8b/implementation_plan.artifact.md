# Implementation Plan - Order Status Simulation & Notifications

We should definitely proceed with these features. They add a significant layer of realism to the coffee ordering experience and demonstrate high-level Android development skills like background task management and platform API integration.

## Goal Description
Implement an automated order status transition (Received → Preparing → Ready) using `WorkManager` and notify the user via a local push notification when their coffee is "Ready".

## User Review Required

> [!IMPORTANT]
> I will be adding the `androidx.work:work-runtime-ktx` dependency to your project.
> I will also request the `POST_NOTIFICATIONS` permission (for Android 13+) in the `AndroidManifest.xml`.

## Proposed Changes

### [Dependencies & Configuration]

#### [MODIFY] [build.gradle.kts](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/build.gradle.kts)
- Add `androidx.work:work-runtime-ktx` dependency.

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/AndroidManifest.xml)
- Add `POST_NOTIFICATIONS` permission.
- (Internal) Register the `WorkManager` default initializer if necessary, though it's usually automatic.

### [Data Layer]

#### [MODIFY] [OrderRepository.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/OrderRepository.kt)
- Add a method to trigger the simulation when an order is placed.

### [Background Simulation]

#### [NEW] [OrderStatusWorker.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/workers/OrderStatusWorker.kt)
- Create a `CoroutineWorker` that handles:
    1.  Waiting for a few seconds.
    2.  Updating status to `Preparing`.
    3.  Waiting again.
    4.  Updating status to `Ready`.
    5.  Triggering a notification.

### [Notifications]

#### [NEW] [NotificationHelper.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/utils/NotificationHelper.kt)
- Utility class to create a Notification Channel and post the "Order Ready" alert.

### [UI Layer]

#### [MODIFY] [CartViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/CartViewModel.kt)
- Change initial order status from `Preparing` to `Received`.
- Trigger the `WorkManager` simulation upon checkout.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/MainActivity.kt)
- Request notification permissions on launch for Android 13+ devices.

## Verification Plan

### Automated Tests
- No specific automated tests, but I will ensure the project builds successfully with new dependencies.

### Manual Verification
1.  Place an order in the app.
2.  Navigate to the "Orders" screen.
3.  Observe the status bar change from "Received" to "Preparing" and finally "Ready" over ~20-30 seconds.
4.  Verify that a system notification appears when the status becomes "Ready".
