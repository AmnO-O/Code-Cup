# Celebration Effect for Rewards and Redemption

Add a celebratory fireworks/confetti effect when a user successfully redeems a reward or claims a loyalty bonus.

## Proposed Changes

### [Component] UI Components

#### [MODIFY] [ConfettiEffect.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/components/ConfettiEffect.kt)
- Update the `ConfettiEffect` to be triggered by a state change.
- Modify the animation to run once (e.g., 3-5 seconds) instead of infinitely repeating.
- Tweak the particle physics to feel more like a burst of fireworks (upward burst then falling).

### [Component] ViewModels

#### [MODIFY] [RedeemRewardsViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/RedeemRewardsViewModel.kt)
- Add `showCelebration: Boolean` to `RedeemRewardsUiState`.
- Trigger `showCelebration = true` upon successful redemption.
- Add a way to reset the celebration state.

#### [MODIFY] [RewardsViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/RewardsViewModel.kt)
- Add `showCelebration: Boolean` to `RewardsUiState`.
- Trigger `showCelebration = true` upon claiming a reward.
- Add a way to reset the celebration state.

### [Component] UI Screens

#### [MODIFY] [RedeemRewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RedeemRewardsScreen.kt)
- Overlay the `ConfettiEffect` when `uiState.showCelebration` is true.

#### [MODIFY] [RewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RewardsScreen.kt)
- Overlay the `ConfettiEffect` when `uiState.showCelebration` is true.

## Verification Plan

### Automated Tests
- Build the project and ensure animations are correctly handled.

### Manual Verification
1. **Redeem Celebration**:
    - Navigate to **Redeem Rewards**.
    - Confirm an order.
    - Verify that a celebration effect appears on the screen for a few seconds.
2. **Loyalty Celebration**:
    - Go to **Rewards**.
    - Claim a reward (Points or Free Drink).
    - Verify the celebration effect triggers.
