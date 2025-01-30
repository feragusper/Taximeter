package com.feragusper.taximeter.libraries.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Unit tests for the [MviContainer] ensuring the MVI cycle operates correctly.
 */
@ExtendWith(CoroutineTestHandler::class)
class MviContainerTest : CoroutineTestConfig {

    override lateinit var testCoroutineScope: kotlinx.coroutines.test.TestScope
    override lateinit var testCoroutineDispatcher: kotlinx.coroutines.test.TestDispatcher

    private val initialViewState = SampleViewState.create()
    private val testIntentProcessor = SampleIntentProcessor()
    private val testStateMapper = SampleStateMapper(initialViewState)
    private val testEffectProducer = SampleEffectProducer()

    private lateinit var mviContainer: MviContainer<SampleViewState, SampleSideEffect, SampleUserIntent, SampleUserIntentAction>

    @BeforeEach
    fun setUp() {
        // Initialize the MVI container with test implementations
        mviContainer = MviContainer(
            testCoroutineScope,
            testIntentProcessor,
            testStateMapper,
            testEffectProducer,
            testCoroutineDispatcher,
            initialViewState,
        )
    }

    @Test
    fun `should emit user intent correctly`() = runTest {
        // Given
        val intent = SampleUserIntent()
        val expectedState = SampleViewState.create()
        testStateMapper.setup(expectedState)

        // When
        mviContainer.onUserIntent(intent)

        // Then
        assertTrue { mviContainer.state.value.ordinal == expectedState.ordinal }
        testIntentProcessor.verifyIntentReceived(intent)
    }

    @Test
    fun `should process new action through state mapper`() = runTest {
        // Given
        val action = SampleUserIntentAction()
        testIntentProcessor.setup(flowOf(action))

        // When
        mviContainer.onUserIntent(SampleUserIntent())

        // Then
        testStateMapper.verifyStateMapping(initialViewState, action)
    }

    @Test
    fun `should pass new action to effect producer`() = runTest {
        // Given
        val action = SampleUserIntentAction()
        testIntentProcessor.setup(flowOf(action))

        // When
        mviContainer.onUserIntent(SampleUserIntent())

        // Then
        testEffectProducer.verifyEffectProduced(initialViewState, action)
    }

    @Test
    fun `should update state properly`() = runTest {
        // Given
        val action = SampleUserIntentAction()
        val expectedState = SampleViewState.create()
        testIntentProcessor.setup(flowOf(action))
        testStateMapper.setup(expectedState)

        // When
        mviContainer.onUserIntent(SampleUserIntent())

        // Then
        assertTrue { mviContainer.state.value.ordinal == expectedState.ordinal }
    }

    @Test
    fun `should emit side effects properly`() = runTest {
        // Given
        val action = SampleUserIntentAction()
        val expectedSideEffect = SampleSideEffect()
        testIntentProcessor.setup(flowOf(action))
        testEffectProducer.setup(expectedSideEffect)

        // When
        mviContainer.onUserIntent(SampleUserIntent())

        // Then
        assertNotNull(mviContainer.sideEffect.first())
    }

    // Sample User Intent
    private class SampleUserIntent : UserIntent

    // Sample User Intent Action
    private class SampleUserIntentAction : UserIntentAction

    // Sample View State
    private data class SampleViewState(val ordinal: Int) : ViewState {
        companion object {
            private var counter = 0
            fun create() = SampleViewState(counter++)
        }
    }

    // Sample Side Effect
    private class SampleSideEffect : SideEffect

    // Sample Intent Processor
    private class SampleIntentProcessor :
        IntentProcessor<SampleUserIntent, SampleUserIntentAction> {
        private var receivedIntent: SampleUserIntent? = null
        private var responseFlow: Flow<SampleUserIntentAction> = flowOf(SampleUserIntentAction())

        override suspend fun invoke(intent: SampleUserIntent): Flow<SampleUserIntentAction> {
            receivedIntent = intent
            return responseFlow
        }

        fun setup(responseFlow: Flow<SampleUserIntentAction>) {
            this.responseFlow = responseFlow
        }

        fun verifyIntentReceived(intent: SampleUserIntent) {
            assertTrue { receivedIntent == intent }
        }
    }

    // Sample State Mapper
    private class SampleStateMapper(initialState: SampleViewState) :
        StateMapper<SampleUserIntentAction, SampleViewState> {
        private var receivedUiState: SampleViewState? = null
        private var receivedAction: SampleUserIntentAction? = null
        private var newState: SampleViewState = initialState

        override fun invoke(
            action: SampleUserIntentAction,
            lastUiState: SampleViewState
        ): SampleViewState {
            receivedUiState = lastUiState
            receivedAction = action
            return newState
        }

        fun setup(newState: SampleViewState) {
            this.newState = newState
        }

        fun verifyStateMapping(lastUiState: SampleViewState, action: SampleUserIntentAction) {
            assertTrue { receivedUiState == lastUiState }
            assertTrue { receivedAction == action }
        }
    }

    // Sample Effect Producer
    private class SampleEffectProducer(sideEffect: SampleSideEffect? = null) :
        EffectProducer<SampleUserIntentAction, SampleViewState, SampleSideEffect> {
        private var newSideEffect: SampleSideEffect? = sideEffect
        private var receivedAction: SampleUserIntentAction? = null
        private var receivedUiState: SampleViewState? = null

        override fun invoke(
            action: SampleUserIntentAction,
            uiState: SampleViewState
        ): SampleSideEffect? {
            receivedAction = action
            receivedUiState = uiState
            return newSideEffect
        }

        fun setup(newSideEffect: SampleSideEffect) {
            this.newSideEffect = newSideEffect
        }

        fun verifyEffectProduced(uiState: SampleViewState, action: SampleUserIntentAction) {
            assertTrue { receivedAction == action }
            assertTrue { receivedUiState == uiState }
        }
    }
}