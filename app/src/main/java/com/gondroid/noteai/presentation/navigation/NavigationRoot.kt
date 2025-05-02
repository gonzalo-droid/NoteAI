package com.gondroid.noteai.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gondroid.noteai.presentation.screens.noteCreate.NoteCreateScreenRoot
import com.gondroid.noteai.presentation.screens.noteCreate.NoteCreateViewModel
import com.gondroid.noteai.presentation.screens.notes.NoteScreenViewModel
import com.gondroid.noteai.presentation.screens.notes.NotesScreenRoot
import com.gondroid.noteai.presentation.screens.task.TaskScreenRoot
import com.gondroid.noteai.presentation.screens.task.TaskScreenViewModel
import com.gondroid.noteai.presentation.screens.taskCreate.TaskCreateScreenRoot
import com.gondroid.noteai.presentation.screens.taskCreate.TaskCreateViewModel
import com.gondroid.noteai.presentation.screens.voiceRecorder.VoiceRecorderScreenRoot

@Composable
fun NavigationRoot(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = NoteScreenRoute
        ) {
            composable<NoteScreenRoute> {
                val viewmodel = hiltViewModel<NoteScreenViewModel>()
                NotesScreenRoot(
                    viewModel = viewmodel,
                    navigateTo = { noteId ->
                        navController.navigate(
                            NoteCreateScreenRoute(
                                noteId = noteId
                            )
                        )
                    }
                )
            }

            composable<NoteCreateScreenRoute> { backStackEntry ->

                val viewmodel = hiltViewModel<NoteCreateViewModel>()

                val navBackStackEntry = remember { navController.currentBackStackEntry }

                LaunchedEffect(navBackStackEntry) {
                    navBackStackEntry
                        ?.savedStateHandle
                        ?.get<String>("recordedFilePath")
                        ?.let { filePath ->
                            viewmodel.updateRecordedFilePath(filePath)
                        }
                }

                NoteCreateScreenRoot(
                    viewModel = viewmodel,
                    navigateBack = {
                        navController.navigateUp()
                    },
                    navigateToVoiceRecorder = {
                        navController.navigate(VoiceRecorderRoute)
                    },
                    navigateToMyTask = { nodeId ->
                        navController.navigate(TaskScreenRoute(noteId = nodeId))
                    }
                )
            }

            composable<TaskScreenRoute> {
                val viewmodel = hiltViewModel<TaskScreenViewModel>()
                TaskScreenRoot(
                    viewModel = viewmodel,
                    navigateBack = {
                        navController.navigateUp()
                    },
                    navigateToTaskCreateScreen = { taskId, noteId ->
                        navController.navigate(
                            TaskCreateScreenRoute(
                                taskId = taskId,
                                noteId = noteId
                            )
                        )
                    }
                )
            }

            composable<TaskCreateScreenRoute> {
                val viewmodel = hiltViewModel<TaskCreateViewModel>()
                TaskCreateScreenRoot(
                    viewModel = viewmodel,
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }

            composable<VoiceRecorderRoute> {
                VoiceRecorderScreenRoot(
                    navigateBack = {
                        navController.navigateUp()
                    },
                    onRecordingFinished = { filePath ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("recordedFilePath", filePath)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
