# UI Design Plan — Coffee Ordering App
### Detailed screen-by-screen specification (includes Splash Screen)

This document defines the visual system and exact UI composition for every screen in the app, so implementation maps directly onto the rubric in `Requirements.md` and the build plan in `plan.md`. Build the shared design system first (§1), then implement screens in the order listed in §2 — every screen below reuses the same header, bottom nav, buttons, and cards, so getting those right once pays off everywhere.

---

## 1. Design System

### 1.1 Brand & Mood
Warm, cafe-like, tactile — not a generic Material demo app. Think "artisan coffee shop app," not "corporate fintech app." Rounded shapes, warm neutrals, one confident accent color, generous whitespace, product photography as the hero visual on every list/card.

### 1.2 Color Palette

| Token | Light mode | Dark mode | Usage |
|---|---|---|---|
| `primary` | `#4A2C1E` (espresso brown) | `#E8C9A8` | headers, primary text on light surfaces, selected nav icon |
| `primary-container` | `#F3E4D7` | `#3A2A20` | loyalty card background, chip backgrounds |
| `accent` | `#C1502E` (terracotta) | `#E2795A` | primary CTA buttons ("Add to Cart," "Checkout," "Redeem") |
| `accent-container` | `#FBE3DA` | `#4A2A1F` | badges, price highlight background |
| `success` | `#4F7942` | `#8FBF7A` | stamp-earned states, order-completed status |
| `error` | `#B3261E` | `#F2B8B5` | remove/destructive actions, validation |
| `background` | `#FFFBF6` | `#1B1512` | screen background |
| `surface` | `#FFFFFF` | `#241C17` | cards, sheets, nav bar |
| `on-surface` | `#2B211B` | `#F1E7DD` | primary text |
| `on-surface-variant` | `#6E5B4E` | `#C9B8AA` | secondary/caption text |
| `outline` | `#E4D6C9` | `#4A3A30` | dividers, input borders |

Dark mode is one of the planned user-defined features (see `plan.md` §3.3) — design every screen with both columns above in mind from the start rather than retrofitting later.

### 1.3 Typography
Two-font system: a rounded humanist display face for headings (e.g. **Poppins** / **Nunito**) + a clean text face for body copy (e.g. **Inter** / **Roboto**).

| Style | Font / Weight | Size / Line height | Usage |
|---|---|---|---|
| Display | Poppins SemiBold | 28sp / 34sp | Splash wordmark |
| Headline | Poppins SemiBold | 22sp / 28sp | Screen titles ("My Cart", "Rewards") |
| Title | Poppins Medium | 17sp / 22sp | Card titles, product names |
| Body | Inter Regular | 15sp / 20sp | Descriptions, list text |
| Label | Inter Medium | 13sp / 16sp | Buttons, chips, tab labels |
| Caption | Inter Regular | 12sp / 16sp | Timestamps, helper text, price sub-labels |
| Price | Poppins SemiBold | 18sp / 22sp | All monetary values — always this style, never body text, so totals are instantly scannable |

### 1.4 Spacing & Grid
4dp base unit. Standard scale: **4 / 8 / 12 / 16 / 24 / 32 / 48**.
- Screen horizontal padding: `16dp`
- Card internal padding: `16dp`
- Gap between list items: `12dp`
- Gap between sections: `24dp`

### 1.5 Shape & Elevation
- Cards / sheets: `12dp` corner radius
- Product images: `16dp` corner radius
- Buttons & chips: fully rounded (`999dp` / pill shape) for primary CTAs; `8dp` radius for secondary/tonal buttons
- Elevation kept mostly flat (elevation 0–1dp) with color-block separation instead of heavy shadows; bottom sheets and the bottom nav bar use elevation `3dp` to lift above content

### 1.6 Iconography
Material Symbols, **Outlined** style at rest, **Filled** style for the active/selected state (nav icons, favorited items). Standard size `24dp`; small inline icons (badges) `16dp`.

### 1.7 Core Reusable Components
Build these once, first, before any screen — every screen spec below references them.

