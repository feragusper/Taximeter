package com.feragusper.taximeter.libraries.mvi

import kotlinx.coroutines.flow.Flow

/**
 * Interface responsible for processing user intents and emitting corresponding actions.
 *
 * @param Intent The type representing user intent.
 * @param Action The type representing the resulting action.
 */
interface IntentProcessor<Intent : UserIntent, Action : UserIntentAction> {

    /**
     * Processes the given user intent and returns a flow of actions to be handled.
     *
     * @param intent The user intent to be processed.
     * @return A flow of actions derived from the intent.
     */
    suspend operator fun invoke(intent: Intent): Flow<Action>
}
