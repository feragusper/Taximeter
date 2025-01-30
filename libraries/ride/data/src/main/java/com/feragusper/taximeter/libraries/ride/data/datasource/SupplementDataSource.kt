package com.feragusper.taximeter.libraries.ride.data.datasource

import com.feragusper.taximeter.libraries.ride.domain.model.Supplement
import java.util.UUID

/**
 * Data source interface for retrieving available ride supplements.
 *
 * This interface defines the contract for fetching a list of supplements
 * that can be added to a ride, such as extra baggage, baby seats, or
 * airport transfers.
 */
interface SupplementDataSource {

    /**
     * Retrieves a list of available supplements.
     *
     * @param uuids Optional list of UUIDs to filter the supplements by.
     * @return The list of available supplements.
     */
    fun getSupplements(uuids: List<UUID>? = null): List<Supplement>
}
