package de.arjmandi.navvistask.numberdatasource.data.repository

import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkError
import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkMode
import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkSimulator
import de.arjmandi.navvistask.numberdatasource.data.remote.ApiClient
import de.arjmandi.navvistask.numberdatasource.domain.model.NumbersResponse
import de.arjmandi.navvistask.numberdatasource.domain.repository.NumbersRepository

class NumbersRepositoryImpl(
    private val apiClient: ApiClient,
    private val networkSimulator: NetworkSimulator
) : NumbersRepository {
    val fallbackNetworkMode = NetworkMode.STABLE

    override suspend fun fetchNumbers(): NumbersResponse {
        val response = apiClient.fetchNumbers()
        return when (response.error) {
            NetworkError.None -> response
            is NetworkError.FlakyResponse -> {
                print(response.error.message)
                networkSimulator.simulateNetworkResponse(fallbackNetworkMode).toNumbersResponse()
            }
            is NetworkError.MalformedResponse -> {
                print(response.error.message)
                networkSimulator.simulateNetworkResponse(fallbackNetworkMode).toNumbersResponse()
            }

            is NetworkError.NoConnection -> {
                print(response.error.message)
                networkSimulator.simulateNetworkResponse(fallbackNetworkMode).toNumbersResponse()
            }

            is NetworkError.Timeout -> {
                print(response.error.message)
                networkSimulator.simulateNetworkResponse(fallbackNetworkMode).toNumbersResponse()
            }

            else -> {
                print(NetworkError.UnknownError().message)
                networkSimulator.simulateNetworkResponse(fallbackNetworkMode).toNumbersResponse()
            }
        }
    }
}