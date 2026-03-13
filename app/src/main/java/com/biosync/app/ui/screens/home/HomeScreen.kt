package com.biosync.app.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.biosync.app.ui.components.EnergySelector
import com.biosync.app.ui.components.TaskCard
import com.biosync.app.ui.theme.AccentViolet
import com.biosync.app.ui.theme.Blue60
import com.biosync.app.ui.theme.DarkSurfaceVariant
import com.biosync.app.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    onNavigateToTasks: () -> Unit,
    onNavigateToAddTask: () -> Unit,
    onNavigateToStats: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (!uiState.showEnergySelector) {
                FloatingActionButton(
                    onClick = onNavigateToAddTask,
                    containerColor = Blue60,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Task")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "BioSync",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Produtividade adaptativa",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onNavigateToTasks) {
                        Icon(
                            Icons.Filled.List,
                            contentDescription = "All Tasks",
                            tint = TextSecondary
                        )
                    }
                    IconButton(onClick = onNavigateToStats) {
                        Icon(
                            Icons.Filled.BarChart,
                            contentDescription = "Statistics",
                            tint = TextSecondary
                        )
                    }
                }
            }

            // Content
            AnimatedVisibility(
                visible = uiState.showEnergySelector,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    EnergySelector(
                        onEnergySelected = { viewModel.selectEnergyLevel(it) }
                    )
                }
            }

            AnimatedVisibility(
                visible = !uiState.showEnergySelector,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Energy level header with reset button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Tarefas sugeridas",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            uiState.selectedEnergyLevel?.let { energy ->
                                Text(
                                    text = "Para ${energy.labelPt.lowercase()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AccentViolet
                                )
                            }
                        }

                        TextButton(onClick = { viewModel.resetEnergySelection() }) {
                            Icon(
                                Icons.Filled.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = TextSecondary
                            )
                            Text(
                                text = " Reavaliar",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Blue60,
                                modifier = Modifier.size(32.dp),
                                strokeWidth = 3.dp
                            )
                        }
                    } else if (uiState.suggestedTasks.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Nenhuma tarefa encontrada",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Adicione tarefas para receber sugestões personalizadas",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center
                                )
                                TextButton(onClick = onNavigateToAddTask) {
                                    Text("+ Criar tarefa")
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = uiState.suggestedTasks,
                                key = { it.id }
                            ) { task ->
                                TaskCard(
                                    task = task,
                                    onComplete = { viewModel.completeTask(task.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
