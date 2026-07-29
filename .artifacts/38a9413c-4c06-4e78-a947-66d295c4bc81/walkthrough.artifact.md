# Walkthrough - Reset Order History

I have implemented the functionality to reset the order database. You can now clear all order history directly from the Profile screen.

## Changes Made

### Database & Repository
- Added `clearAll()` to `OrderDao` to delete all entries from the `orders` table. Due to `CASCADE` constraints, all associated `order_items` are also deleted.
- Added `clearOrderHistory()` to `OrderRepository` to expose this functionality.

### ViewModel & UI
- Added `resetOrderHistory()` to `ProfileViewModel` which triggers the clear action and emits a confirmation snackbar.
- Added a **Danger Zone** section at the bottom of the **Profile Screen**.
- Implemented a confirmation dialog to prevent accidental deletion.

## How to use
1. Open the app and navigate to the **Profile** screen.
2. Scroll to the bottom to find the **Danger Zone**.
3. Tap **Reset Order History**.
4. Confirm the action in the dialog.

> [!WARNING]
> This action is permanent and will remove both ongoing and past orders.
