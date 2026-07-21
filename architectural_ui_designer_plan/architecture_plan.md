# CS426 Midterm Project — Perfect Score Plan
### Coffee Ordering App (Home → Details → Cart → Order → Rewards → Profile)

---

## 0. The Scoring Math (know this before you build anything)

| Component | Max |
|---|---|
| Sum of all rubric line items (Home, Details, Cart, Order Success, My Orders, Rewards, Redeem Rewards, Profile, State/Lifecycle, Persistence) | **~91** |
| **User-Defined Features** (its own line item) | **50** |
| **Total (capped)** | Min(150, sum) ≈ **141** |
| Additional Criteria (Code Quality / Report / Demo) | **-15 to +15** |
| **Final grade** | Min(10, (Total + Additional) / 15) |

**The takeaway:** the fixed checklist only gets you to ~91/141. **User-Defined Features is worth more than every other individual line item combined except the checklist total itself** — it is the single highest-leverage thing you build. You cannot get a perfect score by polishing the required screens alone; you need to also ship genuine extra features. Section 3.3 below is the most important part of this plan.

To hit a 10/10 you need roughly `Total + Additional ≥ 150`. That means: nail essentially all required items (≈91), build enough user-defined features to score close to 50, **and** land a positive Additional Criteria bonus. There's no slack for skipping the extras.

---

## 1. Architecture & Tech Stack

Recommended stack (optimizes for correctness, code-quality points, and dev speed):

