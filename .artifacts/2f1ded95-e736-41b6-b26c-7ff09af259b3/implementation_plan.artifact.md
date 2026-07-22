# Implementation Plan - Redeem Rewards Functionality

Currently, the "Redeem" button in the `RedeemRewardsScreen` has an empty `onClick` handler, and the points/stamps on the Rewards screens are hardcoded. This plan aims to implement the logic for redeeming rewards, managing points, and providing user feedback.

## Proposed Changes

### Data Layer

#### [MODIFY] [ProfileRepository.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/ProfileRepository.kt)
- Add `stamps: Int` to `UserProfile` data class.
- Add `deductPoints(amount: Int)` method.
- Add `addPoints(amount: Int)` method.
- Add `addStamp()` and `resetStamps()` methods.

#### [MODIFY] [OrderRepository.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/OrderRepository.kt)
- Add a method `createRedeemedOrder(product: Product)` to generate a $0 order.

### UI Layer

#### [NEW] [RewardsViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/RewardsViewModel.kt)
- Create a new ViewModel to handle rewards logic.
- Expose the user's points and stamps balance.
- Implement a `redeemProduct(product: Product)` function:
    1. Checks if `points >= cost`.
    2. Calls `profileRepository.deductPoints(cost)`.
    3. Calls `orderRepository.createRedeemedOrder(product)` so it appears in "My Orders".
- Provide a `UIState` to handle loading and success messages.

#### [MODIFY] [RewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RewardsScreen.kt)
- Inject `RewardsViewModel`.
- Replace hardcoded points (1240) and stamps (5) with values from the ViewModel.

#### [MODIFY] [RedeemRewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RedeemRewardsScreen.kt)
- Inject `RewardsViewModel`.
- Replace hardcoded points (1240) with values from the ViewModel.
- Implement the `onClick` handler for the "Redeem" button to call `viewModel.redeemProduct(product)`.
- Add a Snackbar or Dialog to show success feedback after redemption.

#### [MODIFY] [NavGraph.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/navigation/NavGraph.kt)
- Pass the `RewardsViewModel` to the screens in the navigation graph (using `ViewModelFactory`).

## Verification Plan

### Automated Tests
- Create unit tests for `RewardsViewModel` to verify point deduction and "insufficient points" logic.
- Create unit tests for `ProfileRepository` to verify data updates.

### Manual Verification
1. Open the **Rewards** screen. Observe the current points (should be 850 by default from `ProfileRepository`).
2. Navigate to **Redeem Rewards**.
3. Click **Redeem** on a product (e.g., Espresso for 125 pts).
4. Verify that points are deducted (850 -> 725).
5. Verify that a success message appears.
6. Navigate to **My Orders** and verify that a new $0.00 order for "Espresso" is in the **Ongoing** tab.
7. Return to the **Rewards** screen and verify the balance is updated.
