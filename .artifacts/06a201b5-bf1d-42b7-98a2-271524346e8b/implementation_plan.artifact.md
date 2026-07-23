# Implementation Plan - Rewards & Redeem Logic

Implement the missing business logic for earning and redeeming rewards points, as well as tracking loyalty stamps.

## User Review Required

> [!IMPORTANT]
> I will be modifying the `ProfileRepository` and `UserProfile` model to include points history and stamps. I will also be creating new ViewModels for the Rewards and Redeem screens.

## Proposed Changes

### [Data Layer]

#### [MODIFY] [ProfileRepository.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/ProfileRepository.kt)
- Update `UserProfile` data class:
    - Add `stamps: Int`.
    - Add `pointsHistory: List<PointsHistoryItem>`.
- Add methods to `ProfileRepository`:
    - `addPoints(amount: Int, title: String)`
    - `redeemPoints(amount: Int, title: String)`
    - `addStamp()`
    - `resetStamps()`

#### [NEW] [PointsHistoryItem.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/models/PointsHistoryItem.kt)
- Create a shared model for points history entries.

### [Logic & ViewModels]

#### [NEW] [RewardsViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/RewardsViewModel.kt)
- Observe `ProfileRepository` to expose points, stamps, and history.

#### [NEW] [RedeemRewardsViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/RedeemRewardsViewModel.kt)
- Handle the redemption process: call `redeemPoints` and show success/error states.

#### [MODIFY] [CartViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/CartViewModel.kt)
- In `checkout()`:
    - Calculate points earned (e.g., $1 = 5 points).
    - Call `profileRepository.addPoints()` and `profileRepository.addStamp()`.

#### [MODIFY] [HomeViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/HomeViewModel.kt)
- Observe `ProfileRepository` to expose `stampsEarned` in `HomeUiState`.

#### [MODIFY] [ViewModelFactory.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/ViewModelFactory.kt)
- Add support for new ViewModels.

### [UI Layer]

#### [MODIFY] [RewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RewardsScreen.kt)
- Connect to `RewardsViewModel`.
- Remove hardcoded sample data.

#### [MODIFY] [RedeemRewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RedeemRewardsScreen.kt)
- Connect to `RedeemRewardsViewModel`.
- Implement "Redeem" button click logic.

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/home/HomeScreen.kt)
- Update `LoyaltyCard` to use `uiState.stampsEarned`.

## Verification Plan

### Automated Tests
- Build the project to ensure all ViewModel connections are correct.

### Manual Verification
1.  **Earn Points:** Place an order and verify that points and a stamp are added to the user's profile.
2.  **View History:** Navigate to the Rewards screen and check the "Points History" list.
3.  **Redeem Reward:** Navigate to the Redeem screen, select an item, and verify that points are deducted and a new history entry is added.
4.  **Loyalty Card:** Verify that stamps are correctly displayed on both the Home and Rewards screens.
