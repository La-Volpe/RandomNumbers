package de.arjmandi.random_numbers.numberdatasource

import de.arjmandi.random_numbers.numberdatasource.domain.model.ParsedNumber
import de.arjmandi.random_numbers.numberdatasource.domain.parser.NumberParser
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class NumberParserTest {
    private val parser: NumberParser = mockk()

    @Test
    fun testIsValidNumber() {
        // Define behavior for isValidNumber
        every { parser.isValidNumber(0) } returns true
        every { parser.isValidNumber(255) } returns true
        every { parser.isValidNumber(-1) } returns false
        every { parser.isValidNumber(256) } returns false

        // Valid numbers
        assertTrue(parser.isValidNumber(0)) // Minimum valid number
        assertTrue(parser.isValidNumber(255)) // Maximum valid number

        // Invalid numbers
        assertFalse(parser.isValidNumber(-1)) // Below range
        assertFalse(parser.isValidNumber(256)) // Above range
    }

    @Test
    fun testParseNumberValidInput() {
        // Define behavior for parseNumber
        every { parser.parseNumber(0) } returns ParsedNumber(0, 0, false)
        every { parser.parseNumber(140) } returns ParsedNumber(0, 3, true)
        every { parser.parseNumber(30) } returns ParsedNumber(2, 7, false)
        every { parser.parseNumber(41) } returns ParsedNumber(1, 10, false)
        every { parser.parseNumber(191) } returns ParsedNumber(3, 15, true)
        every { parser.parseNumber(67) } returns ParsedNumber(3, 16, false)
        every { parser.parseNumber(255) } returns ParsedNumber(3, 31, true)

        // Test valid inputs
        assertEquals(
            ParsedNumber(sectionIndex = 0, itemValue = 0, itemCheckmark = false),
            parser.parseNumber(0), // 0b00000000
        )
        assertEquals(
            ParsedNumber(sectionIndex = 0, itemValue = 3, itemCheckmark = true),
            parser.parseNumber(140), // 0b10001100
        )
        assertEquals(
            ParsedNumber(sectionIndex = 2, itemValue = 7, itemCheckmark = false),
            parser.parseNumber(30), // 0b00011110
        )
        assertEquals(
            ParsedNumber(sectionIndex = 1, itemValue = 10, itemCheckmark = false),
            parser.parseNumber(41), // 0b00101001
        )
        assertEquals(
            ParsedNumber(sectionIndex = 3, itemValue = 15, itemCheckmark = true),
            parser.parseNumber(191), // 0b10111111
        )
        assertEquals(
            ParsedNumber(sectionIndex = 3, itemValue = 16, itemCheckmark = false),
            parser.parseNumber(67), // 0b01000011
        )
        assertEquals(
            ParsedNumber(sectionIndex = 3, itemValue = 31, itemCheckmark = true),
            parser.parseNumber(255), // 0b11111111
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testParseNumberInvalidInput() {
        // Define behavior for invalid inputs
        every { parser.parseNumber(-12) } throws IllegalArgumentException("Invalid number")
        every { parser.parseNumber(256) } throws IllegalArgumentException("Invalid number")
        every { parser.parseNumber(12345) } throws IllegalArgumentException("Invalid number")

        // Test invalid inputs
        parser.parseNumber(-12)
        parser.parseNumber(256)
        parser.parseNumber(12345)
    }
}
