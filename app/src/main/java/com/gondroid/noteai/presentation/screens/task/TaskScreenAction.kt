package com.gondroid.noteai.presentation.screens.task

import com.gondroid.noteai.domain.Task

sealed interface TaskScreenAction {
    data class OnToggleTask(
        val task: Task
    ) : TaskScreenAction

    data class OnDeleteTask(
        val task: Task
    ) : TaskScreenAction

    data object OnDeleteAllTasks : TaskScreenAction

    data object OnAddTask : TaskScreenAction

    data class OnClickTask(
        val taskId: String
    ) : TaskScreenAction

    data object Back : TaskScreenAction
}