- **`AppHeader`** — 56dp tall, screen title (Title/Headline style) left-aligned or centered depending on screen, optional leading back icon, optional trailing action icon (search, cart, edit).
- **`BottomNavBar`** — 4 destinations: **Home, Orders, Rewards, Profile**. Fixed to bottom, `surface` background, `3dp` elevation, selected icon uses `primary` + filled variant + small label; unselected uses `on-surface-variant` + outlined variant, no label or reduced-opacity label.
- **`PrimaryButton`** — pill shape, `accent` background, white label text, `48dp` height, disabled state = 38% opacity + no ripple.
- **`SecondaryButton`** — outlined, `primary` text/border, transparent fill.
- **`ProductCard`** — image (1:1, `16dp` radius) + product name (Title) + short descriptor (Caption) + price (Price style) + optional small "+" quick-add icon button.
- **`QuantityStepper`** — pill container, `–` / count / `+`, disabled `–` at minimum quantity `1`.
- **`Chip`** (for size/ice-level selection) — selectable, `primary-container` background when selected, `outline` border when not.
- **`Badge`** — small circular count indicator (cart item count, unread stamp count), `accent` background, white text, sits top-right of an icon.
- **`EmptyState`** — centered illustration/icon + Title text + Caption helper text + optional CTA button. Reused for empty cart, no orders yet, no favorites.
- **`LoadingIndicator`** — circular progress, `accent` color, centered.

---

## 2. Screen Inventory & Navigation Map

```
Splash
  └──▶ Home ─────────────┬─▶ Details ──▶ My Cart ──▶ Order Success ──▶ My Orders
       │  (bottom nav)    │      ▲              ▲                          │
       │                  └──────┘ (cart preview,│                        (status
       │                           same screen)  │                        transition
       ├─▶ My Orders  ◀───────────────────────────                        triggers ↓)
       ├─▶ Rewards ───▶ Redeem Rewards                                    Rewards update
       └─▶ Profile
```

- Bottom nav is present on **Home, My Orders, Rewards, Profile** (the four primary destinations).
- **Details, My Cart, Order Success, Redeem Rewards** are pushed on top of the nav stack (no bottom nav visible, back arrow in header instead) since they're part of a linear task flow, not top-level destinations.
- **Splash** is the sole entry point and is never reachable again via back navigation (popped off the back stack once Home loads).

Build order for UI: Splash → Home → Details → My Cart → Order Success → My Orders → Rewards → Redeem Rewards → Profile. This matches the data-dependency order in `plan.md` §7.

---

## 3. Screen Specifications

### 3.0 Splash Screen *(new — not in the original rubric, but required for a polished first-run experience)*

**Purpose:** brand moment + do the invisible work (Room DB seed check/initialization, DataStore read for theme preference) before the user ever sees an empty list.

**Layout (top to bottom, full-bleed, centered vertically):**
1. Background: `background` token, full screen, no header/nav.
2. Centered logo mark (cup icon or wordmark), `96dp`.
3. `12dp` gap, then app name in **Display** style, `primary` color.
4. `4dp` gap, optional one-line tagline in **Caption**, `on-surface-variant` color (e.g. "Your daily ritual, ready in seconds").
5. Bottom-anchored (32dp from bottom edge): small `LoadingIndicator`, only shown if seeding takes >300ms so it doesn't flicker on fast devices.

**Behavior:**
- Implement via the Android 12+ `SplashScreen` API for the system-drawn splash (icon + background color), then hand off to an in-app Compose splash route that performs the actual seed/init work and holds for a **minimum 600ms** even if init finishes instantly — prevents a jarring flash.
- On completion: `navController.navigate(Home) { popUpTo(Splash) { inclusive = true } }` — the user can never navigate back to Splash.
- No user interaction is possible on this screen; if init fails (e.g., DB error), fall back to Home anyway and show a non-blocking snackbar rather than trapping the user here.

**States:** Loading (default) → Error (rare, non-blocking, still proceeds) → Done (auto-navigates).

---

### 3.1 Home Screen — rubric: UI, Header, Bottom Nav, Loyalty Card View, Coffee List View, Navigation Intent

**Layout (top to bottom, scrollable):**
1. `AppHeader`: left — small circular avatar or greeting ("Good morning, {name}"); right — search icon button (opens inline search bar or dedicated search field) and cart icon with `Badge` showing current cart item count.
2. Loyalty Card (`primary-container` background, `12dp` radius, full-width, `16dp` padding): stamp row (8 small cup icons, filled = earned / outlined = not yet), current count label ("5 / 8 stamps"), tapping the card navigates to Rewards.
3. Section label "Menu" (Title style) + optional horizontal category `Chip` row (Espresso, Latte, Cold Brew, Seasonal) for the search/filter feature.
4. Product grid: `LazyVerticalGrid`, 2 columns, `12dp` gutter, each cell a `ProductCard`. Grid scrolls independently under the fixed header; loyalty card and category chips scroll away with it (not pinned) to keep the product list dominant.
5. `BottomNavBar` pinned to bottom, "Home" tab active (filled icon).

