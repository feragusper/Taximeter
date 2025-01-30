package com.feragusper.taximeter.features.taximeter

import com.feragusper.taximeter.libraries.ride.domain.model.FareSummary
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class TaximeterEffectProducerTest {
    private lateinit var subject: TaximeterEffectProducer

    @BeforeEach
    fun setUp() {
        // Initialize the subject before each test
        subject = TaximeterEffectProducer()
    }

    @Test
    fun `When action is ShowError, should respond with ShowError side effect`() {
        // Given: A ShowError action and an initial view state
        val action = TaximeterIntentAction.ShowError
        val viewState = TaximeterViewState()
        val expected = TaximeterSideEffect.ShowError

        // When: Invoking the effect producer with the action
        val actual = subject.invoke(action, viewState)

        // Then: The produced effect should be ShowError
        assertEquals(expected, actual, "Expected ShowError side effect but got different output")
    }

    @ParameterizedTest(name = "[{index}] {0} should return null")
    @MethodSource("actionParameters")
    internal fun `When action is not ShowError, should respond with null`(action: TaximeterIntentAction) {
        // Given: A valid action that is not ShowError
        val viewState = TaximeterViewState()

        // When: Invoking the effect producer with the action
        val actual = subject.invoke(action, viewState)

        // Then: The result should be null as no effect should be triggered
        assertEquals(null, actual, "Expected null but got an unexpected effect")
    }

    companion object {
        /**
         * Provides different actions that should not trigger a side effect.
         */
        @JvmStatic
        fun actionParameters(): Iterable<Arguments> =
            listOf(
                Arguments.of(
                    TaximeterIntentAction.UpdateFare(
                        price = 30.0,
                        elapsedTime = 300,
                        supplements = emptyMap(),
                        distance = 15.0
                    )
                ),
                Arguments.of(TaximeterIntentAction.StartRide),
                Arguments.of(TaximeterIntentAction.UpdateFareSummary(FareSummary(emptyList()))),
            )
    }
}
