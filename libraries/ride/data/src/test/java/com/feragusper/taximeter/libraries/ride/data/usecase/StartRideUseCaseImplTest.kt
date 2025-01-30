package com.feragusper.taximeter.libraries.ride.data.usecase

import com.feragusper.taximeter.libraries.ride.domain.model.PriceConfiguration
import com.feragusper.taximeter.libraries.ride.domain.repository.PriceRepository
import com.feragusper.taximeter.libraries.ride.domain.repository.RideRepository
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

internal class StartRideUseCaseImplTest {
    private val rideRepository: RideRepository = mockk()
    private val priceRepository: PriceRepository = mockk()

    private lateinit var subject: StartRideUseCaseImpl

    @BeforeEach
    fun setup() {
        subject = StartRideUseCaseImpl(
            rideRepository = rideRepository,
            priceRepository = priceRepository
        )
    }

    @Test
    fun `start ride success`() =
        runTest {
            // Given
            val priceConfiguration: PriceConfiguration = mockk()
            val supplements: List<UUID> = mockk()

            coEvery {
                priceRepository.getPriceConfiguration()
            } returns priceConfiguration

            every {
                rideRepository.startRide(
                    priceConfiguration = priceConfiguration,
                    supplements = supplements
                )
            } just Runs


            // When
            val result = subject.invoke(supplements)

            // Then
            result shouldBeSuccess Unit
        }

    @Test
    fun `price configuration failure`() =
        runTest {
            // Given
            val exception = Exception()
            val supplements: List<UUID> = mockk()

            coEvery {
                priceRepository.getPriceConfiguration()
            } throws exception

            // When
            val result = subject.invoke(supplements)

            // Then
            result shouldBeFailure exception
        }

    @Test
    fun `start ride failure`() =
        runTest {
            // Given
            val exception = Exception()
            val priceConfiguration: PriceConfiguration = mockk()
            val supplements: List<UUID> = mockk()

            coEvery {
                priceRepository.getPriceConfiguration()
            } returns priceConfiguration

            every {
                rideRepository.startRide(
                    priceConfiguration = priceConfiguration,
                    supplements = supplements
                )
            } throws exception

            // When
            val result = subject.invoke(supplements)

            // Then
            result shouldBeFailure exception
        }
}
