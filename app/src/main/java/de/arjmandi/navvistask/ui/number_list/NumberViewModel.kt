package de.arjmandi.navvistask.ui.number_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.arjmandi.navvistask.numberdatasource.NumberDataSource
import de.arjmandi.navvistask.numberdatasource.domain.model.NumberResult
import de.arjmandi.navvistask.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NumberViewModel(
    private val numberDataSource: NumberDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchNumbers()
    }

    private fun fetchNumbers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = numberDataSource.fetchParsedNumbers()) {
                is NumberResult.Success -> {
                    _uiState.value = UiState.Success(result.parsedNumbers)
                    Log.d(TAG, "fetchNumbers: ${result.parsedNumbers}")
                }
                is NumberResult.Error -> {
                    _uiState.value = UiState.Error(result.message)
                }
            }
        }
    }

    companion object {
        const val TAG = "NumberViewModel"
    }
}