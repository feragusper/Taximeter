package com.feragusper.taximeter.libraries.ride.data.datasource

import com.feragusper.taximeter.libraries.ride.data.datasource.entity.PriceConfigurationEntity
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Implementation of [PriceDataSource] using Ktor and Kotlin Serialization for HTTP requests.
 */
class PriceDataSourceImpl : PriceDataSource {

    // Ktor HTTP client with Kotlin Serialization for JSON handling
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Ignore unknown JSON keys gracefully
                prettyPrint = true       // Format JSON output for debugging
                isLenient = true         // Allow more flexible parsing
            })
        }
    }

    companion object {
        private const val API_URL =
            "https://gist.githubusercontent.com/alhimaferagusper/a535399cd77a94f1b67e50d2d41258e1/raw/5bdccd0a0f060d23325d613fb513f942ad9315b0/priceconfig.json"

        // Default fallback price configuration in case of network failures
        private val defaultConfiguration = PriceConfigurationEntity(
            pricePerKm = 0.1,
            pricePerSecond = 0.027,
        )
    }

    /**
     * Fetches the current price configuration from the remote API.
     * Falls back to default configuration in case of any issues.
     *
     * @return [PriceConfigurationEntity] with the pricing details.
     */
    override suspend fun getPriceConfiguration(): PriceConfigurationEntity {
        return fetchTariffsFromEndpoint() ?: defaultConfiguration
    }

    /**
     * Executes an HTTP GET request to retrieve pricing configuration.
     *
     * @return A parsed [PriceConfigurationEntity] or null if the request fails.
     */
    private suspend fun fetchTariffsFromEndpoint(): PriceConfigurationEntity? {
        return try {
            val response: HttpResponse = client.get(API_URL) {
                header("Accept", "application/json")
            }

            println("Response status: ${response.status}")
            println("Response headers: ${response.headers}")
            println("Response body: ${response.bodyAsText()}")

            if (response.status == HttpStatusCode.OK) {
                // Manually parse the response from String to the expected data type
                Json.decodeFromString<PriceConfigurationEntity>(response.bodyAsText())
            } else {
                println("Failed to fetch data, HTTP Status: ${response.status}")
                null
            }
        } catch (e: Exception) {
            println("Error fetching price configuration: ${e.message}")
            null
        }
    }
}
