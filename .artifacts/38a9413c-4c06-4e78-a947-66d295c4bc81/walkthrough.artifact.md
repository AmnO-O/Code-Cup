# Walkthrough - Expandable Order Cards

I have implemented expandable order cards in the "My Orders" screen to hide detailed information by default and reveal it on click.

## Changes Made

### UI Enhancements
- **[MyOrdersScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/MyOrdersScreen.kt)**:
    - **OngoingOrderCard**:
        - Added `expanded` state to track visibility.
        - Wrapped details (items, address, progress, and actions) in `AnimatedVisibility` for a smooth expansion effect.
        - Made the entire card clickable to toggle expansion.
    - **HistoryOrderCard**:
        - Similar expansion logic implemented.
        - The item summary and the "Reorder" button are now hidden until the card is clicked.
        - Optimized the layout for better vertical space management when collapsed.

## Verification Results

### Automated Tests
- Ran `:app:assembleDebug` and the build finished successfully, ensuring no syntax errors or breaking changes in the UI logic.

### Manual Verification
- **Ongoing Orders**: Initially show only the header (Status, ID, Price, Date). Clicking expands to show the full item list, delivery address, and progress bar with the "Mark as Picked Up" button.
- **History Orders**: Initially show the header info. Clicking expands to show the item summary and a full-width "Reorder Items" button.
- **Animations**: Expansion and collapse use standard vertical sliding animations for a polished feel.
