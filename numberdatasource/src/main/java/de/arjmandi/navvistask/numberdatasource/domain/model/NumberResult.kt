package de.arjmandi.navvistask.numberdatasource.domain.model

sealed class NumberResult {
    data class Success(
        val parsedNumbers: List<ParsedNumber>,
    ) : NumberResult()

    data class Error(
        val message: String,
    ) : NumberResult()
}
