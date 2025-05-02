package com.gondroid.noteai.presentation.screens.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.gondroid.noteai.domain.repository.TaskLocalDataSource
import com.gondroid.noteai.presentation.navigation.TaskScreenRoute
import com.gondroid.noteai.presentation.screens.task.TaskScreenAction.OnDeleteAllTasks
import com.gondroid.noteai.presentation.screens.task.TaskScreenAction.OnDeleteTask
import com.gondroid.noteai.presentation.screens.task.TaskScreenAction.OnToggleTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TaskScreenViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val taskLocalDataSource: TaskLocalDataSource
) : ViewModel() {
    var state by mutableStateOf(TaskDataState())
        private set

    private val eventChannel = Channel<TaskScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    private val noteData = savedStateHandle.toRoute<TaskScreenRoute>()

    init {

        state =
            state.copy(
                date =
                LocalDate.now().let {
                    DateTimeFormatter.ofPattern("EEEE, MMMM dd yyyy").format(it)
                }
            )

        noteData.noteId?.let {
            state = state.copy(noteId = noteData.noteId)

            taskLocalDataSource.tasksFlow
                .onEach { tasks ->
                    val taskFiler = tasks.filter { it.noteId == noteData.noteId }
                    val completedTasks =
                        taskFiler
                            .filter { task -> task.isCompleted }
                            .sortedByDescending { task ->
                                task.date
                            }
                    val pendingTasks =
                        taskFiler
                            .filter { task ->
                                !task.isCompleted
                            }.sortedByDescending { task ->
                                task.date
                            }

                    state =
                        state.copy(
                            summary = pendingTasks.size.toString(),
                            completedTask = completedTasks,
                            pendingTask = pendingTasks
                        )
                }.launchIn(viewModelScope)
        }
    }

    fun onAction(action: TaskScreenAction) {
        viewModelScope.launch {
            when (action) {
                is OnDeleteTask -> {
                    taskLocalDataSource.removeTask(action.task)
                    eventChannel.send(TaskScreenEvent.DeletedTask)
                }

                is OnToggleTask -> {
                    val updatedTask = action.task.copy(isCompleted = !action.task.isCompleted)
                    taskLocalDataSource.updateTask(updatedTask)
                    eventChannel.send(TaskScreenEvent.UpdatedTask)
                }

                OnDeleteAllTasks -> {
                    taskLocalDataSource.deleteAllTasks()
                    eventChannel.send(TaskScreenEvent.AllTaskDeleted)
                }

                else -> Unit
            }
        }
    }
}
