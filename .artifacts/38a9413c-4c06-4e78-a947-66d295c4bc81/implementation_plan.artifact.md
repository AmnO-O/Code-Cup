# Implementation Plan - Refine My Orders UX

Refine the "My Orders" screen by removing address editing and replacing the "Cancel Order" button with a swipe-to-dismiss gesture for a cleaner, more modern interface.

## User Review Required

> [!NOTE]
> Address editing will be completely removed from the "My Orders" screen as requested. The delivery address will be static once the order is placed.

> [!IMPORTANT]
> The swipe-to-dismiss gesture will only be active when the order is in a "cancellable" state (Received or recently Preparing). If the order is Ready or exceeds the grace period, swiping will be disabled.

## Proposed Changes

### UI Components

#### [MODIFY] [AddressComponents.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/components/AddressComponents.kt)
- Make `onEditClick` parameter in `DeliveryAddressSection` optional (`(() -> Unit)? = null`).
- Only render the "Edit" button if `onEditClick` is not null.

### ViewModels

#### [MODIFY] [MyOrdersViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/MyOrdersViewModel.kt)
- [DELETE] `updateOrderAddress` function as it will no longer be used.

### Screens

#### [MODIFY] [MyOrdersScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/MyOrdersScreen.kt)
- Remove all logic related to `onEditAddress` and `EditAddressDialog`.
- Update `OngoingOrdersList` to wrap `OngoingOrderCard` in a `SwipeToDismissBox`.
- Remove the "Cancel Order" button from `OngoingOrderCard`.
- Pass `onEditClick = null` to `DeliveryAddressSection` inside `OngoingOrderCard`.

## Verification Plan

### Manual Verification
1.  **Address Editing**:
    *   Navigate to "My Orders".
    *   Verify the "Edit" button next to the delivery address is gone.
2.  **Order Cancellation (Swipe)**:
    *   Place a new order.
    *   In "My Orders", swipe the order card from right to left.
    *   Verify the background turns red with a delete icon.
    *   Release to cancel and verify the order is removed.
    *   Wait until the order is "Preparing" past the grace period (or "Ready").
    *   Verify swiping is no longer possible.
