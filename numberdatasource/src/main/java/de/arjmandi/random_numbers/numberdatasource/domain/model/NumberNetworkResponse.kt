package de.arjmandi.random_numbers.numberdatasource.domain.model

import de.arjmandi.random_numbers.numberdatasource.data.mock.NetworkError
import kotlinx.serialization.Serializable

@Serializable
data class NumbersResponse(
    val numbers: List<Int>,
    val error: NetworkError = NetworkError.None,
)
