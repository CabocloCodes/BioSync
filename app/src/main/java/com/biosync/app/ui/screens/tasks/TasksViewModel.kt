package com.biosync.app.ui.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biosync.app.data.model.MentalEffort
import com.biosync.app.data.model.Task
import com.biosync.app.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TasksUiState(
    val pendingTasks: List<Task> = emptyList(),
    val completedTasks: List<Task> = emptyList(),
    val showCompleted: Boolean = false,
    val filterEffort: MentalEffort? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            combine(
                taskRepository.getPendingTasks(),
                taskRepository.getCompletedTasks()
            ) { pending, completed ->
                Pair(pending, completed)
            }.collect { (pending, completed) ->
                val filtered = _uiState.value.filterEffort?.let { effort ->
                    pending.filter { it.mentalEffort == effort }
                } ?: pending

                _uiState.value = _uiState.value.copy(
                    pendingTasks = filtered,
                    completedTasks = completed,
                    isLoading = false
                )
            }
        }
    }

    fun toggleShowCompleted() {
        _uiState.value = _uiState.value.copy(
            showCompleted = !_uiState.value.showCompleted
        )
    }

    fun setFilterEffort(effort: MentalEffort?) {
        _uiState.value = _uiState.value.copy(filterEffort = effort)
        loadTasks()
    }

    fun completeTask(taskId: Long) {
        viewModelScope.launch {
            taskRepository.completeTask(taskId)
        }
    }

    fun uncompleteTask(taskId: Long) {
        viewModelScope.launch {
            taskRepository.uncompleteTask(taskId)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }
}
