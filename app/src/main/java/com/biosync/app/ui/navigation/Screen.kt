package com.biosync.app.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Tasks : Screen("tasks")
    data object AddTask : Screen("add_task")
    data object EditTask : Screen("edit_task/{taskId}") {
        fun createRoute(taskId: Long) = "edit_task/$taskId"
    }
    data object Stats : Screen("stats")
}