**States:**
- **Loading:** skeleton grid (grey rounded rectangles in place of `ProductCard`s) while first Room query resolves.
- **Empty (filtered to zero results):** `EmptyState` — "No drinks match your search" + clear-filter button.
- **Populated (default):** as above.

**Interactions:**
- Tap `ProductCard` (not the quick-add icon) → navigate to Details with the product ID.
- Tap the small "+" quick-add icon on the card → adds default-configuration item straight to cart, brief snackbar "Added to cart · Undo," badge count increments without leaving Home.
- Tap loyalty card → Rewards.
- Type in search / tap category chip → grid filters live, no navigation.

---

### 3.2 Details Screen — rubric: Product Customization, Add to Cart, Cart Preview, Dynamic Price, Back Navigation

**Layout (top to bottom):**
1. `AppHeader`: back arrow (left), cart icon with `Badge` (right) — tapping the cart icon opens the **Cart Preview** bottom sheet without navigating away.
2. Large product image, full width, `4:3`, `16dp` bottom-corner radius only (flush with header above).
3. Product name (Headline) + short description (Body) + base price (Caption, greyed, e.g. "from $4.50").
4. **Customization sections**, each a labeled group of `Chip`s:
   - "Shots" — Single / Double / Triple (single-select chips)
   - "Size" — S / M / L (single-select chips)
   - "Ice Level" — None / Light / Regular / Extra (single-select chips, hidden entirely for hot-only items)
5. `QuantityStepper`, centered or right-aligned under the customization sections.
6. **Sticky bottom bar** (not part of the scroll content): live computed total price (Price style, large) on the left, `PrimaryButton` "Add to Cart — $X.XX" on the right — the button label itself displays the live total, so price feedback is impossible to miss.

**States:**
- **Default:** as above, price recalculates on every chip/stepper change with a brief scale/fade micro-animation on the number to draw the eye.
- **Cart Preview open:** bottom sheet overlays the lower ~50% of the screen — mini list of current cart rows (name + qty + price), running subtotal, "View Full Cart" button, dismiss by swipe-down or scrim tap. Does not navigate; Details screen remains underneath.

**Interactions:**
- Chip tap → updates selection + price instantly.
- Stepper +/– → updates quantity + price instantly, `–` disabled at qty 1.
- "Add to Cart" → inserts row into Room, then **navigates to My Cart** (per rubric — Add to Cart triggers navigation, whereas the cart *icon* only opens the preview sheet; keep these two interactions visually distinct so testers/graders can tell them apart).
- Back arrow → pop back to Home, no side effects, current in-progress customization is discarded (not persisted) unless you choose to draft-save it — document whichever choice you make in the report.

---

### 3.3 My Cart Screen — rubric: UI, Cart Item Rendering, Total Price Display, Gesture Removal, Checkout Navigation

**Layout (top to bottom):**
1. `AppHeader`: back arrow, title "My Cart".
2. Scrollable list of cart rows, each row:
   - Thumbnail image (`48dp`, `8dp` radius) — product name (Title) — customization summary as a single Caption line ("Double shot · Medium · Light ice") — quantity `QuantityStepper` (compact variant) — per-row price (Price style) right-aligned.
   - Full row is swipeable left to reveal an `error`-colored "Remove" affordance and complete the delete on release past a threshold (haptic tick on threshold cross).
3. Divider between rows (`outline` token, `1dp`).
4. **Sticky bottom summary card**: subtotal line, (optional) tax/promo line if implemented, bold **Total** line (Price style, larger weight), then `PrimaryButton` "Checkout" full width.

**States:**
- **Populated:** as above.
- **Empty:** `EmptyState` — cup illustration, "Your cart is empty," `SecondaryButton` "Browse Menu" → Home. Sticky checkout bar is hidden entirely in this state.
- **Row mid-swipe:** row translates left, red delete affordance fades in proportionally to swipe distance.

**Interactions:**
- Stepper change on a row → row price and screen Total both update immediately (this is the 7-point rubric item — make sure both the per-row customization display *and* the live recompute are airtight, this is the highest-weighted single UI element in the whole app).
- Swipe-to-remove → row animates out, Room row deleted, Total recomputes.
- "Checkout" → commits cart to an Order record, clears cart, navigates to Order Success.

---

### 3.4 Order Success Screen — rubric: Order Success UI, Track Order Navigation

