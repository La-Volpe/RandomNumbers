package de.arjmandi.navvistask.numberdatasource.domain.model

import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkError
import kotlinx.serialization.Serializable

@Serializable
data class NumbersResponse(
    val numbers: List<Int>,
    val error: NetworkError = NetworkError.None
)