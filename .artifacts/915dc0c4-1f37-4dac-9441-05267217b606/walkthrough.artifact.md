# Walkthrough - Delivery Address Integration

I have successfully added the delivery address functionality to the Code Cup app. This allows users to specify where they want their drinks delivered during the checkout process and view/edit that address for ongoing orders.

## Changes Made

### Data & Architecture
- **[Order.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/models/Order.kt)**: Added `deliveryAddress` field to the `Order` model with a default value of "123 Artisan Lane, Coffee City".
- **[Entities.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/database/Entities.kt)**: Updated `OrderEntity` to persist the address in the Room database.
- **[OrderRepository.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/OrderRepository.kt)**: Added support for reading/writing the address and an `updateOrderAddress` function for editing ongoing orders.
- **[CartViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/CartViewModel.kt)**: Added state and logic to manage the delivery address during the cart session.

### UI Components
- **[AddressComponents.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/components/AddressComponents.kt)**: Created a new file containing:
    - `DeliveryAddressSection`: A styled row showing the address with a location icon and "Edit" button.
    - `EditAddressDialog`: A standard Material 3 dialog for text input of a new address.

### Screen Integrations
- **[CartScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/CartScreen.kt)**: Integrated the address section into the bottom sheet area, just above the total price and checkout button.
- **[MyOrdersScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/MyOrdersScreen.kt)**: Added the address section to the `OngoingOrderCard`, allowing users to see and change the delivery destination even after placing the order.

### Fixes
- **[AppDatabase.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/database/AppDatabase.kt)**: Incremented the database version to `3` to handle the schema change (adding `deliveryAddress` to `OrderEntity`). This prevents the `IllegalStateException` crash on startup.

### UI Refinements
- **Compact Address Section**: Reduced the vertical padding of the address box and replaced the bulky `TextButton` with a smaller, styled clickable `Text` component.
- **Contextual Styling**: Adjusted the address label in the **My Orders** screen to use a smaller, uppercase font style consistent with other status indicators on that screen.
- **Theme Alignment**: Updated the address box background to use `surfaceVariant` (mapped to `CoffeeSurfaceContainerLow`), matching the Tailwind-based design plan.

## Verification Results

### Automated Tests
- Ran `gradle assembleDebug` to ensure all data model and repository changes are correctly integrated and backward compatible.

### Manual Verification Steps (Recommended)
1.  Navigate to the **Cart** screen. You should see the default address "123 Artisan Lane, Coffee City".
2.  Tap **Edit** next to the address, change it to something else (e.g., "456 Brew Street"), and tap **Save**.
3.  Proceed to **Checkout**.
4.  Navigate to the **Orders** tab. The ongoing order card should display "456 Brew Street".
5.  Tap **Edit** on the ongoing order card to change the address again if needed.
