package com.feragusper.taximeter.libraries.ride.data.usecase

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import com.feragusper.taximeter.libraries.ride.domain.repository.RideRepository
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AddLocationPointToCurrentRideUseCaseImplTest {
    private val rideRepository: RideRepository = mockk()

    private lateinit var subject: AddLocationPointToCurrentRideUseCaseImpl

    @BeforeEach
    fun setup() {
        subject = AddLocationPointToCurrentRideUseCaseImpl(
            rideRepository
        )
    }

    @Test
    fun `add location success`() =
        runTest {
            // Given
            val locationPoint = mockk<LocationPoint>()
            every { rideRepository.addLocationPoint(locationPoint) } just Runs

            // When
            val result = subject.invoke(locationPoint)

            // Then
            result shouldBeSuccess Unit
        }

    @Test
    fun `add location failure`() =
        runTest {
            // Given
            val locationPoint = mockk<LocationPoint>()
            val exception = Exception()
            every { rideRepository.addLocationPoint(locationPoint) } throws exception

            // When
            val result = subject.invoke(locationPoint)

            // Then
            result shouldBeFailure exception
        }
}
