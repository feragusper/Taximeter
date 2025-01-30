package com.feragusper.taximeter.libraries.ride.domain.repository

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import com.feragusper.taximeter.libraries.ride.domain.model.PriceConfiguration
import com.feragusper.taximeter.libraries.ride.domain.model.Ride
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Repository to manage the current ride.
 */
interface RideRepository {

    /**
     * Flow with the updates of the current ride.
     */
    val rideUpdates: Flow<Ride?>

    /**
     * The current ride.
     */
    val currentRide: Ride?

    /**
     * Start a new ride with the given price configuration and supplements.
     *
     * @param priceConfiguration The price configuration for the ride.
     * @param supplements The list of selected ride supplements.
     */
    fun startRide(priceConfiguration: PriceConfiguration, supplements: List<UUID>)

    /**
     * Add a location point to the current ride.
     *
     * @param locationPoint The new location point to be added to the ride's route.
     */
    fun addLocationPoint(locationPoint: LocationPoint)

    /**
     * Update the list of supplements associated with the current ride.
     *
     * @param supplements The updated list of supplements.
     */
    fun updateRideSupplements(supplements: List<UUID>)

    /**
     * End the current ride and finalize its details.
     *
     * This will mark the ride as completed and stop any further updates.
     */
    fun endRide()

    /**
     * Refresh the state of the current ride.
     *
     * @param ride The current ride to be refreshed.
     */
    fun refreshRideState(ride: Ride)
}