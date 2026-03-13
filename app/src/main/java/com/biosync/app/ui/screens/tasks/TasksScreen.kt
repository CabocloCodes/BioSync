package com.biosync.app.ui.screens.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.biosync.app.data.model.MentalEffort
import com.biosync.app.ui.components.TaskCard
import com.biosync.app.ui.theme.Blue60
import com.biosync.app.ui.theme.DarkSurfaceVariant
import com.biosync.app.ui.theme.EffortHigh
import com.biosync.app.ui.theme.EffortLow
import com.biosync.app.ui.theme.EffortMedium
import com.biosync.app.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddTask: () -> Unit,
    onNavigateToEditTask: (Long) -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFilters by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Todas as Tarefas",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(
                            Icons.Filled.FilterList,
                            contentDescription = "Filter",
                            tint = if (uiState.filterEffort != null) Blue60 else TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTask,
                containerColor = Blue60,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filters
            AnimatedVisibility(visible = showFilters) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = uiState.filterEffort == null,
                        onClick = { viewModel.setFilterEffort(null) },
                        label = { Text("Todos") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Blue60.copy(alpha = 0.2f),
                            selectedLabelColor = Blue60
                        )
                    )
                    FilterChip(
                        selected = uiState.filterEffort == MentalEffort.LOW,
                        onClick = { viewModel.setFilterEffort(MentalEffort.LOW) },
                        label = { Text("Baixo") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EffortLow.copy(alpha = 0.2f),
                            selectedLabelColor = EffortLow
                        )
                    )
                    FilterChip(
                        selected = uiState.filterEffort == MentalEffort.MEDIUM,
                        onClick = { viewModel.setFilterEffort(MentalEffort.MEDIUM) },
                        label = { Text("Médio") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EffortMedium.copy(alpha = 0.2f),
                            selectedLabelColor = EffortMedium
                        )
                    )
                    FilterChip(
                        selected = uiState.filterEffort == MentalEffort.HIGH,
                        onClick = { viewModel.setFilterEffort(MentalEffort.HIGH) },
                        label = { Text("Alto") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EffortHigh.copy(alpha = 0.2f),
                            selectedLabelColor = EffortHigh
                        )
                    )
                }
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Blue60, strokeWidth = 3.dp)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Pending tasks section
                    if (uiState.pendingTasks.isNotEmpty()) {
                        item {
                            Text(
                                text = "Pendentes (${uiState.pendingTasks.size})",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        items(
                            items = uiState.pendingTasks,
                            key = { it.id }
                        ) { task ->
                            TaskCard(
                                task = task,
                                onComplete = { viewModel.completeTask(task.id) },
                                onEdit = { onNavigateToEditTask(task.id) },
                                onDelete = { viewModel.deleteTask(task) }
                            )
                        }
                    }

                    // Completed tasks section
                    if (uiState.completedTasks.isNotEmpty()) {
                        item {
                            TextButton(
                                onClick = { viewModel.toggleShowCompleted() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Concluídas (${uiState.completedTasks.size})",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextSecondary
                                    )
                                    Icon(
                                        if (uiState.showCompleted) Icons.Filled.ExpandLess
                                        else Icons.Filled.ExpandMore,
                                        contentDescription = null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        if (uiState.showCompleted) {
                            items(
                                items = uiState.completedTasks,
                                key = { it.id }
                            ) { task ->
                                TaskCard(
                                    task = task,
                                    onComplete = { viewModel.uncompleteTask(task.id) }
                                )
                            }
                        }
                    }

                    // Empty state
                    if (uiState.pendingTasks.isEmpty() && uiState.completedTasks.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Nenhuma tarefa ainda",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextSecondary,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "Toque no + para criar sua primeira tarefa",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}
