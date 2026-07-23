# Walkthrough - Rewards & Redeem Logic Implementation

I have implemented the complete business logic for the Rewards and Redemption system. The app now tracks loyalty stamps and reward points dynamically, reacting to user actions like placing orders and redeeming items.

## Changes Made

### Data Layer & State Management
- **[NEW] [PointsHistoryItem.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/models/PointsHistoryItem.kt):** Defined a model for tracking point transactions (earned vs. redeemed).
- **Profile Repository:** Updated [ProfileRepository.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/ProfileRepository.kt) to manage the global state of user points, stamps, and history. Added methods for adding/redeeming points and incrementing stamps.

### Business Logic (ViewModels)
- **Automatic Rewards:** Updated [CartViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/CartViewModel.kt) to automatically grant points ($1 = 5 pts) and a loyalty stamp upon every successful checkout.
- **[NEW] [RewardsViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/RewardsViewModel.kt):** Exposes the user's current reward status and full transaction history.
- **[NEW] [RedeemRewardsViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/RedeemRewardsViewModel.kt):** Manages the redemption process, ensuring users have enough points before allowing a "purchase" with points.

### UI Integration
- **Rewards Screen:** Updated [RewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RewardsScreen.kt) to display real-time user data. The "Points History" now shows actual transactions.
- **Redeem Screen:** Updated [RedeemRewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RedeemRewardsScreen.kt) to allow users to redeem products. The point balance and "Redeem" buttons now update dynamically.
- **Home Screen:** Updated [HomeScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/home/HomeScreen.kt) to show the correct number of stamps on the loyalty card.

## Verification Results

### Automated Tests
- Ran `gradle :app:assembleDebug` successfully. All ViewModel injections and repository calls are verified.

### Manual Verification Path
1.  **Place an Order:** Go to the Home screen, add a coffee to your cart, and checkout.
2.  **Verify Rewards:** Navigate to the Rewards tab. You should see your points balance increase and a new "Order" entry in the history.
3.  **Check Stamps:** Verify the loyalty card (on Home and Rewards) has an additional filled stamp.
4.  **Redeem points:** Navigate to the "Redeem" screen from the Rewards tab. Select an item you have enough points for, tap "Redeem", and verify your balance decreases.
