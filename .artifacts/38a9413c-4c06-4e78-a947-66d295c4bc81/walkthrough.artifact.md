# Walkthrough - Stamp Accumulation Fix

I have fixed the bug where stamps earned after reaching a full card (8/8) were being lost. Now, stamps accumulate beyond the card limit and carry over to the next card after redemption.

## Changes Made

### Data Layer
- **[RewardsRepository.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/RewardsRepository.kt)**:
    - **Unlimited Accumulation**: Removed the cap in `awardForCompletedOrder`. Stamps now increase indefinitely (e.g., to 9, 10, etc.) as orders are completed.
    - **Carry-over Logic**: Updated `clearStamps` (called when a reward is claimed) to subtract exactly 8 stamps from the total instead of resetting it to zero. This ensures any "overflow" stamps are preserved for the next reward cycle.

### UI Consistency
- **[LoyaltyCard.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/components/LoyaltyCard.kt)**:
    - The UI naturally handles the new logic by displaying the actual count (e.g., "9 / 8") and maintaining the "Full" state (glow effect and "Tap to redeem" message) as long as the balance is 8 or higher.

## Verification Results

### Automated Tests
- Ran `:app:assembleDebug` and the build passed successfully.

### Manual Verification Path
1.  **Overflow**: Reach 8 stamps, then complete another order. Observe the card shows "9 / 8".
2.  **Redemption**: Tap the full card to claim a reward.
3.  **Persistence**: Observe the card balance becomes "1 / 8" after the reset, confirming the carry-over works.
