# Profile Sync in Sidebar Walkthrough

I have synchronized the Sidebar (App Drawer) header with the user's profile data. Now, any changes to the name or avatar in the Profile screen will be immediately reflected in the sidebar.

## Changes Made

### 1. ViewModel Updates
- **MainViewModel**: Added `ProfileRepository` as a dependency to observe the live `userProfile` state.
- **ViewModelFactory**: Updated to inject the singleton `ProfileRepository` into `MainViewModel`.

### 2. Sidebar (AppDrawer) Enhancements
- **Dynamic Header**: The `DrawerHeader` now accepts a `UserProfile` object.
- **Avatar Integration**: Replaced the static placeholder with `AsyncImage` from Coil to load the user's actual `avatarUrl`.
- **Data Binding**:
    - The **User Name** is now bound to `user.name`.
    - The **Points** value is now bound to `user.points` (e.g., "1240 pts" instead of the hardcoded "850 pts").
- **Reactive UI**: Used `collectAsState()` in `AppDrawer` so the UI automatically updates whenever the profile changes in the repository.

## Verification Results

### Automated Tests
- Build successful.

### Manual Verification
- Verified that `MainViewModel` correctly fetches the initial state from `ProfileRepository`.
- Verified that `AppDrawer` UI elements are properly bound to the `UserProfile` properties.
