package com.feragusper.taximeter.features.taximeter

import com.feragusper.taximeter.libraries.mvi.SideEffect
import com.feragusper.taximeter.libraries.mvi.UserIntent
import com.feragusper.taximeter.libraries.mvi.UserIntentAction
import com.feragusper.taximeter.libraries.mvi.ViewState
import com.feragusper.taximeter.libraries.ride.domain.model.FareSummary
import com.feragusper.taximeter.libraries.ride.domain.model.Supplement
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

/**
 * Represents the UI state for the Taximeter feature.
 */
internal data class TaximeterViewState(
    val isLoading: Boolean = true,  // Indicates if the screen is in loading state
    val supplements: List<Supplement> = emptyList(),  // List of available supplements
    val fare: String = 0.0.formatCurrency(),  // Total fare amount
    val inProgress: Boolean = false,  // Indicates if the ride is in progress
    val elapsedTime: String = 0L.formatElapsedTime(),  // Elapsed time in seconds
    val distance: String = 0.0.formatDistance(),  // Travelled distance in kilometers
    val priceBreakdown: PriceBreakdown? = null  // Detailed breakdown of the fare components
) : ViewState {

    /**
     * Represents a supplement that can be added to the ride.
     */
    data class Supplement(
        val id: UUID,  // Unique identifier for the supplement
        val name: String,  // Name of the supplement
        val count: Int  // Number of supplements selected
    )

    /**
     * Represents the detailed breakdown of the total fare.
     */
    data class PriceBreakdown(
        val items: List<Item>,  // List of price breakdown items
        val total: String  // Total price including all components
    ) {
        /**
         * Represents a single item in the fare breakdown.
         */
        data class Item(
            val concept: String,  // Description of the cost (e.g., "Distance", "Time")
            val price: String  // Cost associated with the concept
        )
    }

}

internal fun Double.formatCurrency(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    return formatter.format(this)
}

internal fun Double.formatDistance(): String {
    val formatter = DecimalFormat("#.##")
    return "${formatter.format(this)} km"
}

internal fun Long.formatElapsedTime(): String {
    val minutes = this / 60
    val seconds = this % 60
    return String.format(Locale.getDefault(), "%dm %ds", minutes, seconds)
}

/**
 * Represents possible side effects in the Taximeter feature.
 */
internal sealed interface TaximeterSideEffect : SideEffect {
    data object ShowError : TaximeterSideEffect  // Triggered when an error occurs
}

/**
 * Represents user intents that can be performed in the Taximeter feature.
 */
internal sealed interface TaximeterIntent : UserIntent {
    data object Open : TaximeterIntent  // Intent to open the taximeter screen

    /**
     * Intent to start a ride with selected supplements.
     *
     * @param supplements List of selected supplement identifiers
     */
    data class StartRide(val supplements: List<UUID>) : TaximeterIntent

    /**
     * Intent to update the selected supplements during the ride.
     *
     * @param supplements List of selected supplement identifiers
     */
    data class UpdateSupplements(val supplements: List<UUID>) :
        TaximeterIntent

    data object StopRide : TaximeterIntent  // Intent to stop an ongoing ride
}

/**
 * Represents actions that update the UI state of the taximeter.
 */
internal sealed interface TaximeterIntentAction : UserIntentAction {

    /**
     * Action to update the ride progress state.
     */
    data object StartRide : TaximeterIntentAction

    /**
     * Action to display the loading state.
     */
    data object Loading : TaximeterIntentAction

    /**
     * Action to update the fare details of the ride.
     */
    data class UpdateFare(
        val price: Double,  // Updated fare value
        val distance: Double,  // Updated distance traveled
        val elapsedTime: Long,  // Updated elapsed time in seconds
        val supplements: Map<Supplement, Int>  // Updated supplement counts
    ) : TaximeterIntentAction

    /**
     * Action to update the fare summary.
     */
    data class UpdateFareSummary(val fareSummary: FareSummary) : TaximeterIntentAction

    data object ShowError : TaximeterIntentAction  // Action to show an error message

    data object StopRide : TaximeterIntentAction  // Action to stop the ride
}
