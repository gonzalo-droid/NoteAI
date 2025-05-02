package com.gondroid.noteai.presentation.screens.task.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.gondroid.noteai.domain.Category
import com.gondroid.noteai.domain.Task

class TaskItemPreviewProvider : PreviewParameterProvider<Task> {
    override val values: Sequence<Task>
        get() =
            sequenceOf(
                Task(
                    id = "1",
                    noteId = "",
                    title = "Task 1",
                    isCompleted = false,
                    description = "Description 1",
                    category = Category.WORK
                ),
                Task(
                    id = "2",
                    noteId = "",
                    title = "Task 2",
                    isCompleted = true,
                    description = "Description 2",
                    category = Category.OTHER
                )
            )
}
