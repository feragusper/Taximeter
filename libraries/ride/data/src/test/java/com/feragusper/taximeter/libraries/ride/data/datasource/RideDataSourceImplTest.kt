package com.feragusper.taximeter.libraries.ride.data.datasource

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import com.feragusper.taximeter.libraries.ride.domain.model.PriceConfiguration
import com.feragusper.taximeter.libraries.ride.domain.model.Ride
import com.feragusper.taximeter.libraries.ride.domain.model.Supplement
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID

internal class RideDataSourceImplTest {

    private lateinit var rideDataSource: RideDataSourceImpl
    private val priceConfiguration = PriceConfiguration(
        pricePerKm = 1.0,
        pricePerSecond = 0.5
    )
    private val supplements = listOf(Supplement(UUID.randomUUID(), "Luggage", 5.0))

    @BeforeEach
    fun setup() {
        rideDataSource = RideDataSourceImpl()
    }

    @Nested
    inner class NoRideStarted {

        @Test
        fun `currentRide should be null`() {
            // Then
            rideDataSource.currentRide.shouldBeNull()
        }

        @Test
        fun `endRide should not change currentRide`() {
            // When
            rideDataSource.endRide()

            // Then
            rideDataSource.currentRide.shouldBeNull()
        }

        @Test
        fun `addLocationPoint should not update ride`() {
            // When
            rideDataSource.addLocationPoint(
                LocationPoint(
                    0.0,
                    0.0,
                )
            )

            // Then
            rideDataSource.currentRide.shouldBeNull()
        }
    }

    @Nested
    inner class RideIsStarted {

        @BeforeEach
        fun setup() {
            rideDataSource.startRide(priceConfiguration, supplements)
        }

        @Test
        fun `currentRide should not be null`() {
            rideDataSource.currentRide.shouldBeInstanceOf<Ride>()
        }

        @Test
        fun `ride should contain correct initial values`() {
            val ride = rideDataSource.currentRide!!
            ride.route shouldBe emptyList()
            ride.supplements shouldBe supplements
            ride.status shouldBe Ride.RideStatus.Started
            ride.endTime.shouldBeNull()
        }

        @Test
        fun `adding a location point should update the ride`() = runTest {
            // Given
            val locationPoint = LocationPoint(10.0, 20.0)

            // When
            rideDataSource.addLocationPoint(locationPoint)

            // Then
            val updatedRide = rideDataSource.currentRide!!
            updatedRide.route.size shouldBe 1
            updatedRide.route.first() shouldBe locationPoint
        }

        @Test
        fun `ending the ride should update the status and endTime`() = runTest {
            // When
            rideDataSource.endRide()

            // Then
            val updatedRide = rideDataSource.currentRide!!
            updatedRide.status shouldBe Ride.RideStatus.Ended
            updatedRide.endTime.shouldNotBeNull()
        }
    }

    @Nested
    inner class RideSupplementsUpdated {

        @BeforeEach
        fun setup() {
            rideDataSource.startRide(priceConfiguration, supplements)
        }

        @Test
        fun `update supplements should change supplement list`() {
            val newSupplements = listOf(Supplement(UUID.randomUUID(), "Child Seat", 3.0))
            rideDataSource.updateRideSupplements(newSupplements)

            val updatedRide = rideDataSource.currentRide!!
            updatedRide.supplements shouldBe newSupplements
        }
    }

    @Nested
    inner class RideStateRefresh {

        @BeforeEach
        fun setup() {
            rideDataSource.startRide(priceConfiguration, supplements)
        }

        @Test
        fun `refreshRideState should update the current ride state`() {
            // Given
            val updatedRide = Ride(
                route = listOf(LocationPoint(10.0, 20.0)),
                startTime = System.currentTimeMillis(),
                endTime = null,
                supplements = supplements,
                status = Ride.RideStatus.Started,
                priceConfiguration = priceConfiguration,
                updatedAt = System.currentTimeMillis()
            )

            // When
            rideDataSource.refreshRideState(updatedRide)

            // Then
            rideDataSource.currentRide.shouldNotBeNull()
            rideDataSource.currentRide shouldBe updatedRide
            rideDataSource.currentRide!!.route.size shouldBe 1
            rideDataSource.currentRide!!.route.first() shouldBe updatedRide.route.first()
        }

        @Test
        fun `refreshRideState should replace existing ride`() {
            // Given
            val newSupplements = listOf(Supplement(UUID.randomUUID(), "VIP Service", 20.0))
            val updatedRide = Ride(
                route = emptyList(),
                startTime = System.currentTimeMillis(),
                endTime = System.currentTimeMillis(),
                supplements = newSupplements,
                status = Ride.RideStatus.Ended,
                priceConfiguration = priceConfiguration,
                updatedAt = System.currentTimeMillis()
            )

            // When
            rideDataSource.refreshRideState(updatedRide)

            // Then
            rideDataSource.currentRide.shouldNotBeNull()
            rideDataSource.currentRide!!.status shouldBe Ride.RideStatus.Ended
            rideDataSource.currentRide!!.supplements shouldBe newSupplements
        }
    }
}
