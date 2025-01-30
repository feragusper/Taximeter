package com.feragusper.taximeter.libraries.location.repository

import com.feragusper.taximeter.libraries.location.data.datasource.LocationDataSource
import com.feragusper.taximeter.libraries.location.data.repository.LocationTrackingRepositoryImpl
import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.feragusper.taximeter.location.LocationPoint as LocationPointEntity

class LocationTrackingRepositoryImplTest {

    private val mockDataSource: LocationDataSource = mockk()
    private lateinit var repository: LocationTrackingRepositoryImpl

    @BeforeEach
    fun setup() {
        repository = LocationTrackingRepositoryImpl(mockDataSource)
    }

    @Test
    fun `should fetch location updates from data source`() = runTest {
        // Given
        val locationPoint = LocationPointEntity(30.0, 40.0, "UTC", System.currentTimeMillis())
        every { mockDataSource.getLocationUpdates() } returns flowOf(locationPoint)

        // When
        val result = repository.getLocationUpdates()

        // Then
        result.collect { point ->
            assertEquals(LocationPoint(locationPoint.lat, locationPoint.long), point)
        }
    }
}
