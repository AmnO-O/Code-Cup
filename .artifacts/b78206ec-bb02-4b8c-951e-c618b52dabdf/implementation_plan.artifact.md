# Implementation Plan - Fix Product Customization Units and Categories

This plan addresses the issue where non-drink items (like cakes and pastries) are shown with drink-specific customizations (Ice Level, Espresso Shots) and incorrect size units (8oz, 12oz, etc.).

## User Review Required

> [!IMPORTANT]
> I will be adding a new **"Cakes"** category to the menu to better demonstrate the fix.
> Customization options for non-drinks will be simplified:
> - **Drinks**: Size (oz), Espresso Shots, Ice Level.
> - **Cakes**: Size (Slice vs Whole).
> - **Pastries**: No size/shots/ice customizations (Quantity only).

## Proposed Changes

### Domain Layer

#### [MODIFY] [PriceCalculator.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/domain/PriceCalculator.kt)
- Add logic to distinguish between drinks and non-drinks.
- Define separate size options for Cakes (e.g., "Single Slice", "Whole Cake").
- Update `unitPrice` and `totalPrice` to handle category-specific pricing (e.g., whole cake multiplier).

### Data Layer

#### [MODIFY] [SeedData.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/database/SeedData.kt)
- Add new products under a "Cakes" category:
    - Chocolate Truffle Cake ($4.00 per slice)
    - New York Cheesecake ($4.50 per slice)

### UI Layer

#### [MODIFY] [ProductDetailsViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/ProductDetailsViewModel.kt)
- Initialize `uiState` with category-appropriate defaults.
- Ensure `recalculatePrice` uses the product category.

#### [MODIFY] [ProductDetailsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/ProductDetailsScreen.kt)
- Conditionally render "Espresso Shots" and "Ice Level" sections (only for drinks).
- Use dynamic size options based on the product category.
- Hide the "Size" section for "Pastries" if they are uniform.

#### [MODIFY] [CartItem.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/models/CartItem.kt)
- Update `customizationSummary` to omit irrelevant details for non-drinks (e.g., "Regular Ice" shouldn't show for a Cake).

## Verification Plan

### Automated Tests
- Update `PriceCalculatorTest.kt` to verify pricing for Cakes and Pastries.
- Add a new test case for `ProductDetailsViewModel` to verify category-based defaults.

### Manual Verification
1. Launch the app and navigate to "Chocolate Truffle Cake" (newly added).
2. Verify that "Espresso Shots" and "Ice Level" are hidden.
3. Verify that "Size" shows "Single Slice" and "Whole Cake".
4. Select "Whole Cake" and verify the price increases significantly.
5. Navigate to "Butter Croissant" and verify only the "Quantity" selector is visible.
6. Add items to cart and verify the summary in the Cart Preview is clean (e.g., just "Whole Cake" instead of "Whole Cake, Single Shot, Regular Ice").
