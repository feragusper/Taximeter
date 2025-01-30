package com.feragusper.taximeter.libraries.location.data.datasource

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

object LocationProvider {

    private const val ONE_SEC_IN_MILLIS = 1000L

    fun getRouteFlow(): Flow<LocationPointEntity> = flow {
        var latitude = 37.7749 // Coordenada base (San Francisco)
        var longitude = -122.4194
        val step = 0.0001 // Pequeño desplazamiento por iteración

        repeat(100) {
            val timestamp = System.currentTimeMillis()
            val point = LocationPointEntity(
                lat = latitude,
                long = longitude,
                timestamp = timestamp
            )

            emit(point)

            latitude += step + Random.nextDouble(-0.00005, 0.00005)
            longitude += step + Random.nextDouble(-0.00005, 0.00005)

            delay(ONE_SEC_IN_MILLIS)
        }
    }
}
