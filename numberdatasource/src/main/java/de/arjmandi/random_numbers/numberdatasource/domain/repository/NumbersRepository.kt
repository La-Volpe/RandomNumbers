package de.arjmandi.random_numbers.numberdatasource.domain.repository

import de.arjmandi.random_numbers.numberdatasource.data.mock.NetworkMode
import de.arjmandi.random_numbers.numberdatasource.domain.model.NumbersResponse

interface NumbersRepository {
    suspend fun fetchNumbers(): NumbersResponse
    fun setFallbackMode(mode: NetworkMode)
}
