package com.feragusper.taximeter.libraries.ride.data.usecase

import com.feragusper.taximeter.libraries.ride.domain.model.Ride
import com.feragusper.taximeter.libraries.ride.domain.repository.RideRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RefreshRideStateUseCaseImplTest {

    private lateinit var refreshRideStateUseCase: RefreshRideStateUseCaseImpl
    private val rideRepository: RideRepository = mockk(relaxUnitFun = true)
    private val testRide = Ride(
        route = emptyList(),
        startTime = System.currentTimeMillis() - 10000,
        endTime = null,
        supplements = emptyList(),
        status = Ride.RideStatus.Started,
        priceConfiguration = mockk(),
        updatedAt = System.currentTimeMillis()
    )

    @BeforeEach
    fun setup() {
        refreshRideStateUseCase = RefreshRideStateUseCaseImpl(rideRepository)
    }

    @Test
    fun `when invoked with active ride should refresh ride state`() = runTest {
        // Given
        coEvery { rideRepository.currentRide } returns testRide

        // When
        refreshRideStateUseCase.invoke()

        // Then
        coVerify(exactly = 1) {
            rideRepository.refreshRideState(withArg { refreshedRide ->
                assertNotNull(refreshedRide)
                assert(refreshedRide.updatedAt > testRide.updatedAt)
            })
        }
    }

    @Test
    fun `when no active ride exists should not trigger ride refresh`() = runTest {
        // Given
        coEvery { rideRepository.currentRide } returns null

        // When
        refreshRideStateUseCase.invoke()

        // Then
        coVerify(exactly = 0) { rideRepository.refreshRideState(any()) }
    }
}
