package com.feragusper.taximeter.libraries.ride.data.datasource

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import com.feragusper.taximeter.libraries.ride.domain.model.PriceConfiguration
import com.feragusper.taximeter.libraries.ride.domain.model.Ride
import com.feragusper.taximeter.libraries.ride.domain.model.Supplement
import kotlinx.coroutines.flow.Flow

/**
 * Data source interface that manages ride-related operations.
 *
 * This interface defines methods to manage the lifecycle of a ride, including
 * starting, updating, and ending the ride, as well as tracking ride data in real time.
 */
interface RideDataSource {

    /**
     * Provides a stream of ride updates as a [Flow].
     *
     * This can be used to observe changes to the ride details in real time.
     */
    val rideUpdates: Flow<Ride?>

    /**
     * Retrieves the current ongoing ride, if available.
     *
     * @return The current [Ride], or null if no ride is active.
     */
    val currentRide: Ride?

    /**
     * Starts a new ride with the provided pricing configuration and supplements.
     *
     * @param priceConfiguration The pricing configuration to be applied to the ride.
     * @param supplements The list of additional supplements selected by the user.
     */
    fun startRide(priceConfiguration: PriceConfiguration, supplements: List<Supplement>)

    /**
     * Adds a new location point to the current ride's route.
     *
     * @param locationPoint The [LocationPoint] to be added to the ride.
     */
    fun addLocationPoint(locationPoint: LocationPoint)

    /**
     * Updates the list of supplements associated with the current ride.
     *
     * @param supplements The updated list of supplements.
     */
    fun updateRideSupplements(supplements: List<Supplement>)

    /**
     * Ends the current ride and finalizes its details.
     *
     * This will mark the ride as completed and stop any further updates.
     */
    fun endRide()

    /**
     * Refreshes the state of the current ride.
     *
     * @param ride The current ride to be refreshed.
     */
    fun refreshRideState(ride: Ride)
}
