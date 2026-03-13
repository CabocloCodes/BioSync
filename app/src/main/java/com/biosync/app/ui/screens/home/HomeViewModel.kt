package com.biosync.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biosync.app.data.model.EnergyLevel
import com.biosync.app.data.model.Task
import com.biosync.app.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val selectedEnergyLevel: EnergyLevel? = null,
    val suggestedTasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val showEnergySelector: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun selectEnergyLevel(energyLevel: EnergyLevel) {
        _uiState.value = _uiState.value.copy(
            selectedEnergyLevel = energyLevel,
            showEnergySelector = false,
            isLoading = true
        )
        loadSuggestedTasks(energyLevel)
    }

    fun resetEnergySelection() {
        _uiState.value = HomeUiState()
    }

    private fun loadSuggestedTasks(energyLevel: EnergyLevel) {
        viewModelScope.launch {
            taskRepository.getTasksForEnergyLevel(energyLevel).collect { tasks ->
                _uiState.value = _uiState.value.copy(
                    suggestedTasks = tasks,
                    isLoading = false
                )
            }
        }
    }

    fun completeTask(taskId: Long) {
        viewModelScope.launch {
            taskRepository.completeTask(taskId)
        }
    }
}
