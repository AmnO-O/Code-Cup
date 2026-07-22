# Implementation Plan - Rewards Logic & Room/DataStore Persistence

This plan implements the mandatory rubric requirements for data persistence and the core business logic for the Rewards system.

## User Review Required

> [!IMPORTANT]
> I will be refactoring the entire data layer. This involves moving from in-memory `StateFlow` to a local Room Database and Jetpack DataStore. The current sample data will be used to seed the database on the first launch.

> [!WARNING]
> This change will require a Gradle Sync and potentially a clean build.

## Proposed Changes

### [Dependencies]
Add Room and DataStore libraries to `libs.versions.toml` and `app/build.gradle.kts`.

### [Data Layer - Room]

#### [NEW] [CoffeeDatabase](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/local/CoffeeDatabase.kt)
- Define the Room Database class.
- Implement a `RoomDatabase.Callback` to seed the `products` table with `sampleProducts` on creation.

#### [NEW] [Entities & DAOs]
- **Products:** `ProductEntity` and `ProductDao`.
- **Cart:** `CartItemEntity` and `CartDao`.
- **Orders:** `OrderEntity`, `OrderItemEntity` (to handle the 1-to-many relationship), and `OrderDao`.
- **Rewards:** `RewardsEntity` (total points/stamps) and `RewardsHistoryEntity`.

### [Data Layer - DataStore]

#### [NEW] [ProfileDataStore](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/local/ProfileDataStore.kt)
- Implement Jetpack DataStore to persist user profile details (Name, Email, Phone, Avatar URL).

### [Repositories - Refactoring]

#### [MODIFY] [ProductRepository](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/ProductRepository.kt)
- Fetch products from `ProductDao`.

#### [MODIFY] [CartRepository](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/CartRepository.kt)
- Store and manage cart items in `CartDao`.

#### [MODIFY] [OrderRepository](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/OrderRepository.kt)
- Persist orders and their items using `OrderDao`.
- **Rewards Integration:** After a successful `placeOrder`, trigger point and stamp increment logic.

#### [NEW] [RewardsRepository](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/RewardsRepository.kt)
- Manage total points and stamps.
- Logic: **+1 stamp per order** (max 8), **+1 point per $1 spent**.
- Logic: **Redeem** (deduct points or reset stamps).

#### [MODIFY] [ProfileRepository](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/ProfileRepository.kt)
- Backed by `ProfileDataStore`.

### [UI Layer]

#### [MODIFY] [RewardsScreen](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/RewardsScreen.kt)
- Observe real data from `RewardsRepository`.
- Implement the "Tap full card to reset" requirement from the rubric.

## Verification Plan

### Automated Tests
- Create a simple unit test for the `RewardsRepository` logic (Price to Points calculation, Stamp capping).

### Manual Verification
1. **Initial Persistence:** Install the app, verify products appear (seeded from Room).
2. **Order & Rewards:**
    - Place an order for $12.50.
    - Navigate to Rewards. Verify stamps increased by 1 and points increased by 13 (rounded).
    - Repeat until 8 stamps. Verify the card becomes "ready to reset".
3. **App Restart:** Kill the app and reopen. Verify the cart, orders, profile, and rewards data are all preserved.
