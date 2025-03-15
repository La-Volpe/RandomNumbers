package de.arjmandi.navvistask.numberdatasource.data.mock

import de.arjmandi.navvistask.numberdatasource.domain.model.NumbersResponse

sealed class NetworkResponse {
    data class StableConnection(val numbers: List<Int>) : NetworkResponse()
    data class StableConnectionWithMalformedResponse(val numbers: List<Int>) : NetworkResponse()
    data class FlakyConnection(val partialResponse: String) : NetworkResponse()
    data class NoConnection(val connectionError: List<Int>) : NetworkResponse()

    fun toNumbersResponse(): NumbersResponse {
        return when (this) {
            is StableConnection -> NumbersResponse(numbers)
            is StableConnectionWithMalformedResponse -> {
                if(numbers.isNotEmpty())
                    NumbersResponse(numbers)
                else NumbersResponse(emptyList(), error = NetworkError.MalformedResponse())
            }
            is FlakyConnection -> NumbersResponse(emptyList(), error = NetworkError.FlakyResponse())
            is NoConnection -> NumbersResponse(emptyList(), error = NetworkError.NoConnection())
        }
    }
}