package com.feragusper.taximeter.features.taximeter

import com.feragusper.taximeter.libraries.mvi.IntentProcessor
import com.feragusper.taximeter.libraries.ride.domain.RideUpdateCoordinator
import com.feragusper.taximeter.libraries.ride.domain.model.Ride
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetCurrentRideFareSummaryUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetCurrentRideUpdatesUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetSupplementsUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.StartRideUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.UpdateRideSupplementsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Handles processing of Taximeter intents and translates them into actions.
 */
internal class TaximeterIntentProcessor(
    private val getSupplementsUseCase: GetSupplementsUseCase,
    private val startRideUseCase: StartRideUseCase,
    private val rideUpdateCoordinator: RideUpdateCoordinator,
    private val getCurrentRideUpdatesUseCase: GetCurrentRideUpdatesUseCase,
    private val updateRideSupplementsUseCase: UpdateRideSupplementsUseCase,
    private val getCurrentRideFareSummaryUseCase: GetCurrentRideFareSummaryUseCase
) : IntentProcessor<TaximeterIntent, TaximeterIntentAction> {

    override suspend fun invoke(intent: TaximeterIntent): Flow<TaximeterIntentAction> =
        when (intent) {
            is TaximeterIntent.Open -> handleOpenIntent()
            is TaximeterIntent.StartRide -> handleStartRide(intent)
            is TaximeterIntent.UpdateSupplements -> handleUpdateSupplements(intent)
            is TaximeterIntent.StopRide -> handleStopRide()
        }

    /**
     * Handles the `Open` intent by retrieving ride updates and available supplements.
     */
    private suspend fun handleOpenIntent(): Flow<TaximeterIntentAction> =
        getCurrentRideUpdatesUseCase().map { result ->
            result.fold(
                onSuccess = { ride -> mapRideToUpdateFareAction(ride) },
                onFailure = { TaximeterIntentAction.ShowError }
            )
        }

    /**
     * Handles the `StartRide` intent by initiating a ride and starting updates.
     */
    private fun handleStartRide(intent: TaximeterIntent.StartRide): Flow<TaximeterIntentAction> =
        flow {
            emit(TaximeterIntentAction.Loading)
            startRideUseCase(intent.supplements)
                .onSuccess {
                    emit(TaximeterIntentAction.StartRide)
                    rideUpdateCoordinator.startRideUpdates()
                }
                .onFailure {
                    emit(TaximeterIntentAction.ShowError)
                }
        }

    /**
     * Handles the `UpdateSupplements` intent by updating ride supplements.
     */
    private fun handleUpdateSupplements(intent: TaximeterIntent.UpdateSupplements): Flow<TaximeterIntentAction> =
        flow {
            updateRideSupplementsUseCase(intent.supplements)
                .onFailure { emit(TaximeterIntentAction.ShowError) }
        }

    /**
     * Handles the `StopRide` intent by stopping ride updates and fetching the final fare summary.
     */
    private fun handleStopRide(): Flow<TaximeterIntentAction> =
        flow {
            rideUpdateCoordinator.stopRideUpdates()
            emit(TaximeterIntentAction.StopRide)
            getCurrentRideFareSummaryUseCase()
                .onSuccess { emit(TaximeterIntentAction.UpdateFareSummary(it)) }
                .onFailure { emit(TaximeterIntentAction.ShowError) }
        }

    /**
     * Maps the retrieved ride and supplements to an action to update the UI with fare details.
     */
    private suspend fun mapRideToUpdateFareAction(ride: Ride?): TaximeterIntentAction {
        val availableSupplementsResult = getSupplementsUseCase()
        return availableSupplementsResult.fold(
            onSuccess = { availableSupplements ->
                ride?.let {
                    TaximeterIntentAction.UpdateFare(
                        price = it.totalPrice,
                        elapsedTime = it.timeInSeconds,
                        supplements = availableSupplements.associateWith { supplement ->
                            ride.supplements.count { rideSupplement -> rideSupplement.id == supplement.id }
                        },
                        distance = it.distance,
                    )
                } ?: TaximeterIntentAction.UpdateFare(
                    price = 0.0,
                    distance = 0.0,
                    elapsedTime = 0,
                    supplements = availableSupplements.associateWith { 0 },
                )
            },
            onFailure = {
                TaximeterIntentAction.ShowError
            }
        )
    }

}
