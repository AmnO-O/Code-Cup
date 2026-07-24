# Walkthrough - Manual Loyalty Card Reset

I have implemented the manual reset logic for the loyalty card and updated the default state for easier testing.

## Changes Made

### 1. Enhanced User Profile Logic
In [ProfileRepository.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/ProfileRepository.kt):
- **Set default stamps to 7**: You only need 1 more order to fill the card.
- **Added `freeDrinks` tracking**: The profile now tracks how many free drinks have been earned.
- **Manual Reset**: `addStamp()` now caps at 8. A new `resetStamps()` function handles the transition from 8 back to 0.

### 2. Connected UI to Business Logic
In [RewardsViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/RewardsViewModel.kt) and [RewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RewardsScreen.kt):
- Added `resetLoyaltyCard()` to the ViewModel.
- Implemented `onClick` for the `LoyaltyCard` component.
- Added a `Snackbar` to provide feedback when the user clicks the card or redeems a reward.

## How to Test

1. **Check Default State**: Open the **Rewards** screen. You should see **7/8 stamps**.
2. **Fill the Card**: Place one order. Go back to the **Rewards** screen. It should now show **8/8 stamps**.
3. **Redeem**: Tap on the card.
    - You will see a "Reward Redeemed!" message.
    - The stamps will reset to **0**.
4. **Validation**: Tap the card again (when < 8). It will tell you how many more stamps are needed.

> [!TIP]
> This manual reset flow ensures you hit the specific rubric requirement for "explicit user action" to reset the loyalty card.
