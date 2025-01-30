package com.feragusper.taximeter.features.taximeter

import com.feragusper.taximeter.libraries.mvi.StateMapper

/**
 * Maps incoming [TaximeterIntentAction] to updated [TaximeterViewState].
 */
internal class TaximeterStateMapper : StateMapper<TaximeterIntentAction, TaximeterViewState> {
    override fun invoke(
        action: TaximeterIntentAction,
        lastUiState: TaximeterViewState,
    ): TaximeterViewState =
        when (action) {
            // Updates fare, elapsed time, and supplements in the view state
            is TaximeterIntentAction.UpdateFare ->
                lastUiState.copy(
                    isLoading = false,
                    fare = action.price.formatCurrency(),
                    elapsedTime = action.elapsedTime.formatElapsedTime(),
                    supplements = action.supplements.map { (supplement, count) ->
                        TaximeterViewState.Supplement(
                            id = supplement.id,
                            name = supplement.name,
                            count = count
                        )
                    },
                    distance = action.distance.formatDistance(),
                )

            TaximeterIntentAction.Loading ->
                lastUiState.copy(isLoading = true)

            TaximeterIntentAction.StartRide ->
                lastUiState.copy(
                    inProgress = true,
                    priceBreakdown = null,
                )

            // Updates the price breakdown with new fare summary
            is TaximeterIntentAction.UpdateFareSummary ->
                lastUiState.copy(
                    priceBreakdown = TaximeterViewState.PriceBreakdown(
                        items = action.fareSummary.concepts.map { item ->
                            TaximeterViewState.PriceBreakdown.Item(
                                concept = item.name,
                                price = item.price.formatCurrency()
                            )
                        },
                        total = action.fareSummary.total.formatCurrency()
                    )
                )

            // Handles error by stopping loading and maintaining the previous state
            is TaximeterIntentAction.ShowError ->
                lastUiState.copy(isLoading = false)

            TaximeterIntentAction.StopRide ->
                lastUiState.copy(inProgress = false)
        }
}
