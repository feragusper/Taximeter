package com.feragusper.taximeter.libraries.ride.data.repository

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import com.feragusper.taximeter.libraries.ride.data.datasource.RideDataSource
import com.feragusper.taximeter.libraries.ride.data.datasource.SupplementDataSource
import com.feragusper.taximeter.libraries.ride.domain.model.PriceConfiguration
import com.feragusper.taximeter.libraries.ride.domain.model.Ride
import com.feragusper.taximeter.libraries.ride.domain.model.Supplement
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID

internal class RideRepositoryImplTest {

    private val rideDataSource: RideDataSource = mockk(relaxed = true)
    private val supplementDataSource: SupplementDataSource = mockk(relaxed = true)
    private lateinit var rideRepository: RideRepositoryImpl

    private val priceConfiguration = PriceConfiguration(
        pricePerKm = 1.0,
        pricePerSecond = 0.5
    )

    private val supplements = listOf(Supplement(UUID.randomUUID(), "Luggage", 5.0))

    @BeforeEach
    fun setup() {
        rideRepository = RideRepositoryImpl(
            rideDataSource = rideDataSource,
            supplementDataSource = supplementDataSource
        )
    }

    @Nested
    inner class GivenNoRideStarted {

        @Test
        fun `currentRide should be null`() {
            // Given
            every { rideDataSource.currentRide } returns null

            // Then
            rideRepository.currentRide.shouldBeNull()
        }

        @Test
        fun `endRide should not change currentRide`() {
            // When
            rideRepository.endRide()

            // Then
            verify { rideDataSource.endRide() }
        }

        @Test
        fun `addLocationPoint should not update ride`() {
            // Given
            val locationPoint = LocationPoint(0.0, 0.0)

            // When
            rideRepository.addLocationPoint(locationPoint)

            // Then
            verify { rideDataSource.addLocationPoint(locationPoint) }
        }
    }

    @Nested
    inner class GivenARideIsStarted {

        @BeforeEach
        fun setup() {
            rideRepository.startRide(priceConfiguration, supplements.map { it.id })
        }

        @Test
        fun `currentRide should not be null`() {
            // Given
            val ride = Ride(
                emptyList(),
                System.currentTimeMillis(),
                null,
                supplements,
                Ride.RideStatus.Started,
                priceConfiguration,
                System.currentTimeMillis(),
            )
            every { rideDataSource.currentRide } returns ride

            // Then
            rideRepository.currentRide.shouldBeInstanceOf<Ride>()
        }

        @Test
        fun `ride should contain correct initial values`() {
            // Given
            val ride = Ride(
                emptyList(),
                System.currentTimeMillis(),
                null,
                supplements,
                Ride.RideStatus.Started,
                priceConfiguration,
                System.currentTimeMillis(),
            )
            every { rideDataSource.currentRide } returns ride

            // When
            val currentRide = rideRepository.currentRide!!

            // Then
            currentRide.route shouldBe emptyList()
            currentRide.supplements shouldBe supplements
            currentRide.status shouldBe Ride.RideStatus.Started
            currentRide.endTime.shouldBeNull()
        }

        @Test
        fun `adding a location point should update the ride`() = runTest {
            // Given
            val locationPoint = LocationPoint(10.0, 20.0)

            // When
            rideRepository.addLocationPoint(locationPoint)

            // Then
            verify { rideDataSource.addLocationPoint(locationPoint) }
        }

        @Test
        fun `ending the ride should update the status and endTime`() = runTest {
            // When
            rideRepository.endRide()

            // Then
            verify { rideDataSource.endRide() }
        }
    }

    @Nested
    inner class UpdatingRideSupplements {

        @BeforeEach
        fun setup() {
            rideRepository.startRide(priceConfiguration, supplements.map { it.id })
        }

        @Test
        fun `update supplements should change supplement list`() {
            // Given
            val newSupplements = listOf(Supplement(UUID.randomUUID(), "Child Seat", 3.0))
            every { supplementDataSource.getSupplements(newSupplements.map { it.id }) } returns newSupplements

            // When
            rideRepository.updateRideSupplements(newSupplements.map { it.id })

            // Then
            verify { rideDataSource.updateRideSupplements(newSupplements) }
        }
    }

    @Nested
    inner class ObservingRideUpdates {

        @Test
        fun `rideUpdates should emit correct values`() = runTest {
            // Given
            val ride = Ride(
                emptyList(),
                System.currentTimeMillis(),
                null,
                supplements,
                Ride.RideStatus.Started,
                priceConfiguration,
                System.currentTimeMillis(),
            )
            every { rideDataSource.rideUpdates } returns flowOf(ride)

            // When
            val result = rideRepository.rideUpdates

            // Then
            result.collect {
                it shouldBe ride
            }
        }
    }

    @Test
    fun `refreshRideState should forward ride to rideDataSource`() {
        // Given
        val ride = Ride(
            emptyList(),
            System.currentTimeMillis(),
            null,
            supplements,
            Ride.RideStatus.Started,
            priceConfiguration,
            System.currentTimeMillis(),
        )

        // When
        rideRepository.refreshRideState(ride)

        // Then
        verify(exactly = 1) { rideDataSource.refreshRideState(ride) }
    }
}
