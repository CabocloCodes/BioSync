package com.biosync.app.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biosync.app.data.model.MentalEffort
import com.biosync.app.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StatsUiState(
    val completedLow: Int = 0,
    val completedMedium: Int = 0,
    val completedHigh: Int = 0,
    val totalCompleted: Int = 0,
    val completedToday: Int = 0,
    val completedThisWeek: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val todayStart = now - (now % (24 * 60 * 60 * 1000))
            val weekStart = todayStart - (6 * 24 * 60 * 60 * 1000)

            combine(
                taskRepository.getCompletedCountByEffort(MentalEffort.LOW),
                taskRepository.getCompletedCountByEffort(MentalEffort.MEDIUM),
                taskRepository.getCompletedCountByEffort(MentalEffort.HIGH),
                taskRepository.getCompletedCountSince(todayStart),
                taskRepository.getCompletedCountSince(weekStart)
            ) { low, medium, high, today, week ->
                StatsUiState(
                    completedLow = low,
                    completedMedium = medium,
                    completedHigh = high,
                    totalCompleted = low + medium + high,
                    completedToday = today,
                    completedThisWeek = week,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}
