package de.arjmandi.random_numbers.numberdatasource.data.mock

import de.arjmandi.random_numbers.numberdatasource.domain.model.NumbersResponse

sealed class NetworkResponse {
    data class StableConnection(
        val numbers: List<Any>,
    ) : NetworkResponse()

    data class StableConnectionWithMalformedResponse(
        val numbers: List<Any>,
    ) : NetworkResponse()

    data class FlakyConnection(
        val partialResponse: String,
    ) : NetworkResponse()

    data class NoConnection(
        val connectionError: List<Any>,
    ) : NetworkResponse()

    fun toNumbersResponse(): NumbersResponse =
        when (this) {
            is StableConnection -> {
                NumbersResponse(numbers.filterIsInstance<Int>())
            }
            is StableConnectionWithMalformedResponse -> {
                if (numbers.isNotEmpty()) {
                    NumbersResponse(numbers.filterIsInstance<Int>())
                } else {
                    NumbersResponse(emptyList<Int>(), error = NetworkError.MalformedResponse())
                }
            }
            is FlakyConnection -> NumbersResponse(emptyList<Int>(), error = NetworkError.FlakyResponse())
            is NoConnection -> NumbersResponse(emptyList<Int>(), error = NetworkError.NoConnection())
        }
}
