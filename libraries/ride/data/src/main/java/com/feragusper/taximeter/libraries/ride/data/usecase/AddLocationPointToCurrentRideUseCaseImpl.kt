package com.feragusper.taximeter.libraries.ride.data.usecase

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import com.feragusper.taximeter.libraries.ride.domain.repository.RideRepository
import com.feragusper.taximeter.libraries.ride.domain.usecase.AddLocationPointToCurrentRideUseCase

/**
 * Implementation of the [AddLocationPointToCurrentRideUseCase] interface.
 * This use case handles the addition of location points to the current ride.
 *
 * @param rideRepository The repository responsible for ride-related operations.
 */
class AddLocationPointToCurrentRideUseCaseImpl(
    private val rideRepository: RideRepository
) : AddLocationPointToCurrentRideUseCase {

    /**
     * Adds a new location point to the current ride.
     *
     * @param locationPoint The location data to be added.
     * @return A [Result] indicating success or failure of the operation.
     */
    override suspend operator fun invoke(locationPoint: LocationPoint): Result<Unit> {
        return runCatching {
            rideRepository.addLocationPoint(locationPoint)
            Result.success(Unit)
        }.getOrElse {
            Result.failure(it)
        }
    }
}
