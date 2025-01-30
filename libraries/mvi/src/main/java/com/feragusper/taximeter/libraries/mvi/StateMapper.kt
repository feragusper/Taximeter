package com.feragusper.taximeter.libraries.mvi

/**
 * Interface responsible for mapping an action (UserIntentAction) to a new UI state (ViewState).
 *
 * @param Action The type representing an action triggered by user input or system events.
 * @param UiState The type representing the current state of the UI.
 */
interface StateMapper<Action : UserIntentAction, UiState : ViewState> {

    /**
     * Processes the given action and generates a new UI state.
     *
     * @param action The action to be processed.
     * @param lastUiState The current state of the UI before processing the action.
     * @return The updated UI state reflecting changes caused by the action.
     */
    operator fun invoke(
        action: Action,
        lastUiState: UiState,
    ): UiState
}
