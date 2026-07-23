# Implementation Plan - Fix Theme Contrast and Restore Light Mode Design

This plan addresses the contrast issues with the Sign Out button, Product Details customization chips, and restores the original "Accent" color for the Rewards banner in Light Mode.

## User Review Required

> [!IMPORTANT]
> I will be updating the `tertiaryContainer` color in the theme to act as the "Accent" color (the light peach/orange) used in the Rewards screen. This ensures the Light Theme remains faithful to its original design while providing a proper semantic mapping for Dark Mode.

## Proposed Changes

### [Theme & Colors]

#### [MODIFY] [Theme.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/theme/Theme.kt)
- Update `LightColorScheme`:
    - Set `onPrimaryContainer` to `Color.White` for better contrast on dark brown backgrounds.
    - Set `tertiaryContainer` to `CoffeeAccentContainer` (the original light orange banner color).
    - Set `onTertiaryContainer` to `CoffeeOnSecondaryContainer` (for readable text on the accent banner).
- Update `DarkColorScheme`:
    - Set `onPrimaryContainer` to `Color.White`.
    - Ensure `tertiaryContainer` has a distinct dark-mode accent color.

### [Profile Screen]

#### [MODIFY] [ProfileScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/ProfileScreen.kt)
- Change the Sign Out button to use `secondary` and `onSecondary` (orange) instead of `primaryContainer`. This will prevent it from blending into the dark background in Dark Mode.

### [Rewards Screen]

#### [MODIFY] [RewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RewardsScreen.kt)
- Update the Points Banner to use `tertiaryContainer` and `onTertiaryContainer`. This restores the light orange look in Light Mode.

### [Product Details Screen]

#### [MODIFY] [ProductDetailsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/ProductDetailsScreen.kt)
- Update the customization chips to use `primary` and `onPrimary` (or `onPrimaryContainer` if set to White) for the selected state to ensure maximum readability.

## Verification Plan

### Automated Tests
- Run `gradle :app:assembleDebug` to verify compilation.

### Manual Verification
- **Profile:** Verify Sign Out button is orange and visible in both themes.
- **Rewards:** Verify the Points Banner is light orange in Light Theme (as it was originally).
- **Product Details:** Verify selected customization chips have white/very light text on the dark brown background.
- **Dark Mode:** Verify all these areas remain legible and well-contrasted in Dark Mode.
