package com.feragusper.taximeter.libraries.ride.domain.model

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import io.kotest.matchers.doubles.shouldBeExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

internal class RideTest {

    private lateinit var ride: Ride
    private lateinit var priceConfiguration: PriceConfiguration
    private lateinit var supplements: List<Supplement>
    private lateinit var route: List<LocationPoint>

    @BeforeEach
    fun setUp() {
        priceConfiguration =
            PriceConfiguration(
                pricePerKm = 0.5,
                pricePerSecond = 0.1
            )

        supplements = listOf(
            Supplement(id = UUID.randomUUID(), name = "Luggage", price = 5.0),
            Supplement(id = UUID.randomUUID(), name = "Pet", price = 3.0)
        )

        route = listOf(
            LocationPoint(lat = 40.4168, lon = -3.7038),
            LocationPoint(lat = 40.4170, lon = -3.7035)
        )

        ride = Ride(
            route = route,
            startTime = System.currentTimeMillis() - 10.seconds.inWholeMilliseconds,
            endTime = System.currentTimeMillis(),
            supplements = supplements,
            status = Ride.RideStatus.Started,
            priceConfiguration = priceConfiguration,
            updatedAt = System.currentTimeMillis()
        )
    }

    @Nested
    inner class DistanceCalculation {

        @Test
        fun `should calculate correct distance between two points`() {
            val expectedDistance = route.first().distanceTo(route.last())
            val calculatedDistance = ride.route.calculateTotalDistance()
            calculatedDistance shouldBeExactly expectedDistance
        }

        @Test
        fun `should return zero when no route points`() {
            val emptyRide = ride.copy(route = emptyList())
            emptyRide.distance shouldBeExactly 0.0
        }
    }

    @Nested
    inner class FareSummaryCalculation {

        @Test
        fun `should calculate correct fare with distance, time, and supplements`() {
            val fareSummary = ride.fareSummary

            fareSummary.concepts.size shouldBe 3
            fareSummary.concepts.find { it.name == "Distance" }!!.price shouldBeExactly route.calculateTotalDistance() * priceConfiguration.pricePerKm
            fareSummary.concepts.find { it.name == "Time" }!!.price shouldBeExactly ride.timeInSeconds * priceConfiguration.pricePerSecond
            fareSummary.concepts.find { it.name == "Supplements" }!!.price shouldBeExactly supplements.sumOf { it.price }
        }
    }

    @Nested
    inner class RidePriceCalculation {

        @Test
        fun `should calculate total ride price correctly`() {
            val expectedPrice =
                route.calculateTotalDistance() * priceConfiguration.pricePerKm +
                        ride.timeInSeconds * priceConfiguration.pricePerSecond +
                        supplements.sumOf { it.price }

            ride.totalPrice shouldBeExactly expectedPrice
        }
    }

    @Nested
    inner class TimeCalculation {

        @Test
        fun `should calculate time in seconds correctly`() {
            val calculatedTime = ride.timeInSeconds
            val expectedTime = ((ride.endTime
                ?: System.currentTimeMillis()) - ride.startTime).milliseconds.inWholeSeconds
            calculatedTime shouldBe expectedTime
        }

        @Test
        fun `should use current time when ride is ongoing`() {
            val ongoingRide = ride.copy(endTime = null)
            val expectedTime =
                ((System.currentTimeMillis() - ride.startTime)).milliseconds.inWholeSeconds
            ongoingRide.timeInSeconds shouldBe expectedTime
        }
    }
}
