# Fix Redemption Flow and Add Order Simulation

Refine the rewards redemption process and add background status simulation for free orders.

## Proposed Changes

### [Component] ViewModels & Logic

#### [MODIFY] [RedeemRewardsViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/RedeemRewardsViewModel.kt)
- Add `context: Context?` to constructor.
- Update `confirmRedeem(takeNow: Boolean)`:
    - If `takeNow` is `false`, just `dismissDialog()` without deducting points.
    - If `takeNow` is `true`, enqueue `OrderStatusWorker` to simulate the order status changes (Received -> Preparing -> Ready).

#### [MODIFY] [RewardsViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/RewardsViewModel.kt)
- Add `context: Context?` to constructor.
- Update `claimReward(choice: RewardChoice)`:
    - If `choice` is `FREE_DRINK`, enqueue `OrderStatusWorker` for the automatically placed free order.

#### [MODIFY] [ViewModelFactory.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/ViewModelFactory.kt)
- Pass `context` to `RewardsViewModel` and `RedeemRewardsViewModel`.

### [Component] UI Screens

#### [MODIFY] [RedeemRewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RedeemRewardsScreen.kt)
- Rename the "Save for Later" button in the `AlertDialog` to "Cancel" to better reflect the new non-deductive behavior.

#### [MODIFY] [MyOrdersScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/MyOrdersScreen.kt)
- Update the `PrimaryButton` in `OngoingOrderCard`:
    - Disable the button if `order.status != OrderStatus.Ready`.
    - Change the button text to "Preparing..." if not ready.

## Verification Plan

### Automated Tests
- Build the project and verify dependency injection.

### Manual Verification
1. **Redeem Flow**:
    - Click "Redeem" -> Select "Cancel" -> Verify points are NOT deducted.
    - Click "Redeem" -> Select "Order Now" -> Verify points ARE deducted and order simulation starts.
2. **Order Simulation**:
    - Place a $0 order (Redeem or Reward).
    - Navigate to **My Orders**.
    - Verify the status bar progresses from **Received** to **Preparing** to **Ready** over ~15 seconds.
3. **Pick Up Logic**:
    - While the order is "Preparing", verify the "Pick Up" button is disabled.
    - Once the status reaches "Ready", verify the button becomes enabled and allows completion.
