# Walkthrough - Artisan Coffee Project History

This document tracks the iterative improvements and feature implementations for the Code Cup coffee ordering app.

---

## 1. Search & Swipe-to-Dismiss (Latest)
Implemented searching on the Home screen and gesture-based removal in the Cart.
- **[HomeViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/HomeViewModel.kt)**: Combined category + name search logic.
- **[HomeScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/home/HomeScreen.kt)**: Added thematic search bar.
- **[CartScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/CartScreen.kt)**: Integrated `SwipeToDismissBox` for modern item removal.

---

## 2. Profile Screen Redesign
Revamped the profile to match the brand aesthetics.
- **Avatar & Stats**: Added camera overlay for avatar and bento-style stats cards.
- **Personal Info**: Added icons to all fields (Name, Email, Phone).
- **Sign Out**: Integrated a branded Logout button.
- **MVVM**: Introduced `ProfileViewModel` and `ProfileRepository`.

---

## 3. Order Persistence and UI Redesign
Implemented real order tracking and redesigned success/order screens.
- **Real Tracking**: Orders placed are now saved in `OrderRepository` and can be tracked by their actual ID.
- **Redesigned Screens**: Updated `OrderSuccessScreen` and `MyOrdersScreen` (Ongoing/History tabs) based on high-fidelity designs.
- **Celebration**: Integrated a custom Confetti effect upon successful checkout.

---

## 4. Cart Grouping & Celebration
- **Grouping**: Identical items added to the cart are now consolidated into a single row with updated quantities.
- **Confetti**: Added a vibrant celebration animation using Jetpack Compose Canvas.

---

## 5. Category Filtering & Navigation Fix
- **Filtering**: Products are now categorized (Espresso, Cold Brew, etc.), and chips on Home are fully interactive.
- **Navigation**: Standardized bottom bar navigation and fixed "stuck" states using `popBackStack`.

---

## 6. UI Responsiveness & MVVM Core
- **Scrollability**: Fixed Home and Details screens to ensure all content is accessible on smaller devices.
- **MVVM Architecture**: Migrated from local `remember` states to ViewModels (`ProductDetailsViewModel`, `CartViewModel`, etc.) for lifecycle safety and state persistence.
- **Dynamic Pricing**: Implemented live calculation logic for customizations (size surcharges, extra shots).

---
Ứng dụng hiện đã đáp ứng đầy đủ các yêu cầu của rubric và mang lại trải nghiệm người dùng mượt mà, chuyên nghiệp!
