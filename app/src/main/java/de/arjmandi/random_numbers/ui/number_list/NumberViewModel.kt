package de.arjmandi.random_numbers.ui.number_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.arjmandi.random_numbers.numberdatasource.NumberDataSource
import de.arjmandi.random_numbers.numberdatasource.data.mock.NetworkMode
import de.arjmandi.random_numbers.numberdatasource.domain.model.NumberResult
import de.arjmandi.random_numbers.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NumberViewModel(
    private val numberDataSource: NumberDataSource,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _fallbackModeState = MutableStateFlow<NetworkMode>(NetworkMode.STABLE)
    val fallbackModeState: MutableStateFlow<NetworkMode> = _fallbackModeState

    init {
        viewModelScope.launch {
            fallbackModeState.collectLatest { mode ->
                fetchNumbers(mode)
            }
        }

    }

    private suspend fun fetchNumbers(fallbackMode: NetworkMode) {
        _uiState.value = UiState.Loading
        when (val result = numberDataSource.fetchParsedNumbers(fallbackMode)) {
            is NumberResult.Success -> {
                _uiState.value = UiState.Success(result.parsedNumbers)
                Log.d(TAG, "fetchNumbers: ${result.parsedNumbers}")
            }
            is NumberResult.Error -> {
                _uiState.value = UiState.Error(result.message)
            }
        }
    }

    fun changeNetworkMode(fallbackMode: NetworkMode){

    }

    fun refreshNumbers() {
        viewModelScope.launch{
            _fallbackModeState.collectLatest {
                fetchNumbers(it)
            }
        }
    }

    val onItemSelected: (mode: NetworkMode) -> Unit = { mode ->
        _fallbackModeState.value = mode
    }

    companion object {
        const val TAG = "NumberViewModel"
    }
}
