package com.feragusper.taximeter.libraries.ride.data.usecase

import com.feragusper.taximeter.libraries.ride.domain.repository.RideRepository
import com.feragusper.taximeter.libraries.ride.domain.usecase.RefreshRideStateUseCase

/**
 * Use case to refresh ride state periodically by emitting the current ride to the UI.
 */
class RefreshRideStateUseCaseImpl(
    private val rideRepository: RideRepository
) : RefreshRideStateUseCase {
    /**
     * Emits the current ride state without modifying any values.
     */
    override suspend operator fun invoke() {
        rideRepository.currentRide?.let { currentRide ->
            // Emit the current ride again to trigger UI updates
            rideRepository.refreshRideState(
                currentRide.copy(
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
}
