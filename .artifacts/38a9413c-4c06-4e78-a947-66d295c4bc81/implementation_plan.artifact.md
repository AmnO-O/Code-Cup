# Implementation Plan - Fix Stamp Accumulation Bug

Address the issue where stamps stop accumulating at 8/8, causing users to lose stamps earned from orders placed before they redeem their full card.

## User Review Required

> [!IMPORTANT]
> The stamps will now accumulate beyond 8. For example, if you have 8 stamps and complete another order, you will have 9 stamps. When you "Reset" the card to claim your reward, it will subtract 8 stamps, leaving the remaining 1 stamp for your next card.

## Proposed Changes

### Data Layer

#### [MODIFY] [RewardsRepository.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/RewardsRepository.kt)
- Update `awardForCompletedOrder` to remove the `.coerceAtMost(STAMPS_PER_CARD)` cap on stamps.
- Update `clearStamps` to subtract `STAMPS_PER_CARD` from the current stamp count instead of setting it to 0. Use `coerceAtLeast(0)` as a safety measure.

### UI Components

#### [MODIFY] [LoyaltyCard.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/components/LoyaltyCard.kt)
- The existing logic already handles `stampsEarned >= totalStamps` for the "Full" state.
- The stamp circles will remain all filled if `stampsEarned > 8`.
- The text will show the actual count (e.g., "9 / 8"), which confirms to the user that their extra orders are being counted.

## Verification Plan

### Manual Verification
1.  **Seed 8 Stamps**: Use the demo seeder or place enough orders to reach 8/8.
2.  **Place 9th Order**: Complete one more order and mark it as "Picked Up".
3.  **Check Balance**: Navigate to the Rewards screen and verify the card shows "9 / 8".
4.  **Redeem Reward**: Tap the card and claim a reward (points or drink).
5.  **Check Carry-over**: Verify the card now shows "1 / 8".
6.  **Verify UI**: Ensure the "X stamps until..." text is hidden and the "Tap to redeem" message is shown whenever stamps >= 8.
