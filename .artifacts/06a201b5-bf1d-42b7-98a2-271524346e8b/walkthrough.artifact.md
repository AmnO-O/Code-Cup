# Walkthrough - Theme Contrast and Design Restoration

I have fixed the contrast issues with the Sign Out button and customization chips, and restored the original design of the Rewards banner in Light Mode.

## Changes Made

### Theme & Colors
- **Contrast Improvement:** Updated `onPrimaryContainer` to `Color.White` in both Light and Dark themes. This ensures that any text on a `primaryContainer` (like selected customization chips) is perfectly legible.
- **Semantic Restoration:** Updated `tertiaryContainer` to use the original peach/orange accent color (`CoffeeAccentContainer`) for Light Mode.

### Profile Screen
- **Sign Out Visibility:** Updated the Sign Out button in [ProfileScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/ProfileScreen.kt) to use the orange `secondary` color scheme. This prevents it from blending into the dark background in Dark Mode while remaining prominent in Light Mode.

### Rewards Screen
- **Design Restoration:** Updated the Points Banner in [RewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RewardsScreen.kt) to use the `tertiary` color slots. This restores the original light orange look in Light Mode as requested.

### Product Details
- **Chip Readability:** The selected customization chips in [ProductDetailsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/ProductDetailsScreen.kt) now use white text on their dark brown background due to the `onPrimaryContainer` update, making them easy to read.

## Verification Results

### Automated Tests
- Ran `gradle :app:assembleDebug` successfully.

### Manual Verification Required
- **Rewards:** Verify the points banner has restored its light peach color in Light Mode.
- **Profile:** Verify the Sign Out button is orange and visible in both themes.
- **Product Details:** Verify that selected size/shot/ice options have clear white text.
