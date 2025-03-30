package de.arjmandi.random_numbers.numberdatasource.domain.parser

import de.arjmandi.random_numbers.numberdatasource.domain.model.ParsedNumber

interface NumberParser {
    fun isValidNumber(number: Int): Boolean

    fun parseNumber(number: Int): ParsedNumber
}
