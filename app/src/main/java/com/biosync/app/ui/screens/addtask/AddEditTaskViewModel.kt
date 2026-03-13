package com.biosync.app.ui.screens.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biosync.app.data.model.MentalEffort
import com.biosync.app.data.model.Task
import com.biosync.app.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditTaskUiState(
    val title: String = "",
    val description: String = "",
    val mentalEffort: MentalEffort = MentalEffort.MEDIUM,
    val tags: List<String> = emptyList(),
    val currentTag: String = "",
    val dueDate: Long? = null,
    val priority: Int = 0,
    val isEditing: Boolean = false,
    val editingTaskId: Long = 0,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditTaskUiState())
    val uiState: StateFlow<AddEditTaskUiState> = _uiState.asStateFlow()

    fun loadTask(taskId: Long) {
        viewModelScope.launch {
            taskRepository.getTaskById(taskId)?.let { task ->
                _uiState.value = _uiState.value.copy(
                    title = task.title,
                    description = task.description,
                    mentalEffort = task.mentalEffort,
                    tags = task.tags,
                    dueDate = task.dueDate,
                    priority = task.priority,
                    isEditing = true,
                    editingTaskId = task.id
                )
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title, errorMessage = null)
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun updateMentalEffort(effort: MentalEffort) {
        _uiState.value = _uiState.value.copy(mentalEffort = effort)
    }

    fun updateCurrentTag(tag: String) {
        _uiState.value = _uiState.value.copy(currentTag = tag)
    }

    fun addTag() {
        val tag = _uiState.value.currentTag.trim()
        if (tag.isNotEmpty() && tag !in _uiState.value.tags) {
            _uiState.value = _uiState.value.copy(
                tags = _uiState.value.tags + tag,
                currentTag = ""
            )
        }
    }

    fun removeTag(tag: String) {
        _uiState.value = _uiState.value.copy(
            tags = _uiState.value.tags.filter { it != tag }
        )
    }

    fun updateDueDate(dueDate: Long?) {
        _uiState.value = _uiState.value.copy(dueDate = dueDate)
    }

    fun updatePriority(priority: Int) {
        _uiState.value = _uiState.value.copy(priority = priority)
    }

    fun saveTask() {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Task title is required")
            return
        }

        viewModelScope.launch {
            if (state.isEditing) {
                val existingTask = taskRepository.getTaskById(state.editingTaskId)
                existingTask?.let {
                    taskRepository.updateTask(
                        it.copy(
                            title = state.title,
                            description = state.description,
                            mentalEffort = state.mentalEffort,
                            tags = state.tags,
                            dueDate = state.dueDate,
                            priority = state.priority
                        )
                    )
                }
            } else {
                taskRepository.insertTask(
                    Task(
                        title = state.title,
                        description = state.description,
                        mentalEffort = state.mentalEffort,
                        tags = state.tags,
                        dueDate = state.dueDate,
                        priority = state.priority
                    )
                )
            }
            _uiState.value = state.copy(isSaved = true)
        }
    }
}
