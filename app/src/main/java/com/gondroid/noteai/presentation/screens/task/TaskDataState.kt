package com.gondroid.noteai.presentation.screens.task

import com.gondroid.noteai.domain.Task

data class TaskDataState(
    val noteId: String = "",
    val date: String = "",
    val summary: String = "",
    val completedTask: List<Task> = emptyList(),
    val pendingTask: List<Task> = emptyList()
)
