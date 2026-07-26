# Celebration Fireworks & Confetti Walkthrough

I have added a celebratory burst effect to the rewards and redemption flows to make successful actions feel more rewarding.

## Changes Made

### 1. Fireworks/Confetti Animation
- **Burst Physics**: Modified the `ConfettiEffect` to act like a firework burst. Particles now launch upwards from the bottom-middle of the screen and then fall gracefully due to simulated gravity.
- **Single Shot**: The animation now triggers once and lasts for about 2.5 seconds, providing a clear "congratulations" moment without cluttering the UI permanently.

### 2. Integration with Redemption
- **Redeem Celebration**: In the **Redeem Rewards** screen, the celebration effect triggers immediately after you confirm an "Order Now" action.
- **State Management**: The `RedeemRewardsViewModel` now tracks `showCelebration` and resets it automatically after the animation finishes.

### 3. Integration with Loyalty Rewards
- **Loyalty Celebration**: In the **Rewards** screen, the effect triggers when you successfully claim a reward (either the 500 points or the free drink).
- **Visual Feedback**: Provides immediate visual confirmation that your loyalty stamps have been processed.

## Verification Results

### Automated Tests
- `gradle assembleDebug` passed successfully.
- Verified that the `ConfettiEffect` correctly uses the new `trigger` parameter to start and `onAnimationEnd` to clean up state.

### Manual Verification Steps
- **Redeem**: Confirm a reward order. Verify the colorful burst appears from the bottom of the screen.
- **Loyalty**: Claim your free drink after 8 stamps. Verify the celebratory effect triggers.
- **Auto-Dismiss**: Ensure the effect disappears on its own after ~2.5 seconds.
