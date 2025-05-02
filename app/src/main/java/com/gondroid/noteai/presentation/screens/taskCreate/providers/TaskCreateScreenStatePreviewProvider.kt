package com.gondroid.noteai.presentation.screens.taskCreate.providers

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.gondroid.noteai.domain.Category
import com.gondroid.noteai.presentation.screens.taskCreate.TaskCreateScreenState

class TaskCreateScreenStatePreviewProvider : PreviewParameterProvider<TaskCreateScreenState> {
    override val values: Sequence<TaskCreateScreenState>
        get() =
            sequenceOf(
                TaskCreateScreenState(
                    taskName = TextFieldState("Task 1"),
                    taskDescription = TextFieldState("Description 1"),
                    isTaskDone = false,
                    category = Category.WORK
                ),
                TaskCreateScreenState(
                    taskName = TextFieldState("Task 2"),
                    taskDescription = TextFieldState("Description 2"),
                    isTaskDone = true,
                    category = Category.WORK
                ),
                TaskCreateScreenState(
                    taskName = TextFieldState("Task 3"),
                    taskDescription = TextFieldState("Description 3"),
                    isTaskDone = false,
                    category = Category.OTHER
                ),
                TaskCreateScreenState(
                    taskName = TextFieldState("Task 4"),
                    taskDescription = TextFieldState("Description 4"),
                    isTaskDone = true,
                    category = null
                ),
                TaskCreateScreenState(
                    taskName = TextFieldState("Task 5"),
                    taskDescription = TextFieldState(""),
                    isTaskDone = false,
                    category = null
                )
            )
}
