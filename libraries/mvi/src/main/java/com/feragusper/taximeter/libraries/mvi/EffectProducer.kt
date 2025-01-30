package com.feragusper.taximeter.libraries.mvi

/**
 * Interface responsible for producing side effects based on the given action and current UI state.
 *
 * @param Action The type of user intent action that triggers side effects.
 * @param UiState The type of view state representing the UI's state.
 * @param Effect The type of side effect that might be produced.
 */
interface EffectProducer<Action : UserIntentAction, UiState : ViewState, Effect : SideEffect> {

    /**
     * Evaluates an action and the current UI state to determine if a side effect should be triggered.
     *
     * @param action The user action that triggered the evaluation.
     * @param uiState The current state of the UI.
     * @return The corresponding side effect if applicable, otherwise null.
     */
    operator fun invoke(
        action: Action,
        uiState: UiState,
    ): Effect?
}
