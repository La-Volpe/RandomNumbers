package de.arjmandi.navvistask.numberdatasource.data.mock

import io.ktor.client.plugins.ConnectTimeoutException
import kotlinx.coroutines.delay
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.random.Random

class NetworkSimulator {

    private val random = Random

    suspend fun simulateNetworkResponse(mode: NetworkMode): NetworkResponse {
        delay(random.nextLong(100, 1500)) // Simulated delay (100ms to 1.5s)

        return when (mode) {
            NetworkMode.STABLE -> {
                val numbers = generateRandomNumbers(includeInvalidNumbers = false)
                NetworkResponse.StableConnection(numbers)
            }
            NetworkMode.STABLE_WITH_MALFORMED -> {
                if (random.nextBoolean()) {
                    // 50% chance to return a valid response
                    val numbers = generateRandomNumbers(includeInvalidNumbers = true)
                    NetworkResponse.StableConnectionWithMalformedResponse(numbers)
                } else {
                    // 50% chance to return a JSON parsing error
                    throw SerializationException("Malformed JSON received")
                }
            }
            NetworkMode.FLAKY -> {
                if (random.nextBoolean()) {
                    // 50% chance of a network timeout
                    delay(3000)
                    throw ConnectTimeoutException("Connection Timeout", 3000)
                } else {
                    // 50% chance of an incomplete response
                    NetworkResponse.FlakyConnection("""{"numbers": [""")
                }
            }
            NetworkMode.NO_CONNECTION -> {
                if (random.nextBoolean()) {
                    // 50% chance of an actual offline scenario
                    throw IOException("Simulated network failure")
                } else {
                    // 50% chance of an empty response
                    NetworkResponse.NoConnection(emptyList())
                }
            }
        }
    }

    private fun generateRandomNumbers(includeInvalidNumbers: Boolean): List<Int> {
        val size = random.nextInt(1, 20)
        return List(size) {
            if (includeInvalidNumbers && random.nextBoolean()) {
                random.nextInt(-100, 356)
            } else {
                random.nextInt(0, 256)
            }
        }
    }
}

/**
 * Represents the mode of the network simulation.
 */
enum class NetworkMode {
    STABLE, STABLE_WITH_MALFORMED, NO_CONNECTION, FLAKY
}