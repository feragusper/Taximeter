package com.feragusper.taximeter.libraries.ride.domain.usecase

/**
 * Use case to refresh ride state periodically by emitting the current ride to the UI.
 */
interface RefreshRideStateUseCase {

    /**
     * Emits the current ride state without modifying any values.
     */
    suspend operator fun invoke()
}
