package com.feragusper.taximeter.libraries.mvi

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * A container that manages the MVI (Model-View-Intent) cycle.
 *
 * @param coroutineScope The scope in which the container operates.
 * @param intentProcessor Processes incoming user intents into actions.
 * @param stateMapper Maps actions to new UI states.
 * @param effectProducer Produces side effects based on actions and UI state.
 * @param processDispatcher The dispatcher for processing intents (default to IO).
 * @param initialState The initial state of the UI.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("LongParameterList")
class MviContainer<UiState : ViewState, Effect : SideEffect, Intent : UserIntent, Action : UserIntentAction>(
    private val coroutineScope: CoroutineScope,
    private val intentProcessor: IntentProcessor<Intent, Action>,
    private val stateMapper: StateMapper<Action, UiState>,
    private val effectProducer: EffectProducer<Action, UiState, Effect>,
    processDispatcher: CoroutineDispatcher = Dispatchers.IO,
    initialState: UiState,
) {
    // Shared flow to handle incoming intents
    private val intents = MutableSharedFlow<Intent>()

    // StateFlow to hold the current UI state
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<UiState> = _state

    // Channel to emit side effects
    private val _sideEffect = Channel<Effect>(Channel.BUFFERED)
    val sideEffect: Flow<Effect> = _sideEffect.receiveAsFlow()

    init {
        // Launch processing pipeline for intents
        intents
            .flatMapMerge { intent ->
                intentProcessor(intent)
                    .catch { throwable ->
                        handleException(throwable)
                    }
            }
            .map { intentAction ->
                val currentUiState = _state.value
                val nextUiState = stateMapper(intentAction, currentUiState)
                val nextSideEffect = effectProducer(intentAction, currentUiState)
                nextUiState to nextSideEffect
            }
            .onEach { (newUiState, newSideEffect) ->
                _state.value = newUiState
                newSideEffect?.let { _sideEffect.send(it) }
            }
            .flowOn(processDispatcher)
            .launchIn(coroutineScope)
    }

    /**
     * Handles incoming user intents and processes them.
     *
     * @param intent The user intent to be processed.
     */
    fun onUserIntent(intent: Intent) {
        coroutineScope.launch {
            try {
                intents.emit(intent)
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    /**
     * Handles exceptions that may occur during intent processing.
     *
     * @param throwable The exception that occurred.
     */
    private fun handleException(throwable: Throwable) {
        println("Error processing intent: ${throwable.message}")
    }
}
