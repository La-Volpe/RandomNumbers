package de.arjmandi.random_numbers.numberdatasource.parser

import de.arjmandi.random_numbers.numberdatasource.domain.model.ParsedNumber
import de.arjmandi.random_numbers.numberdatasource.domain.parser.NumberParser

class NumberParserImpl : NumberParser {
    override fun isValidNumber(number: Int): Boolean {
        val valid = number in 0..255
        println("input $number is ${if (valid) "valid" else "not valid"}")
        return valid
    }

    override fun parseNumber(number: Int): ParsedNumber {
        if (!isValidNumber(number)) {
            throw IllegalArgumentException("Invalid number. Number must be between 0 and 255.")
        }
        return ParsedNumber(
            sectionIndex = number and 0b11, // Extracting the two least significant bits
            itemValue = (number shr 2) and 0b11111, // Extracting bits 2-6
            itemCheckmark = (number and 0b10000000) != 0, // Extracts the most significant bit
        )
    }
}
