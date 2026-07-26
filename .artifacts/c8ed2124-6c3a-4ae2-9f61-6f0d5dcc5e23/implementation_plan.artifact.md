# Implement "Ask the Barista" Chat Feature

Implement a conversational UI where users can ask for drink recommendations and get advice from a virtual barista, based on the provided HTML design.

## User Review Required

> [!NOTE]
> The chat will be a simulated experience with mock responses. In the future, this could be connected to an AI backend.

> [!IMPORTANT]
> The screen will transition from a drawer-based navigation to a transactional flow with a back button, as seen in the HTML design.

## Proposed Changes

### [Component] Data Models

#### [NEW] [ChatMessage.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/models/ChatMessage.kt)
- Define `ChatMessage` data class with fields for text, sender (User vs. Barista), timestamp, and optional `Product` recommendation.

### [Component] ViewModels

#### [NEW] [BaristaViewModel.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/BaristaViewModel.kt)
- Manage a list of `ChatMessage` objects.
- Implement logic to send a user message and trigger a delayed mock barista response.
- Provide pre-defined recommendation logic based on keywords (e.g., "hot", "refreshing", "caffeine").

#### [MODIFY] [ViewModelFactory.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/viewmodels/ViewModelFactory.kt)
- Add support for `BaristaViewModel`.

### [Component] UI Screens

#### [MODIFY] [BaristaScreen.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/screens/BaristaScreen.kt)
- Redesign the UI to be a chat interface:
    - **Header**: Back button and "Ask the Barista" title.
    - **Chat History**: A scrolling list of message bubbles.
    - **Barista Bubble**: Creamy background, coffee icon avatar.
    - **User Bubble**: Primary/Reddish background, aligned to the right.
    - **Product Card**: Inline card for recommended drinks.
    - **Footer**: Suggestion chips and text input field with send button.

#### [MODIFY] [NavGraph.kt](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/app/src/main/java/com/example/codecup/ui/navigation/NavGraph.kt)
- Update `BaristaScreen` call to pass `onBackClick`.

## Verification Plan

### Automated Tests
- Build the project to ensure all new components are correctly integrated.

### Manual Verification
- Navigate to "Ask the Barista" from the sidebar.
- Type a message and verify it appears on the right.
- Wait for the barista's response to appear on the left.
- Click a suggestion chip and verify it sends the message.
- Verify the back button returns to the previous screen.
