package com.gondroid.randomchallengeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.gondroid.randomchallengeapp.presentation.screens.home.HomeDataState
import com.gondroid.randomchallengeapp.presentation.screens.home.HomeScreen
import com.gondroid.randomchallengeapp.presentation.screens.home.HomeScreenRoot
import com.gondroid.randomchallengeapp.presentation.screens.home.providers.HomeScreenPreviewProvider
import com.gondroid.randomchallengeapp.ui.theme.RandomChallengeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomChallengeAppTheme {

                val previewProvider = HomeScreenPreviewProvider()
                val state = HomeDataState(
                    date = previewProvider.values.first().date,
                    summary = previewProvider.values.first().summary,
                    completedTask = previewProvider.values.first().completedTask,
                    pendingTask = previewProvider.values.first().pendingTask
                )
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreenRoot()
                }
            }
        }
    }
}

