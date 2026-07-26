# Sync Profile Data in Sidebar

Synchronize the avatar image, user name, and points in the Sidebar (App Drawer) header with the actual user profile data, so changes made in the Profile screen are immediately reflected in the Sidebar.

## Proposed Changes

### [Component] ViewModels

#### [MODIFY] [MainViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/MainViewModel.kt)
- Add `profileRepository` as a dependency.
- Expose `userProfile: StateFlow<UserProfile>` from the repository.

#### [MODIFY] [ViewModelFactory.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/ViewModelFactory.kt)
- Pass `profileRepository` to `MainViewModel` constructor.

### [Component] UI Components

#### [MODIFY] [AppDrawer.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/components/AppDrawer.kt)
- Update `AppDrawer` to collect `userProfile` from `MainViewModel`.
- Update `DrawerHeader` to take `UserProfile` as a parameter.
- Use `AsyncImage` from Coil to load the avatar URL instead of a placeholder.
- Bind the user's name and points to the UI elements.

## Verification Plan

### Automated Tests
- Ensure the project builds successfully.

### Manual Verification
1. Open the app and verify the Sidebar shows the initial profile data (Alex Johnson, 1240 pts).
2. Navigate to the **Profile** screen.
3. Edit the user name (e.g., change to "Alex Smith") and save.
4. Open the Sidebar again and verify the name has updated to "Alex Smith".
5. Verify the avatar matches the one shown in the Profile screen.
