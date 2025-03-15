package de.arjmandi.navvistask.numberdatasource.data.mock

import de.arjmandi.navvistask.numberdatasource.data.remote.ApiException
import de.arjmandi.navvistask.numberdatasource.domain.mock.RandomSimulator
import de.arjmandi.navvistask.numberdatasource.domain.model.ParsedNumber
import io.ktor.client.plugins.ConnectTimeoutException
import kotlinx.coroutines.delay
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

class NetworkSimulator(
    private val random: RandomSimulator,
) {
    suspend fun simulateNetworkResponse(mode: NetworkMode): NetworkResponse {
        delay(random.randomDelay())

        return when (mode) {
            NetworkMode.STABLE -> {
                val numbers = generateRandomNumbers(includeInvalidNumbers = false)
                NetworkResponse.StableConnection(numbers)
            }
            NetworkMode.STABLE_WITH_MALFORMED -> {
                when(random.randomOneThirdChance()){
                    0 -> {
                        // 33.33% chance to return a "valid" response. (i.e: all integer numbers)
                        val numbers = generateRandomNumbers(includeInvalidNumbers = true)
                        NetworkResponse.StableConnectionWithMalformedResponse(numbers)
                    }
                    1 -> {
                        //33.33% chance to return an empty response (i.e: a backend error)
                        NetworkError.MalformedResponse("")
                        NetworkResponse.StableConnectionWithMalformedResponse(emptyList<Int>())
                        throw ApiException("An empty response was retrieved!")
                    }
                    else -> {
                        //33.33% chance to return an outright invalid response. (i.e: an string in the middle of integer numbers)
                        val numbers = generateRandomNumbers(includeInvalidNumbers = true)
                        val responseList = listOf("HSV", "FCB", 2, true)
                        NetworkResponse.StableConnectionWithMalformedResponse(numbers.plus(responseList))
                        throw ApiException("Illegal items in the retrieved list!")
                    }
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
                NetworkResponse.NoConnection(emptyList())
                throw IOException("Simulated network failure")
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
