# Implementation Plan - Order Persistence and UI Redesign

This plan addresses the issue where the "Track My Order" functionality doesn't show the real order information and redesigns the `OrderSuccessScreen` and `MyOrdersScreen` based on the provided architectural designs.

## User Review Required

> [!IMPORTANT]
> I will be introducing a new `OrderRepository` to persist orders in memory during the app session. This will allow the "My Orders" screen to show real data instead of sample placeholders.

## Proposed Changes

### [Data Layer]

#### [MODIFY] [Order.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/models/Order.kt)
- Update the `Order` model to include a list of `CartItem`s and a more descriptive `OrderStatus`.

#### [NEW] [OrderRepository.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/OrderRepository.kt)
- Create a repository to manage orders, providing methods to place an order and update its status.

### [Logic / ViewModels]

#### [MODIFY] [CartViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/CartViewModel.kt)
- Add `OrderRepository` as a dependency.
- Implement a `checkout()` function that creates an `Order`, saves it to the repository, and clears the cart.

#### [NEW] [MyOrdersViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/MyOrdersViewModel.kt)
- Create a ViewModel to observe orders from `OrderRepository` and handle status updates (e.g., "Mark as Picked Up").

#### [MODIFY] [ViewModelFactory.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/ViewModelFactory.kt)
- Update to provide `OrderRepository` to `CartViewModel` and `MyOrdersViewModel`.

### [UI Layer]

#### [MODIFY] [OrderSuccessScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/OrderSuccessScreen.kt)
- Redesign based on `order_success/code.html`.
- Display the real order ID of the most recently placed order.

#### [MODIFY] [MyOrdersScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/MyOrdersScreen.kt)
- Redesign based on `my_orders/code.html`.
- Use `MyOrdersViewModel` to display real orders from the repository.
- Implement the "Ongoing" and "History" tabs.
- Implement the "Mark as Picked Up" functionality.

#### [MODIFY] [CartScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/CartScreen.kt)
- Update "Checkout" button to call `viewModel.checkout()` before navigating.

## Verification Plan

### Manual Verification
1.  Add items to the cart.
2.  Proceed to checkout.
3.  On `OrderSuccessScreen`, verify the order ID matches the expected format and click "Track My Order".
4.  On `MyOrdersScreen`, verify the new order appears in the "Ongoing" tab with correct items and total price.
5.  Click "Mark as Picked Up" and verify the order moves to the "History" tab.
