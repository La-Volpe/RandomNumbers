package de.arjmandi.navvistask.numberdatasource.domain.parser

import de.arjmandi.navvistask.numberdatasource.domain.model.ParsedNumber

interface NumberParser {
    fun isValidNumber(number: Int): Boolean
    fun parseNumber(number: Int): ParsedNumber
}