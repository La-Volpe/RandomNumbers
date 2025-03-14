package de.arjmandi.navvistask.numberdatasource.parser

import de.arjmandi.navvistask.numberdatasource.domain.model.ParsedNumber
import de.arjmandi.navvistask.numberdatasource.domain.parser.NumberParser

class NumberParserImpl: NumberParser {
    override fun isValidNumber(number: Int): Boolean {
        val valid = number in 0..255
        if (valid)
            println("input $number is valid")
        else
            println("input $number is not valid")
        return valid
    }

    override fun parseNumber(number: Int): ParsedNumber {
        if (!isValidNumber(number)) {
            throw IllegalArgumentException("Invalid number. Number must be between 0 and 255.")
        }
        return ParsedNumber(
            sectionIndex = (number and 0b11),
            itemValue = (number shr 2) and 0b11111,
            itemCheckmark = (number >= 0b10000000))
    }
}