- **Language:** Kotlin
- **UI:** Jetpack Compose (LazyColumn satisfies the "ListView/RecyclerView" requirement — say so explicitly in your report so the grader doesn't dock you for not using a literal `RecyclerView`)
- **Architecture:** MVVM — `Screen (Composable) → ViewModel → Repository → Room / DataStore`
- **Navigation:** Navigation-Compose, single `NavHost` in `MainActivity`
- **State:** `StateFlow` in ViewModels, collected via `collectAsStateWithLifecycle()`
- **Persistence:** Room (menu items, cart, orders, rewards) + DataStore Preferences (profile/user prefs)
- **DI:** Hilt (small footprint, but shows up well in "code quality" — optional if time-constrained, manual constructor injection is a fine substitute)
- **Images:** Coil, for coffee product images
- **Testing:** JUnit + Turbine for ViewModel logic (price calc, stamp/points logic) — cheap points toward the code-quality bonus

Package structure to put in the report (shows organization, a code-quality signal):
```
com.example.coffeeapp/
├── data/
│   ├── local/         (Room entities, DAOs, DataStore)
│   ├── repository/    (single source of truth per domain: cart, orders, rewards, profile)
│   └── model/         (domain models)
├── ui/
│   ├── home/
│   ├── details/
│   ├── cart/
│   ├── ordersuccess/
│   ├── myorders/
│   ├── rewards/
│   ├── redeem/
│   ├── profile/
│   └── components/    (shared composables: header, bottom nav, product card)
├── navigation/
└── di/
```

---

## 2. Screen-by-Screen Build Plan (mapped 1:1 to the rubric)

Build in this order — each screen unlocks the data the next one needs.

### Home Screen — 15 pts
- **UI Implementation (1):** basic scaffold with header + content + bottom nav.
- **Header Component (2):** app logo/title + maybe a location or greeting — keep it a reusable `@Composable Header()` used across screens for consistency (also helps code quality).
- **Bottom Navigation Bar (3):** `NavigationBar` with 4–5 destinations (Home, Orders, Rewards, Profile). Must actually navigate and show current-selection state — don't fake it with static icons.
- **Loyalty Card View (3):** a card showing stamp progress (e.g., "5/8 stamps") sourced live from the Rewards repository, not a hardcoded number.
- **Coffee List View (3):** `LazyColumn`/`LazyVerticalGrid` of products from Room, seeded at first launch (see §3.2).
- **Navigation Intent (3):** `onClick` on a list item passes the product ID (not the whole object) to the Details screen via nav args.

### Details Screen — 13 pts
- **Product Customization Interface (3):** shot count stepper, size selector (S/M/L radio or chips), ice level selector. Each control mutates local UI state.
- **Add to Cart Functionality (3):** on press, build a `CartItem` (product + selected customizations + quantity + computed price) and insert into the Room cart table, then navigate to My Cart.
- **Cart Preview (3):** a cart icon with a badge (item count) that opens a bottom sheet/dialog showing current cart contents without leaving Details — implement as a `ModalBottomSheet`.
- **Dynamic Price Calculation (3):** price must recompute **live** as quantity/options change — drive it from a `derivedStateOf`/computed `StateFlow`, never a static string.
- **Back Navigation (1):** `NavController.popBackStack()` or a top-bar back icon.

### My Cart Screen — 15 pts
- **UI Implementation (1):** list layout + total footer + checkout button.
- **Cart Item Rendering (7 — the single biggest line item after State/Lifecycle and Persistence, get this fully right):** each row must correctly reflect that item's specific customizations (size, shots, ice), quantity stepper, and per-item price, all pulled from Room, not recomputed from scratch each time — this is worth more than almost any other single item, budget real time on it.
- **Total Price Display (3):** live sum across all cart rows, recalculates on any change.
- **Gesture-Based Item Removal (3):** `SwipeToDismissBox` (Compose) removes the row from Room and updates the total instantly.
- **Checkout Navigation (1):** "Checkout" clears/commits the cart into an Order record, then navigates to Order Success.

### Order Success Screen — 2 pts
- **Order Success UI (1):** confirmation illustration/message + order ID/summary.
- **Track Order Navigation (1):** button routes to My Orders, ideally scrolled/highlighted to the just-placed order.

### My Orders Screen — 7 pts
- **My Orders UI (1):** two sections/tabs: Ongoing, History.
- **Order History Display (3):** query Room, split by an `orderStatus` enum field.
- **Order Status Transition (3):** swipe or button action flips `orderStatus` from `ONGOING → COMPLETED`, persisted immediately, and **this is the trigger that should increment the loyalty stamp** (tie this directly to Rewards logic — see below).

### Rewards Screen — 12 pts
- **Rewards Screen UI (1):** stamp card grid (8 slots) + points balance.
- **Loyalty Stamp Logic (3):** increment stamp count by exactly 1 per completed order, cap at 8 — do this in the repository layer (single source of truth), not scattered across UI code.
- **Loyalty Card Reset (3):** an explicit user action (tap the full card) resets to 0 once it hits 8 — don't auto-reset silently, the rubric wants an event handler.
- **Points Calculation & Display (3):** e.g. 1 point per $1 spent, computed at order-completion time and stored, then listed per order.
- **Total Points Aggregation (2):** sum of all points across order history, shown prominently.

### Redeem Rewards Screen — 3 pts
- **Points Redemption (3):** user picks a reward-eligible product, confirms, and points are decremented by its point cost — guard against redeeming with insufficient points (disable button + message).

### Profile Screen — 6 pts
- **UI Implementation (3):** avatar, name, email, maybe stats (orders placed, member since).
- **Profile Editing Functionality (3):** edit-mode toggle via icon, form fields become editable, save persists to DataStore/Room and returns to read-only view.

---

## 3. General Application Requirements — 68 pts (the section that decides your grade)

### 3.1 State & Lifecycle Management — 12 pts
- Hoist all durable state into ViewModels (survives `onPause`/`onResume`/`onStop`) — never keep cart/order state in a Composable's local `remember` alone.
- Use `SavedStateHandle` for anything that must survive **process death**, not just rotation (e.g., in-progress customization on the Details screen).
- Test explicitly: rotate the device mid-customization, mid-checkout, and after backgrounding the app for a while (Android Studio → "Don't keep activities" developer option is the fastest way to simulate process death). Document this test in your report — graders reward evidence, not just claims.

### 3.2 Data Persistence & Initialization — 6 pts
- Room database with a `RoomDatabase.Callback.onCreate()` (or a `Migration`/pre-population strategy) to seed the coffee menu on first launch — don't hardcode a `List<Product>` in memory, that won't satisfy "initialization of required application data."
- DataStore Preferences for lightweight profile/user-setting persistence.
- Verify: uninstall/reinstall the app and confirm the menu re-seeds correctly exactly once (no duplicate rows on every launch).

### 3.3 User-Defined Features — 50 pts ⭐ **the priority section**

This is graded holistically ("novel features beyond the scope of this document"), so **quality and completeness of a few features beats a shallow list of many**. Pick 4–6 of the following, implemented to the same polish level as the required screens (persisted, state-safe, reachable through real navigation — not a stub):

| Feature | Why it scores well |
|---|---|
| **Search & filter on Home** (by name, price, category) | Cheap to build, visibly "extra," touches state you already have |
| **Favorites / wishlist** | New Room table + relation, extra screen, natural tie-in to Home & Details |
| **Dark mode / theme toggle** | Persisted in DataStore, demonstrates Compose theming skill |
| **Order status simulation with timed transitions** (e.g., Placed → Preparing → Ready after N seconds, using `WorkManager` or a coroutine timer) | Shows async/background work — a skill not otherwise tested by the rubric |
| **Local push notification when order is "ready"** | Demonstrates a platform API (notifications) outside the base rubric |
| **Promo code field at checkout** | Reuses your price-calculation pipeline, easy to demo |
| **Biometric login / app-lock on Profile** (`BiometricPrompt`) | High-signal "security" feature — leverages authentication concepts, distinct and impressive if you have any device to test on |
| **Encrypted local storage for profile data** (Jetpack DataStore + Tink, or `EncryptedSharedPreferences`) | Same idea — shows you understand data protection beyond the basic Room/DataStore ask |
| **Unit tests for cart/rewards logic** | Doesn't look flashy in a demo but is real engineering value — mention it explicitly in the report since it won't show up on screen |
| **Accessibility pass** (content descriptions, dynamic type scaling, TalkBack sanity check) | Cheap, and reviewers notice its absence more than its presence |

**Plan of record:** pick at minimum (a) search/filter, (b) favorites, (c) order status simulation with notification, and (d) one "polish" item (dark mode or the security pair). Document *every* extra feature explicitly by name in the Development Retrospective — an implemented-but-unmentioned feature is much less likely to be credited than a mediocre one that's clearly called out.

---

## 4. Code Quality Checklist (drives the ±10/±15 Additional Criteria)

- [ ] Consistent naming (`PascalCase` composables, `camelCase` functions/vals, no `data1`/`tmp` variables)
- [ ] No business logic inside Composables — UI reads state, ViewModel computes it
- [ ] No magic numbers (stamp cap `8`, points-per-dollar rate, etc. as named constants)
- [ ] Meaningful KDoc on non-obvious ViewModels/repositories, not on every trivial getter
- [ ] Run the Android Studio linter and fix warnings before submitting
- [ ] No commented-out dead code, no `TODO` left unresolved in submitted code
- [ ] Git history with reasonably descriptive commits (if graded on repo, don't submit a single "final commit" squash)
- [ ] Crash-test the whole flow once end-to-end right before recording the demo — a crash during the video is the worst possible bonus outcome

---

## 5. Report Writing Plan

Follow the template's structure exactly, and don't leave any of its explicit todos undone:

1. **Student Information** — fill in name/ID.
2. **Self-Assessment table** — replace every design mock-up image with your **actual app screenshots**, and score honestly against what you actually shipped (an inflated self-score that doesn't match a working feature reads worse than an accurate one).
3. **Development Retrospective:**
   - *Content of New Features* — name each user-defined feature explicitly (this is where the 50-pt section gets justified in writing).
   - *Techniques Applied* — mention MVVM, Room seeding, StateFlow-driven live pricing, SavedStateHandle for process-death survival, WorkManager/coroutines for order-status simulation, etc. Call out anything that was genuinely hard (e.g., swipe-to-dismiss + Room sync, or biometric integration) — graders like seeing honest difficulty, not just a feature list.
   - *Implementation Experience* — short, genuine reflection; don't pad it.
4. **References** — list every third-party library (Coil, Hilt, Room, WorkManager, Turbine, etc.) and any code snippet adapted from docs/Stack Overflow. Under-disclosure here is a bigger risk than over-disclosure.

---

## 6. Demo Video Checklist

Record only after the crash-test pass above. Walk through, in order:

1. Home → scroll list, use search/filter, show loyalty card
2. Tap a product → Details → change customizations, watch price update live, open cart preview
3. Add to cart → land on My Cart → show item rendering with distinct customizations, swipe to remove one item, show total update
4. Checkout → Order Success → Track Order
5. My Orders → transition an order from Ongoing to History, show the stamp/points update on Rewards afterward
6. Rewards → show stamp progress, reset at 8, points total
7. Redeem Rewards → redeem something, show points decrement
8. Profile → edit mode, save
9. Rotate the device once mid-flow to visibly prove state survival (an easy, high-value 10 seconds of footage for the Lifecycle Management item)
10. Close with a fast montage of the extra user-defined features so they're unambiguously visible and attributable to you

Keep it tight and narrated — a rambling demo undercuts the code-quality/report/demo bonus even if the app itself is solid.

---

## 7. Suggested Build Order (phase-based)

1. **Phase 1 — Data layer:** Room schema (products, cart, orders, rewards, profile) + seeding + DataStore. Get this right first; every screen depends on it.
2. **Phase 2 — Core flow:** Home → Details → Cart → Order Success (the primary user journey, gets you to a demoable app fastest).
3. **Phase 3 — Loop-closing screens:** My Orders → Rewards → Redeem Rewards (this is where stamp/points logic lives, wire it to order completion).
4. **Phase 4 — Profile + lifecycle hardening:** editing flow, then a dedicated pass rotating/backgrounding every screen to confirm state survives.
5. **Phase 5 — User-defined features:** build the 4–6 chosen extras from §3.3 — do not leave this for the last night, it's worth as much as the rest of the checklist combined.
6. **Phase 6 — Polish pass:** lint, remove dead code, add KDoc, write unit tests for price/stamp/points logic.
7. **Phase 7 — Report + demo:** screenshots, self-assessment, retrospective, references, then record the demo video last, after everything is frozen.

---

## 8. Final Pre-Submission Checklist

- [ ] Every rubric row in §2 actually works, not just visually present
- [ ] Rotation/backgrounding tested on every screen, not just one
- [ ] Fresh install re-seeds data correctly, exactly once
- [ ] 4+ user-defined features implemented to full polish and named explicitly in the report
- [ ] Lint clean, no dead code, constants named
- [ ] Report screenshots replace all placeholder images
- [ ] Self-assessment scores match reality
- [ ] References section lists all third-party code/libraries
- [ ] Demo video covers the full flow + rotation proof + feature montage
- [ ] Final gut-check: does `Total + Additional` plausibly clear ~150? If the user-defined feature set feels thin, that's the first place to add more before submitting.
