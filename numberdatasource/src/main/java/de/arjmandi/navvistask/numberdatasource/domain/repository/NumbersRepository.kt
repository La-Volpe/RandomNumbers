package de.arjmandi.navvistask.numberdatasource.domain.repository

import de.arjmandi.navvistask.numberdatasource.domain.model.NumbersResponse

interface NumbersRepository {
    suspend fun fetchNumbers(): NumbersResponse
}
