package com.gondroid.noteai.presentation.screens.task.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.gondroid.noteai.domain.Category
import com.gondroid.noteai.domain.Task
import com.gondroid.noteai.presentation.screens.task.TaskDataState

class TaskScreenPreviewProvider : PreviewParameterProvider<TaskDataState> {
    override val values: Sequence<TaskDataState>
        get() =
            sequenceOf(
                TaskDataState(
                    date = "March 29, 2024",
                    summary = "5 incomplete, 5 completed",
                    completedTask = completedTask,
                    pendingTask = pendingTask
                )
            )
}

val completedTask =
    mutableListOf<Task>()
        .apply {
            repeat(20) {
                add(
                    Task(
                        id = it.toString(),
                        noteId = "",
                        title = "Task $it",
                        description = "Description $it",
                        category = Category.WORK,
                        isCompleted = true
                    )
                )
            }
        }

val pendingTask =
    mutableListOf<Task>()
        .apply {
            repeat(20) {
                add(
                    Task(
                        id = (it + 30).toString(),
                        noteId = "",
                        title = "Task $it",
                        description = "Description $it",
                        category = Category.OTHER,
                        isCompleted = false
                    )
                )
            }
        }
