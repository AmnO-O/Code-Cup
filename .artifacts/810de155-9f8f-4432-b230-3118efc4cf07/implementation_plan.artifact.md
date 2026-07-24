# Implementation Plan - Manual Loyalty Card Reset

Implement the "Manual Reset" logic for the loyalty card as required by the rubric. This allows users to explicitly redeem their reward once 8 stamps are collected, rather than automatically resetting. Also, set the default stamps to 7 for easier testing.

## User Review Required

> [!IMPORTANT]
> The default stamp count will be set to **7** in the `UserProfile` model. This is for testing purposes and can be changed back to 0 or 5 later.

## Proposed Changes

### Data Layer

#### [MODIFY] [ProfileRepository.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/ProfileRepository.kt)
- Update `UserProfile` data class to include `freeDrinks: Int = 0` and set default `stamps = 7`.
- Modify `addStamp()` to cap at 8 stamps instead of auto-resetting.
- Add `resetStamps()` function to reset stamps to 0 and increment `freeDrinks`.

### View Model Layer

#### [MODIFY] [RewardsViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/RewardsViewModel.kt)
- Add `resetLoyaltyCard()` method that calls `profileRepository.resetStamps()`.

### UI Layer

#### [MODIFY] [RewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RewardsScreen.kt)
- Update the `LoyaltyCard` call to handle `onClick`.
- Implement logic: if `stamps == 8`, call `viewModel.resetLoyaltyCard()` and show a "Reward Redeemed" message (using Snackbar).
- If `stamps < 8`, show a message indicating how many more stamps are needed.

---

## Verification Plan

### Automated Tests
- N/A (Manual verification is primary for this UI/UX change).

### Manual Verification
1. Open the **Rewards** screen. Verify that the card shows **7/8 stamps** by default.
2. Go to the **Home** screen, add an item to the cart, and complete an order.
3. Return to the **Rewards** screen. Verify the card now shows **8/8 stamps**.
4. **Click on the Loyalty Card**.
    - Verify that a Snackbar appears saying "Reward Redeemed! Enjoy your free drink."
    - Verify that the stamps reset to **0/8**.
5. Click on the card again (when stamps < 8). Verify it shows a message like "Collect 8 stamps to get a free drink!".
