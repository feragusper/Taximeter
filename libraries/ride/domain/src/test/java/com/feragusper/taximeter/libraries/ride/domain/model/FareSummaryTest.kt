package com.feragusper.taximeter.libraries.ride.domain.model

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class FareSummaryTest {

    @Test
    fun `total fare summary`() =
        runTest {
            // Given
            val fareSummary = FareSummary(
                concepts = listOf(
                    FareSummary.Concept(
                        name = "Base fare",
                        price = 5.0,
                    ),
                    FareSummary.Concept(
                        name = "Time fare",
                        price = 2.0,
                    ),
                    FareSummary.Concept(
                        name = "Distance fare",
                        price = 3.0,
                    ),
                    FareSummary.Concept(
                        name = "Luggage fare",
                        price = 10.0,
                    ),
                ),
            )

            // When
            val result = fareSummary.total

            // Then
            result shouldBe 20.0
        }

}
