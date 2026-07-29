# Implementation Plan - Expandable Order Details

Implement expandable order cards in the "My Orders" screen. Details like item lists, delivery addresses, and progress indicators will be hidden by default and revealed when the user clicks on the order card.

## User Review Required

> [!NOTE]
> For **Ongoing Orders**, the status, order ID, total price, and date will remain visible. The item list, delivery address, and progress/action section will be hidden until expanded.
> For **History Orders**, the order ID, total price, and date will remain visible. The item summary and reorder button will be hidden until expanded.

## Proposed Changes

### UI Layer

#### [MODIFY] [MyOrdersScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/MyOrdersScreen.kt)
- Add `import androidx.compose.animation.AnimatedVisibility` and `import androidx.compose.foundation.clickable`.
- **OngoingOrderCard**:
    - Add `expanded` state using `remember { mutableStateOf(false) }`.
    - Apply `Modifier.clickable { expanded = !expanded }` to the `Surface`.
    - Wrap the following in `AnimatedVisibility`:
        - `HorizontalDivider`
        - Order items list
        - `DeliveryAddressSection`
        - Progress indicator section
        - Action button ("Mark as Picked Up")
- **HistoryOrderCard**:
    - Add `expanded` state.
    - Change layout to a `Column` to allow vertical expansion.
    - Move header info into a `Row`.
    - Wrap `itemsSummary` and the reorder button section in `AnimatedVisibility`.

## Verification Plan

### Manual Verification
1.  **Ongoing Orders**:
    *   Navigate to "My Orders".
    *   Verify that ongoing orders only show the header (ID, Price, Status).
    *   Click the card.
    *   Verify that the items, address, and progress bar appear with an animation.
    *   Click again to collapse.
2.  **History Orders**:
    *   Verify that past orders only show the ID and Price (and date).
    *   Click the card.
    *   Verify that the item summary and reorder button appear.
    *   Click again to collapse.
