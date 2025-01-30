package com.feragusper.taximeter.libraries.ride.domain

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import com.feragusper.taximeter.libraries.location.domain.usecase.GetLocationUpdatesUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.AddLocationPointToCurrentRideUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.RefreshRideStateUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RideUpdateCoordinatorTest {

    private val addLocationPointToCurrentRideUseCase: AddLocationPointToCurrentRideUseCase =
        mockk(relaxed = true)
    private val refreshRideStateUseCase: RefreshRideStateUseCase = mockk(relaxed = true)
    private val getLocationUpdatesUseCase: GetLocationUpdatesUseCase = mockk(relaxed = true)

    private lateinit var coordinator: RideUpdateCoordinator

    @BeforeEach
    fun setup() {
        coordinator = RideUpdateCoordinator(
            addLocationPointToCurrentRideUseCase,
            refreshRideStateUseCase,
            getLocationUpdatesUseCase
        )
    }

    @Test
    fun `startRideUpdates should collect location updates`() = runTest {
        // Given
        val mockLocation = LocationPoint(10.0, 20.0)
        coEvery { getLocationUpdatesUseCase() } returns flowOf(mockLocation)

        // When
        coordinator.startRideUpdates()

        // Advance time to ensure periodic refresh is triggered
        advanceTimeBy(2000L)

        // Then
        coVerify { addLocationPointToCurrentRideUseCase(mockLocation) }

        coordinator.stopRideUpdates()
    }

    @Test
    fun `stopRideUpdates should cancel ongoing jobs`() = runTest {
        // Given
        coordinator.startRideUpdates()
        delay(1000L)

        // When
        coordinator.stopRideUpdates()

        // Then
        assert(!coordinator.isRunning())
    }
}
