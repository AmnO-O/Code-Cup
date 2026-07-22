# Implementation Plan - Profile Screen Redesign

Redesign the Profile screen to match the artisan coffee theme, adding icons to information fields, a sign-out button, and an avatar editing trigger.

## User Review Required

> [!NOTE]
> I will be introducing a `ProfileViewModel` and `ProfileRepository` to manage user data reactively, following the MVVM pattern established in previous tasks.

## Proposed Changes

### [Data Layer]

#### [NEW] [ProfileRepository](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/data/ProfileRepository.kt)
Create a repository to manage user profile data (name, email, phone, avatar, stats). It will be an in-memory singleton for this session.

### [Logic / ViewModels]

#### [NEW] [ProfileViewModel](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/ProfileViewModel.kt)
Create a ViewModel to observe profile data and handle updates (saving changes, changing avatar, signing out).

#### [MODIFY] [ViewModelFactory](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/ViewModelFactory.kt)
Update the factory to provide the `ProfileRepository` to the `ProfileViewModel`.

### [UI Layer]

#### [MODIFY] [ProfileScreen](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/ProfileScreen.kt)
- **Avatar:** Replace the Icon with an `AsyncImage`. Add a circular border and a floating camera icon button on the bottom-right for editing.
- **Stats:** Redesign into card-style "Bento" items with icons (`ReceiptLong`, `Stars`, `CalendarMonth`).
- **Personal Information Card:**
    - Wrap fields in a stylized card with a header.
    - Add leading icons to each field (`Person`, `Email`, `Phone`).
    - Update both read-only and edit modes to include these icons.
- **Sign Out:** Add a "Sign Out" button at the bottom of the information card with a `Logout` icon.

## Verification Plan

### Manual Verification
1.  Navigate to the Profile screen.
2.  Verify the avatar has a "camera" icon overlay.
3.  Verify stats (Orders, Points, Joined) have icons and a card-like appearance.
4.  Verify the "Personal Information" section has icons next to the Name, Email, and Phone.
5.  Click "Edit" (pencil icon), modify fields, and "Save". Verify icons are still present in edit mode.
6.  Verify the "Sign Out" button is visible and styled correctly.
