package de.arjmandi.random_numbers.numberdatasource.domain.extensions

import de.arjmandi.random_numbers.numberdatasource.domain.model.ParsedNumber

fun List<ParsedNumber>.groupAndSort(): List<ParsedNumber> {
    return this
        .groupBy { it.sectionIndex }
        .toSortedMap()
        .flatMap { (_, items) -> items.sortedBy { it.itemValue } } // Sort within each section
}
