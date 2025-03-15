package de.arjmandi.navvistask.ui

import de.arjmandi.navvistask.numberdatasource.domain.model.ParsedNumber

sealed class UiState {
    object Loading : UiState()

    data class Success(
        val parsedNumbers: List<ParsedNumber>,
    ) : UiState()

    data class Error(
        val message: String,
    ) : UiState()
}
