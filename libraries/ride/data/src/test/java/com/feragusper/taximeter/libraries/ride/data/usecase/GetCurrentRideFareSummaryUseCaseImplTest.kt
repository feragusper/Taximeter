package com.feragusper.taximeter.libraries.ride.data.usecase

import com.feragusper.taximeter.libraries.ride.domain.model.FareSummary
import com.feragusper.taximeter.libraries.ride.domain.repository.RideRepository
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetCurrentRideFareSummaryUseCaseImplTest {
    private val rideRepository: RideRepository = mockk()

    private lateinit var subject: GetCurrentRideFareSummaryUseCaseImpl

    @BeforeEach
    fun setup() {
        subject = GetCurrentRideFareSummaryUseCaseImpl(rideRepository)
    }

    @Test
    fun `get current ride fare summary success`() =
        runTest {
            // Given
            val fareSummary = mockk<FareSummary>()
            every { rideRepository.currentRide?.fareSummary } returns fareSummary

            // When
            val result = subject.invoke()

            // Then
            result shouldBeSuccess fareSummary
        }

    @Test
    fun `get current ride fare summary empty`() =
        runTest {
            // Given
            every { rideRepository.currentRide } returns null

            // When
            val result = subject.invoke()

            // Then
            result.shouldBeFailure { it.message shouldBe "No ride in progress" }
        }

    @Test
    fun `get current ride fare summary failure`() =
        runTest {
            // Given
            val exception = Exception()
            every { rideRepository.currentRide?.fareSummary } throws exception

            // When
            val result = subject.invoke()

            // Then
            result shouldBeFailure exception
        }
}