**Layout (centered, no bottom nav, no back arrow — this is a terminal confirmation state):**
1. Large success illustration/checkmark icon (`success` color), `96dp`, centered, vertically ~30% down the screen.
2. Headline: "Order placed!"
3. Body: short confirmation copy + order number/estimated ready time if available ("Order #1042 · Ready in ~8 min").
4. `PrimaryButton` "Track My Order" full-width, positioned in the lower third.
5. Small `SecondaryButton` (text-only) "Back to Home" beneath it, for users who don't want to track immediately.

**Interactions:**
- "Track My Order" → navigate to My Orders, with the just-placed order pre-scrolled/highlighted (brief background pulse on that row) in the Ongoing tab.
- Hardware/gesture back from this screen should skip directly to Home (pop the whole checkout stack), not back into the now-empty cart.

---

### 3.5 My Orders Screen — rubric: UI, Order History Display, Order Status Transition

**Layout (top to bottom):**
1. `AppHeader`: title "My Orders", no back arrow (top-level destination).
2. Segmented tab row directly under the header: **Ongoing | History**, `primary` underline/pill on active tab.
3. **Ongoing tab:** list of order cards — order number/date, item summary ("2 items"), status label as a colored `Chip` ("Preparing," "Ready"), and an explicit action ("Mark as Picked Up" `SecondaryButton`) that performs the ongoing→history transition.
4. **History tab:** simpler list rows — date, item summary, total price, no action controls (read-only, this is the archive).
5. `BottomNavBar` pinned, "Orders" tab active.

**States:**
- **Ongoing empty:** `EmptyState` — "No active orders" + "Order Now" button → Home.
- **History empty:** `EmptyState` — "No past orders yet."
- **Populated:** as above; the just-completed order (arriving from Order Success) gets a one-time highlight animation.

**Interactions:**
- Tap tab → switches list content, no navigation.
- "Mark as Picked Up" (or swipe gesture, pick one and be consistent — a button is easier to grade unambiguously than a hidden gesture) → order moves from Ongoing to History, **this transition is what triggers the stamp increment on Rewards** — surface a small toast/snackbar here ("+1 stamp earned!") so the connection between screens is visible to a grader watching the demo, not just happening silently in the data layer.

---

### 3.6 Rewards Screen — rubric: UI, Stamp Logic, Card Reset, Points Calc & Display, Points Aggregation

**Layout (top to bottom):**
1. `AppHeader`: title "Rewards".
2. Large loyalty stamp card (same visual language as the Home mini-card but full-size): 8-slot grid of cup icons, filled = earned, with a subtle "pop" animation on the most-recently-earned slot. If count == 8, card visually pulses/glows and shows "Tap to redeem your free drink!"
3. Points summary card directly below: large **Total Points** number (Price-weight style, but not currency), Caption subtext ("Earn 1 point per $1 spent").
4. "Points History" section: list of past point-earning events, one row per completed order ("Order #1042 · +5 pts"), most recent first.
5. `BottomNavBar` pinned, "Rewards" tab active.

**States:**
- **0 stamps / 0 points (new user):** cards still render with all-empty slots and "0 pts" — never hide these components, an empty state here should still teach the user how the system works via a short Caption ("Complete orders to start earning stamps and points").
- **8/8 stamps:** card enters the "ready to reset" visual state described above.

**Interactions:**
- Tap the stamp card **only when full (8/8)** → confirmation dialog ("Reset your loyalty card and redeem a free drink?") → on confirm, count resets to 0 with a brief celebratory animation. Tapping a non-full card does nothing (or navigates nowhere useful) — don't make it a dead tap target, disable the tap affordance visually until it's full.
- Tap "Points History" row → optional, can deep-link to that order in My Orders History tab.

---

### 3.7 Redeem Rewards Screen — rubric: Points Redemption

**Layout (top to bottom):**
1. `AppHeader`: back arrow, title "Redeem Rewards".
2. Points balance banner directly under the header (`accent-container` background, full width): "You have **{n} points**".
3. Grid or list of redeemable products, each row/card showing: product image, name, point cost (Price-style but labeled "pts" not "$"), and a `PrimaryButton` "Redeem" — disabled (38% opacity, no ripple) if the user's balance is below that item's cost.
4. Tapping "Redeem" on an eligible item opens a confirm dialog ("Redeem {product} for {n} pts?") before committing — protects against accidental taps on an irreversible action.

