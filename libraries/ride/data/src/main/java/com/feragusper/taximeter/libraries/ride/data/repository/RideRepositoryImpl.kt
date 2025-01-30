package com.feragusper.taximeter.libraries.ride.data.repository

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import com.feragusper.taximeter.libraries.ride.data.datasource.RideDataSource
import com.feragusper.taximeter.libraries.ride.data.datasource.SupplementDataSource
import com.feragusper.taximeter.libraries.ride.domain.model.PriceConfiguration
import com.feragusper.taximeter.libraries.ride.domain.model.Ride
import com.feragusper.taximeter.libraries.ride.domain.repository.RideRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Implementation of [RideRepository] to manage ride-related operations.
 *
 * This repository delegates ride operations to the underlying data source,
 * providing a clean interface for use cases to interact with ride data.
 *
 * @param rideDataSource The data source responsible for managing ride data.
 * @param supplementDataSource The data source responsible for managing supplement data.
 */
class RideRepositoryImpl(
    private val rideDataSource: RideDataSource,
    private val supplementDataSource: SupplementDataSource
) : RideRepository {

    /**
     * Observes updates of the current ride from the data source.
     */
    override val rideUpdates: Flow<Ride?>
        get() = rideDataSource.rideUpdates

    /**
     * Retrieves the current ride if available.
     */
    override val currentRide: Ride?
        get() = rideDataSource.currentRide

    /**
     * Starts a new ride with the provided price configuration and supplements.
     *
     * @param priceConfiguration The price configuration to be applied to the ride.
     * @param supplements The list of additional supplements selected by the user.
     */
    override fun startRide(priceConfiguration: PriceConfiguration, supplements: List<UUID>) {
        rideDataSource.startRide(
            priceConfiguration,
            supplementDataSource.getSupplements(supplements)
        )
    }

    /**
     * Adds a new location point to the ongoing ride.
     *
     * @param locationPoint The new location point to be added.
     */
    override fun addLocationPoint(locationPoint: LocationPoint) {
        rideDataSource.addLocationPoint(locationPoint)
    }

    /**
     * Updates the supplements for the current ride.
     *
     * @param supplements A list of [UUID] representing the supplements to be applied.
     */
    override fun updateRideSupplements(supplements: List<UUID>) {
        rideDataSource.updateRideSupplements(supplementDataSource.getSupplements(supplements))
    }

    /**
     * Ends the current ride.
     */
    override fun endRide() {
        rideDataSource.endRide()
    }

    /**
     * Refreshes the state of the current ride.
     */
    override fun refreshRideState(ride: Ride) {
        rideDataSource.refreshRideState(ride)
    }

}
