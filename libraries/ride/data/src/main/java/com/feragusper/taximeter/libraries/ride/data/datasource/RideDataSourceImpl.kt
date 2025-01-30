package com.feragusper.taximeter.libraries.ride.data.datasource

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import com.feragusper.taximeter.libraries.ride.domain.model.PriceConfiguration
import com.feragusper.taximeter.libraries.ride.domain.model.Ride
import com.feragusper.taximeter.libraries.ride.domain.model.Supplement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Implementation of the [RideDataSource] interface to manage ride data operations.
 *
 * This class maintains the current ride and provides operations to manipulate
 * the ride's lifecycle including starting, updating, and ending a ride.
 */
class RideDataSourceImpl : RideDataSource {

    // Mutable state flow to hold the current ride
    private val _currentRide = MutableStateFlow<Ride?>(null)

    /**
     * Provides flow updates of the current ride.
     */
    override val rideUpdates: Flow<Ride?> = _currentRide.asStateFlow()

    /**
     * Retrieves the current active ride, or null if no active ride exists.
     */
    override val currentRide: Ride?
        get() = _currentRide.value

    /**
     * Starts a new ride with the given pricing configuration and supplements.
     *
     * @param priceConfiguration The price configuration for the ride.
     * @param supplements List of selected ride supplements.
     */
    override fun startRide(
        priceConfiguration: PriceConfiguration,
        supplements: List<Supplement>
    ) {
        _currentRide.value = Ride(
            route = emptyList(),
            startTime = System.currentTimeMillis(),
            endTime = null,
            supplements = supplements,
            status = Ride.RideStatus.Started,
            priceConfiguration = priceConfiguration,
            updatedAt = System.currentTimeMillis()
        )
    }

    /**
     * Adds a location point to the current ride.
     *
     * @param locationPoint The new location point to be added to the ride's route.
     */
    override fun addLocationPoint(locationPoint: LocationPoint) {
        updateCurrentRide(locationPoint = locationPoint)
    }

    /**
     * Updates the current ride with new supplements.
     *
     * @param supplements The updated list of supplements for the ride.
     */
    override fun updateRideSupplements(supplements: List<Supplement>) {
        updateCurrentRide(supplements = supplements)
    }

    /**
     * Ends the current ride by setting its status to 'Ended' and logging the end time.
     */
    override fun endRide() {
        updateCurrentRide(
            status = Ride.RideStatus.Ended,
            endTime = System.currentTimeMillis()
        )
    }

    /**
     * Refreshes the state of the current ride with the provided ride data.
     *
     * @param ride The current ride to be refreshed.
     */
    override fun refreshRideState(ride: Ride) {
        _currentRide.value = ride
    }

    /**
     * Updates the current ride with optional parameters such as new location points, updated supplements, status, or end time.
     *
     * @param locationPoint An optional new location point to add to the ride's route.
     * @param supplements An optional updated list of supplements.
     * @param status The new status of the ride (e.g., Started, Ended).
     * @param endTime The end timestamp for the ride, if applicable.
     */
    private fun updateCurrentRide(
        locationPoint: LocationPoint? = null,
        supplements: List<Supplement>? = null,
        status: Ride.RideStatus? = null,
        endTime: Long? = null
    ) {
        _currentRide.value?.let { ride ->
            _currentRide.value = ride.copy(
                route = ride.route + (locationPoint?.let { listOf(it) } ?: emptyList()),
                supplements = supplements ?: ride.supplements,
                status = status ?: ride.status,
                endTime = endTime ?: ride.endTime
            )
        }
    }
}