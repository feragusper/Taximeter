package com.feragusper.taximeter.features.taximeter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feragusper.taximeter.libraries.mvi.MviContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * ViewModel for managing the state and user intents for the Taximeter feature.
 * This ViewModel follows the MVI (Model-View-Intent) architecture pattern to ensure
 * unidirectional data flow and state management.
 */
@HiltViewModel
internal class TaximeterViewModel @Inject constructor(
    intentProcessor: TaximeterIntentProcessor,  // Handles processing of user intents
    stateReducer: TaximeterStateMapper,  // Maps actions to view state updates
    effectProducer: TaximeterEffectProducer  // Produces side effects based on actions
) : ViewModel() {

    // Container that manages state, intent processing, and effect production
    private val mviContainer = MviContainer(
        coroutineScope = viewModelScope,  // Scoped to the ViewModel's lifecycle
        intentProcessor = intentProcessor,
        stateMapper = stateReducer,
        effectProducer = effectProducer,
        initialState = TaximeterViewState()  // Default initial state
    )

    /**
     * Exposes the current state of the taximeter screen.
     * Observed by the UI to reflect changes in the taximeter feature.
     */
    val viewState: StateFlow<TaximeterViewState> = mviContainer.state

    /**
     * Exposes side effects such as showing errors or navigation triggers.
     * Can be collected by the UI to handle one-time events.
     */
    val sideEffect: Flow<TaximeterSideEffect> = mviContainer.sideEffect

    /**
     * Handles user intents and processes them through the MVI container.
     *
     * @param userIntent The user action that needs to be processed.
     */
    fun onUserIntent(userIntent: TaximeterIntent) {
        mviContainer.onUserIntent(userIntent)
    }
}