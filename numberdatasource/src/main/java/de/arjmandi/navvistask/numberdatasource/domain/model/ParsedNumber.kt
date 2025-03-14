package de.arjmandi.navvistask.numberdatasource.domain.model

data class ParsedNumber(
    val sectionIndex: Int,
    val itemValue: Int,
    val itemCheckmark: Boolean
)