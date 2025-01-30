package com.feragusper.taximeter.libraries.ride.domain.usecase

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint

/**
 * Use case to add a location point to the current ride.
 */
interface AddLocationPointToCurrentRideUseCase {
    suspend operator fun invoke(locationPoint: LocationPoint): Result<Unit>
}