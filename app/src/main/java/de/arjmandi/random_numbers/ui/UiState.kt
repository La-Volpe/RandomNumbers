package de.arjmandi.random_numbers.ui

import de.arjmandi.random_numbers.numberdatasource.domain.model.ParsedNumber

sealed class UiState {
    object Loading : UiState()

    data class Success(
        val parsedNumbers: List<ParsedNumber>,
    ) : UiState()

    data class Error(
        val message: String,
    ) : UiState()
}
