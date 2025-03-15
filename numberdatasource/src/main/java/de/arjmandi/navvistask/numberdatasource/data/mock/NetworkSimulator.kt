package de.arjmandi.navvistask.numberdatasource.data.mock

import de.arjmandi.navvistask.numberdatasource.domain.mock.RandomSimulator
import io.ktor.client.plugins.ConnectTimeoutException
import kotlinx.coroutines.delay
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

class NetworkSimulator(private val random: RandomSimulator) {


    suspend fun simulateNetworkResponse(mode: NetworkMode): NetworkResponse {
        delay(random.randomDelay())

        return when (mode) {
            NetworkMode.STABLE -> {
                val numbers = generateRandomNumbers(includeInvalidNumbers = false)
                NetworkResponse.StableConnection(numbers)
            }
            NetworkMode.STABLE_WITH_MALFORMED -> {
                if (random.randomBoolean()) {
                    // 50% chance to return a "valid" response. (i.e: all integer numbers)
                    val numbers = generateRandomNumbers(includeInvalidNumbers = true)
                    NetworkResponse.StableConnectionWithMalformedResponse(numbers)
                } else {
                    NetworkResponse.StableConnectionWithMalformedResponse(emptyList())
                    // 50% chance to return a JSON parsing error (i.e: an string in the middle of the response json)
                    throw SerializationException("Malformed JSON received")
                }
            }
            NetworkMode.FLAKY -> {
                if (random.randomBoolean()) {
                    // 50% chance of a network timeout
                    delay(3000)
                    NetworkResponse.FlakyConnection("")
                    throw ConnectTimeoutException("Connection Timeout", 3000)
                } else {
                    // 50% chance of an incomplete response
                    NetworkResponse.FlakyConnection("""{"numbers": [""")
                    throw SerializationException("Likely incomplete json.")
                }
            }
            NetworkMode.NO_CONNECTION -> {
                if (random.randomBoolean()) {
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
        val size = random.randomListLength()
        return List(size) {
            if (includeInvalidNumbers && random.randomBoolean()) {
                random.randomInvalidNumber()
            } else {
                random.randomValidNumber()
            }
        }
    }
}