package de.arjmandi.navvistask.ui.number_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.arjmandi.navvistask.numberdatasource.data.mock.NetworkMode
import de.arjmandi.navvistask.numberdatasource.domain.model.ParsedNumber
import de.arjmandi.navvistask.ui.UiState
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun NumberListScreen(viewModel: NumberViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val fallbackState by viewModel.fallbackModeState.collectAsState()

    Column(modifier = Modifier.padding(8.dp)) {
        Text("Connection Mode:")
        ConnectionDropdownMenu(fallbackState, viewModel.onItemSelected)

        when (val state = uiState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp),
                ) {
                    Button(onClick = { viewModel.refreshNumbers() }) {
                        Text("Get New Numbers")
                    }
                    NumberList(parsedNumbers = state.parsedNumbers)
                }
            }

            is UiState.Error -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.refreshNumbers() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun NumberList(parsedNumbers: List<ParsedNumber>) {
    // Group numbers by section index
    val groupedNumbers = parsedNumbers.groupBy { it.sectionIndex }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        groupedNumbers.forEach { (sectionIndex, numbers) ->
            // Sort numbers by item value
            val sortedNumbers = numbers.sortedBy { it.itemValue }

            // Add section header
            item {
                Text(
                    text = "SECTION${sectionIndex + 1}",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }

            // Add items for this section
            items(sortedNumbers.size) { parsedNumber ->
                NumberItem(parsedNumber = sortedNumbers[parsedNumber])
            }
        }
    }
}

@Composable
fun NumberItem(parsedNumber: ParsedNumber) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "Item${parsedNumber.itemValue + 1}")

        // Use a CheckBox instead of "√"
        Checkbox(
            checked = parsedNumber.itemCheckmark,
            onCheckedChange = null, // Disable interaction since the state is read-only
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}
@Composable
fun ConnectionDropdownMenu(networkMode: NetworkMode, onItemSelected: (networkMode: NetworkMode) -> Unit) {
    val options = listOf(
        NetworkMode.NO_CONNECTION,
        NetworkMode.FLAKY,
        NetworkMode.STABLE_WITH_MALFORMED,
        NetworkMode.STABLE
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(networkMode) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedOption.name)
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Dropdown Icon",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        selectedOption = option
                        onItemSelected(option)
                        expanded = false
                    }
                )
            }

        }
    }
}