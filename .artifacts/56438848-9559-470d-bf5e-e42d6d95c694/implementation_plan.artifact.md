# Implementation Plan - Category Filtering & Navigation Fix

The goal is to implement category-based filtering on the Home screen and resolve the navigation issue where users cannot return from the Rewards screen to the Home screen.

## User Review Required

> [!IMPORTANT]
> I will be adding a `category` field to the `Product` model. This is a breaking change for the model, but since it's a local mock, I will update all sample data accordingly.

## Proposed Changes

### [Models]

#### [MODIFY] [Product.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/models/Product.kt)
- Add `val category: String` to the `Product` data class.
- Update `sampleProducts` with categories: "Espresso", "Cold Brew", "Pastries".

### [Home Feature]

#### [MODIFY] [HomeViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/HomeViewModel.kt)
- Update `HomeUiState` to include:
    - `selectedCategory: String` (defaulting to "All Coffee").
    - `categories: List<String>` (derived from data or hardcoded).
- Add `selectCategory(category: String)` method.
- Filter the `products` list in `loadProducts` (or via a `Flow` transformation) based on the `selectedCategory`.

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/home/HomeScreen.kt)
- Update `CategoryChips` to:
    - Accept `selectedCategory` and `onCategorySelected` callback.
    - Dynamically render chips based on the state.
    - Highlight the selected chip.
- Pass the selected category and selection logic from the ViewModel to `CategoryChips`.

### [Navigation]

#### [MODIFY] [NavGraph.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/navigation/NavGraph.kt)
- Standardize the `onNavigate` logic for bottom navigation destinations.
- Use `navController.graph.findStartDestination().id` for `popUpTo` to ensure consistent behavior when navigating back to the home screen.

#### [MODIFY] [RewardsScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RewardsScreen.kt)
- Ensure the back button in `AppHeader` correctly triggers navigation to "home".
- Consider adding a direct `navController.popBackStack()` or ensuring `onNavigate("home")` behaves correctly as a "back" action.

## Verification Plan

### Manual Verification
- **Category Filter**: Tap "Espresso" on the Home screen. Verify that only espresso products are shown. Tap "All Coffee" to show everything again.
- **Rewards Navigation**:
    1. Navigate from Home to Rewards.
    2. Click the "Home" icon in the Bottom Bar. Verify it returns to Home.
    3. Navigate back to Rewards.
    4. Click the "Back" arrow in the Top Bar. Verify it returns to Home.
- **State Survival**: Verify that the selected category persists across simple UI interactions (like opening the cart preview and closing it).
