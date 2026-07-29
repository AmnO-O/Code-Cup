# Implementation Plan - Reset Database of Orders

This plan outlines the steps to add functionality to clear the order history from the application's database.

## User Review Required

> [!IMPORTANT]
> This action will permanently delete all order history and cannot be undone.

## Proposed Changes

### Database Layer

#### [MODIFY] [Daos.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/database/Daos.kt)
- Add `clearAll()` method to `OrderDao` using `@Query("DELETE FROM orders")`.
- Note: `order_items` will be automatically cleared due to the `CASCADE` delete foreign key.

### Data Layer

#### [MODIFY] [OrderRepository.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/OrderRepository.kt)
- Add `clearOrders()` method to call the DAO's `clearAll()`.

### ViewModel Layer

#### [MODIFY] [MyOrdersViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/MyOrdersViewModel.kt)
- Add `clearOrderHistory()` method to trigger the repository's clear function.
- Emit a snackbar event once the history is cleared.

### UI Layer

#### [MODIFY] [MyOrdersScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/MyOrdersScreen.kt)
- Add a "Clear History" button at the top of the `OrdersHistoryList` in the History tab.
- Show a confirmation dialog before performing the deletion.

## Verification Plan

### Manual Verification
- Place a few orders to populate the history.
- Navigate to the "My Orders" screen and select the "History" tab.
- Click the "Clear History" button.
- Confirm the deletion.
- Verify that the history list is now empty and the "No past orders yet" empty state is shown.
