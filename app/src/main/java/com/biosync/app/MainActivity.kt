package com.biosync.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.biosync.app.notifications.NotificationScheduler
import com.biosync.app.ui.navigation.BioSyncNavGraph
import com.biosync.app.ui.theme.BioSyncTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        NotificationScheduler.scheduleAll(this)

        setContent {
            BioSyncTheme {
                val navController = rememberNavController()
                BioSyncNavGraph(navController = navController)
            }
        }
    }
}
