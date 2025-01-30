package com.feragusper.taximeter.libraries.location.usecase

import com.feragusper.taximeter.libraries.location.data.usecase.GetLocationUpdatesUseCaseImpl
import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import com.feragusper.taximeter.libraries.location.domain.repository.LocationTrackingRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetLocationUpdatesUseCaseImplTest {

    private val mockRepository: LocationTrackingRepository = mockk()
    private lateinit var getLocationUpdatesUseCase: GetLocationUpdatesUseCaseImpl

    @BeforeEach
    fun setup() {
        getLocationUpdatesUseCase = GetLocationUpdatesUseCaseImpl(mockRepository)
    }

    @Test
    fun `should return location updates from repository`() = runTest {
        // Given
        val locationPoint = LocationPoint(10.0, 20.0)
        coEvery { mockRepository.getLocationUpdates() } returns flowOf(locationPoint)

        // When
        val result = getLocationUpdatesUseCase()

        // Then
        result.collect { point ->
            assertEquals(locationPoint, point)
        }
    }
}
