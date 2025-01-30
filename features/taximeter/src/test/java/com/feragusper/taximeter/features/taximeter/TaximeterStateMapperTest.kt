package com.feragusper.taximeter.features.taximeter

import com.feragusper.taximeter.libraries.ride.domain.model.FareSummary
import com.feragusper.taximeter.libraries.ride.domain.model.Supplement
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Locale
import java.util.UUID

class TaximeterStateMapperTest {
    private lateinit var subject: TaximeterStateMapper

    @BeforeEach
    fun setUp() {
        // Initialize the StateMapper before each test
        subject = TaximeterStateMapper()
        Locale.setDefault(Locale.FRANCE)
    }

    @Test
    fun `when action is UpdateFare, should update fare, elapsedTime, supplements, and distance`() {
        // Given: A supplement and expected state change upon receiving UpdateFare action
        val supplement = Supplement(UUID.randomUUID(), "Luggage", 5.0)
        val supplementsMap = mapOf(supplement to 2)
        val action = TaximeterIntentAction.UpdateFare(
            price = 25.0,
            elapsedTime = 300,
            supplements = supplementsMap,
            distance = 15.0
        )
        val lastUiState = TaximeterViewState()

        val expectedState = lastUiState.copy(
            isLoading = false,
            fare = "25,00 €",
            elapsedTime = "5m 0s",
            supplements = supplementsMap.map { (supplement, count) ->
                TaximeterViewState.Supplement(
                    supplement.id,
                    supplement.name,
                    count
                )
            },
            distance = "15 km"
        )

        // When: The state mapper processes the action
        val actualState = subject.invoke(action, lastUiState)

        // Then: Ensure the state is updated correctly
        actualState shouldBe expectedState
    }

    @Test
    fun `when action is UpdateRideInProgress, should update ride progress`() {
        // Given: Initial ride state is not in progress
        val action = TaximeterIntentAction.StartRide
        val lastUiState = TaximeterViewState(inProgress = false)

        val expectedState = lastUiState.copy(inProgress = true)

        // When: The state mapper processes the action
        val actualState = subject.invoke(action, lastUiState)

        // Then: Ensure the ride progress state is updated correctly
        actualState shouldBe expectedState
    }

    @Test
    fun `when action is UpdateFareSummary, should update fare summary breakdown`() {
        // Given: A fare summary with multiple cost breakdowns
        val fareSummary = FareSummary(
            concepts = listOf(
                FareSummary.Concept("Base Fare", 10.0),
                FareSummary.Concept("Distance Fare", 15.0)
            )
        )

        val action = TaximeterIntentAction.UpdateFareSummary(fareSummary)
        val lastUiState = TaximeterViewState()

        val expectedState = lastUiState.copy(
            priceBreakdown = TaximeterViewState.PriceBreakdown(
                items = fareSummary.concepts.map {
                    TaximeterViewState.PriceBreakdown.Item(it.name, it.price.formatCurrency())
                },
                total = fareSummary.total.formatCurrency()
            )
        )

        // When: The state mapper processes the action
        val actualState = subject.invoke(action, lastUiState)

        // Then: Ensure the price breakdown is updated correctly
        actualState shouldBe expectedState
    }

    @Test
    fun `when action is ShowError, should update isLoading to false`() {
        // Given: Initial state where loading is true
        val action = TaximeterIntentAction.ShowError
        val lastUiState = TaximeterViewState(isLoading = true)

        val expectedState = lastUiState.copy(isLoading = false)

        // When: The state mapper processes the action
        val actualState = subject.invoke(action, lastUiState)

        // Then: Ensure loading is set to false in case of an error
        actualState shouldBe expectedState
    }
}
