# "Ask the Barista" Chat Feature Walkthrough

I have implemented a conversational UI where you can interact with a virtual barista to get coffee recommendations and advice.

## Changes Made

### 1. Conversational UI
- **Redesigned Barista Screen**: Transformed the static screen into a modern chat interface.
- **Message Bubbles**:
    - **Barista**: Styled with a clean white background and a coffee icon avatar.
    - **User**: Styled with a distinctive reddish background (`#C1502E`) and aligned to the right.
- **Product Recommendations**: The barista can now send inline "Product Cards" for recommended drinks. These cards are clickable and take you directly to the drink's details.
- **Typing Indicator**: Added a "Barista is typing..." indicator to make the conversation feel alive.

### 2. Barista Simulation (ViewModel)
- **Smart Responses**: The `BaristaViewModel` analyzes your messages for keywords like "strong", "hot", "refreshing", or "dairy" and provides tailored advice.
- **Automated Recommendations**: When suggesting a drink, the barista automatically attaches the corresponding product card to the message.
- **Suggestion Chips**: Added quick-reply buttons (e.g., "Recommend a drink", "Low caffeine") to help users start the conversation easily.

### 3. Navigation & Architecture
- **Transactional Flow**: The screen now features a back button instead of the drawer menu, focusing the user's attention on the conversation.
- **Data Model**: Created a `ChatMessage` data class to handle the message history and attached products.
- **Dependency Injection**: Updated `ViewModelFactory` to manage the new `BaristaViewModel`.

## Verification Results

### Automated Tests
- `gradle assembleDebug` passed successfully.
- Verified state management for chat history and typing simulation.

### Manual Verification Steps
1. Open the sidebar and click **"Ask the Barista"**.
2. Type "I need something refreshing" and click send.
3. Observe the user bubble appearing on the right.
4. Watch the "typing" indicator on the left.
5. Review the barista's response and the attached **Nitro Cold Brew** card.
6. Click the suggestion chip **"Low caffeine"** and verify the response.
7. Click the back arrow to return to the previous screen.
