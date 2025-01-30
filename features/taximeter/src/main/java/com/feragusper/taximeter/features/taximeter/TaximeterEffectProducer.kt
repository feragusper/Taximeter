package com.feragusper.taximeter.features.taximeter

import com.feragusper.taximeter.libraries.mvi.EffectProducer

/**
 * Handles side effects triggered by TaximeterIntentActions.
 * Only specific actions, such as errors, produce side effects.
 */
internal class TaximeterEffectProducer :
    EffectProducer<TaximeterIntentAction, TaximeterViewState, TaximeterSideEffect> {

    override fun invoke(
        action: TaximeterIntentAction,
        uiState: TaximeterViewState,
    ): TaximeterSideEffect? {
        return when (action) {
            is TaximeterIntentAction.ShowError -> {
                // Produces an error side effect to notify the UI layer
                TaximeterSideEffect.ShowError
            }

            is TaximeterIntentAction.Loading,
            is TaximeterIntentAction.UpdateFareSummary,
            is TaximeterIntentAction.UpdateFare,
            is TaximeterIntentAction.StartRide,
            is TaximeterIntentAction.StopRide -> {
                // These actions do not trigger any side effects
                null
            }
        }
    }
}