**States:**
- **Sufficient points:** buttons enabled, normal.
- **Insufficient points (per-item):** that item's button disabled + small Caption under it ("Need {n - balance} more pts").
- **Zero redeemable items available:** `EmptyState` — "No rewards available yet — check back soon."
- **Post-redemption:** snackbar confirmation, balance banner updates instantly, that item's button re-evaluates against the new balance.

**Interactions:**
- Confirm redeem → decrement points in the repository, snackbar "Redeemed! Enjoy your {product}," stays on screen (doesn't force navigation) so a user can redeem multiple things in one visit if they have enough points.

---

### 3.8 Profile Screen — rubric: UI, Profile Editing Functionality

**Layout (top to bottom):**
1. `AppHeader`: title "Profile", trailing edit-mode toggle icon (pencil ↔ checkmark depending on mode).
2. Centered avatar (`80dp` circle, tap-to-change if you implement photo picking as a bonus) + name (Headline) + email (Caption) beneath it, centered.
3. Stats row (read-only, both modes): three small stat blocks — "Orders," "Points," "Member since" — horizontal, evenly spaced.
4. Below the fold, a form section:
   - **Read mode:** each field (Name, Email, Phone) rendered as a simple label/value row, no input chrome.
   - **Edit mode:** the same fields become bordered `TextField`s, plus a `PrimaryButton` "Save Changes" and a `SecondaryButton` "Cancel" pinned at the bottom.
5. `BottomNavBar` pinned, "Profile" tab active (hidden while in edit mode in favor of the Save/Cancel bar, to keep the user focused on finishing the edit).

**States:**
- **Read (default):** as above.
- **Edit:** form fields active, keyboard-aware (screen scrolls/resizes so the focused field and Save button stay visible above the keyboard).
- **Saving:** brief inline `LoadingIndicator` replacing the Save button label, then reverts to Read mode on success.
- **Validation error** (e.g. malformed email): inline error text under the offending field, `error` color, Save button stays enabled but re-validates on tap rather than disabling proactively.

**Interactions:**
- Tap edit icon → toggle to Edit mode, fields become editable, focus first field.
- "Save Changes" → persist to DataStore/Room, toggle back to Read mode, snackbar "Profile updated."
- "Cancel" → discard in-progress edits, revert displayed values to last-saved state, toggle back to Read mode.

---

## 4. Cross-Screen Consistency Rules

- **Every price, anywhere in the app, uses the Price typography style** — a grader should be able to tell at a glance which numbers are money.
- **Every list that can be empty has a designed `EmptyState`** — never ship a screen that just renders blank white space when a Room query returns zero rows.
- **Every destructive action (remove from cart, reset loyalty card, redeem points) has either a swipe-to-confirm gesture or an explicit confirm dialog** — never a single accidental tap.
- **The bottom nav bar's visible/hidden state is consistent**: shown on the four top-level destinations (Home, My Orders, Rewards, Profile), hidden on everything pushed on top of them (Details, My Cart, Order Success, Redeem Rewards), which instead show a back arrow in the header.
- **Loading states use the same `LoadingIndicator` everywhere** — don't invent a second spinner style.

---

## 5. Accessibility Notes

- Minimum tappable target size `48dp × 48dp`, even for small icons like the cart badge or quantity stepper buttons.
- All icon-only buttons (search, cart, back, edit) carry a `contentDescription` — write these out explicitly in code, don't leave them null.
- Color is never the *only* signal for state — the "Ready" order status uses both a color chip **and** a text label; the full loyalty card uses both a glow **and** the "Tap to redeem" text.
- Support system font-scaling: verify at 130% text size that no screen clips or overlaps (test Details and My Cart specifically, they have the densest layouts).
- Verify with TalkBack that navigation order through each screen is logical (header → primary content → bottom nav), especially on Details where customization chips and the sticky price bar could otherwise get read out of order.

---

## 6. Motion & Micro-interactions (keep subtle, not decorative)

- Screen transitions: standard slide-in-from-right / slide-out-to-left for pushed screens (Details, Cart, Redeem); simple fade for bottom-nav tab switches (they're peers, not a stack).
- Price changes on Details/Cart: brief scale-pulse (~150ms) on the number itself when it updates, not the whole layout.
- Stamp card: newly-earned stamp icon animates in with a small pop/scale rather than just appearing.
- Swipe-to-remove (Cart): row slides out and collapses its height smoothly rather than snapping, so the list doesn't jump.
- Keep every animation under ~250ms — this is a quick-service coffee app, motion should feel snappy, not showy.
