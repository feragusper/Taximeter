package com.feragusper.taximeter.libraries.ride.data.datasource

import com.feragusper.taximeter.libraries.ride.domain.model.Supplement
import java.util.UUID

/**
 * Implementation of [SupplementDataSource] that provides a predefined list of available supplements.
 *
 * This class simulates fetching supplement data, which in a real-world scenario
 * could come from a remote API or local database.
 */
class SupplementDataSourceImpl : SupplementDataSource {

    private val supplements = listOf(
        Supplement(UUID.randomUUID(), "Extra baggage", 5.0),
    )

    /**
     * Retrieves a list of available supplements.
     *
     * @param uuids Optional list of UUIDs to filter the supplements by.
     * @return The list of supplements corresponding to the given UUIDs, including duplicates.
     */
    override fun getSupplements(uuids: List<UUID>?): List<Supplement> {
        return uuids?.mapNotNull { id ->
            supplements.find { it.id == id }
        } ?: supplements
    }
}
