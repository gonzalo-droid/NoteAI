package com.gondroid.noteai.presentation.screens.taskCreate

import androidx.compose.foundation.text.input.TextFieldState
import com.gondroid.noteai.domain.Category

data class TaskCreateScreenState(
    val taskName: TextFieldState = TextFieldState(),
    val taskDescription: TextFieldState = TextFieldState(),
    val isTaskDone: Boolean = false,
    val category: Category? = null,
    val canSaveTask: Boolean = false
)
