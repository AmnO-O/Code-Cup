# Implementation Plan - Cart Grouping & Order Success Celebration

The goal is to improve the cart logic by grouping identical items and adding a celebratory "Confetti" effect to the order success screen.

## User Review Required

> [!TIP]
> I will be implementing the celebration effect using a custom Jetpack Compose `Confetti` component. This avoids adding new large dependencies like Lottie and keeps the app lightweight.

## Proposed Changes

### [Data Layer]

#### [MODIFY] [CartRepository](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/CartRepository.kt)
- Update `addToCart(item: CartItem)` logic:
    - Search for an existing item in the cart that matches the `product.id`, `size`, `shots`, and `iceLevel`.
    - If a match is found, update its `quantity` and `totalPrice` instead of adding a new row.
    - If no match is found, add it as a new item.

### [UI Components]

#### [NEW] [ConfettiEffect](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/components/ConfettiEffect.kt)
Create a new reusable Composable that uses `Canvas` and `Animatable` to create a colorful burst of confetti when displayed.

### [Screens]

#### [MODIFY] [OrderSuccessScreen](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/OrderSuccessScreen.kt)
- Integrate the `ConfettiEffect` at the top of the `Box` container so it overlays the success message beautifully when the screen opens.

## Verification Plan

### Manual Verification
- **Cart Grouping:**
    1. Go to Home.
    2. Click the "+" button on "Artisan Cappuccino" 3 times.
    3. Navigate to the Cart.
    4. Verify that there is only **one** entry for "Artisan Cappuccino" with a quantity of 3.
    5. Go to Product Details, pick a different size, and add to cart. Verify it appears as a separate entry.
- **Success Celebration:**
    1. Add items to cart and click "Checkout".
    2. Verify that a colorful confetti animation triggers upon entering the `OrderSuccessScreen`.
