# Implementation Plan - Refine Delivery Address UI

The user wants to improve the UI of the delivery address section to be more compact and aligned with the provided HTML design plan.

## User Review Required

> [!NOTE]
> - I will reduce the vertical padding and replace the `TextButton` with a smaller clickable `Text` to minimize the box height.
> - I will use the `surfaceVariant` color (which maps to `CoffeeSurfaceContainerLow`) to match the intended background.
> - I will adjust the "Delivery Address" label styling for the My Orders screen to match its specific context.

## Proposed Changes

### UI Components

#### [MODIFY] [AddressComponents.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/components/AddressComponents.kt)
- Update `DeliveryAddressSection` to accept a `titleStyle` and `titleColor` for flexibility.
- Reduce vertical padding from `12.dp` to `8.dp`.
- Replace `Icon` and `Text` sizes to be slightly more compact.
- Replace `TextButton` with a `Text` component using `Modifier.clickable`.
- Use `MaterialTheme.colorScheme.surfaceVariant` for the background.

### Screens

#### [MODIFY] [MyOrdersScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/MyOrdersScreen.kt)
- Pass a specific label style to `DeliveryAddressSection` to match the "My Orders" design (uppercase, smaller font).

## Verification Plan

### Manual Verification
1.  **Cart Screen:** Verify the address section looks compact and matches the checkout area's aesthetic.
2.  **My Orders Screen:** Verify the address section uses a smaller label style consistent with the "Preparing" status label.
3.  **Interaction:** Ensure the "Edit" clickable text still opens the correct dialog on both screens.
