package com.biosync.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.biosync.app.ui.screens.addtask.AddEditTaskScreen
import com.biosync.app.ui.screens.home.HomeScreen
import com.biosync.app.ui.screens.stats.StatsScreen
import com.biosync.app.ui.screens.tasks.TasksScreen

@Composable
fun BioSyncNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToTasks = { navController.navigate(Screen.Tasks.route) },
                onNavigateToAddTask = { navController.navigate(Screen.AddTask.route) },
                onNavigateToStats = { navController.navigate(Screen.Stats.route) }
            )
        }

        composable(Screen.Tasks.route) {
            TasksScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddTask = { navController.navigate(Screen.AddTask.route) },
                onNavigateToEditTask = { taskId ->
                    navController.navigate(Screen.EditTask.createRoute(taskId))
                }
            )
        }

        composable(Screen.AddTask.route) {
            AddEditTaskScreen(
                taskId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditTask.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId")
            AddEditTaskScreen(
                taskId = taskId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Stats.route) {
            StatsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
