package de.arjmandi.navvistask.numberdatasource

import de.arjmandi.navvistask.numberdatasource.domain.extensions.groupAndSort
import de.arjmandi.navvistask.numberdatasource.domain.model.NumberResult
import de.arjmandi.navvistask.numberdatasource.domain.parser.NumberParser
import de.arjmandi.navvistask.numberdatasource.domain.repository.NumbersRepository

//Normally there should be a use case or another layer to wrap the repository
//so it's not directly exposed. But it's not a terrible idea either.
class NumberDataSource(
    private val repository: NumbersRepository,
    private val numberParser: NumberParser
) {
    suspend fun fetchParsedNumbers(): NumberResult {
        return try {
            // Fetch the raw numbers from the repository
            val response = repository.fetchNumbers()

            // Parse the numbers into ParsedNumber objects
            val parsedNumbers = response.numbers.map { number ->
                numberParser.parseNumber(number)
            }

            // Return the success result
            NumberResult.Success(parsedNumbers.groupAndSort())
        } catch (e: Exception) {
            // Handle errors and return an error message
            NumberResult.Error("Failed to fetch numbers: ${e.message}")
        }
    }
}