package com.feragusper.taximeter.libraries.ride.domain

import com.feragusper.taximeter.libraries.location.domain.usecase.GetLocationUpdatesUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.AddLocationPointToCurrentRideUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.RefreshRideStateUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Coordinator that manages the updates of the current ride.
 * It starts the location updates and refreshes the state of the current ride periodically.
 *
 * @param addLocationPointToCurrentRideUseCase The use case to add location points to the current ride.
 * @param refreshRideStateUseCase The use case to refresh the state of the current ride.
 * @param getLocationUpdatesUseCase The use case to get location updates.
 */
class RideUpdateCoordinator(
    private val addLocationPointToCurrentRideUseCase: AddLocationPointToCurrentRideUseCase,
    private val refreshRideStateUseCase: RefreshRideStateUseCase,
    private val getLocationUpdatesUseCase: GetLocationUpdatesUseCase,
) {
    private lateinit var supervisorJob: Job
    private lateinit var coroutineScope: CoroutineScope

    fun startRideUpdates() {
        initJob()

        coroutineScope.launch {
            getLocationUpdatesUseCase()
                .collect {
                    addLocationPointToCurrentRideUseCase(it)
                }
        }

        coroutineScope.launch {
            while (true) {
                delay(1000L)
                refreshRideStateUseCase()
            }
        }
    }

    fun stopRideUpdates() {
        supervisorJob.cancel()
    }

    fun isRunning() = supervisorJob.isActive

    private fun initJob() {
        supervisorJob = SupervisorJob()
        coroutineScope = CoroutineScope(Dispatchers.IO + supervisorJob)
    }
}

