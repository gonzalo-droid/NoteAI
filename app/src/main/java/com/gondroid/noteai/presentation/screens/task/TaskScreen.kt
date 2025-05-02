package com.gondroid.noteai.presentation.screens.task

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.gondroid.noteai.R
import com.gondroid.noteai.presentation.screens.components.SectionTitle
import com.gondroid.noteai.presentation.screens.components.SummaryInfo
import com.gondroid.noteai.presentation.screens.components.TaskItem
import com.gondroid.noteai.presentation.screens.task.providers.TaskScreenPreviewProvider
import com.gondroid.noteai.ui.theme.NoteAppTheme

@Composable
fun TaskScreenRoot(
    navigateBack: () -> Boolean,
    navigateToTaskCreateScreen: (taskId: String?, noteId: String) -> Unit,
    viewModel: TaskScreenViewModel
) {
    val state = viewModel.state
    val event = viewModel.events
    val context = LocalContext.current

    LaunchedEffect(
        true
    ) {
        event.collect { event ->
            when (event) {
                TaskScreenEvent.DeletedTask -> {
                    Toast
                        .makeText(
                            context,
                            context.getString(R.string.task_deleted),
                            Toast.LENGTH_SHORT
                        ).show()
                }

                TaskScreenEvent.AllTaskDeleted -> {
                    Toast
                        .makeText(
                            context,
                            context.getString(R.string.all_task_deleted),
                            Toast.LENGTH_SHORT
                        ).show()
                }

                TaskScreenEvent.UpdatedTask -> {
                    Toast
                        .makeText(
                            context,
                            context.getString(R.string.task_updated),
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        }
    }
    TaskScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is TaskScreenAction.OnAddTask -> {
                    navigateToTaskCreateScreen(null, state.noteId)
                }

                is TaskScreenAction.OnClickTask -> {
                    navigateToTaskCreateScreen(action.taskId, state.noteId)
                }

                is TaskScreenAction.Back -> {
                    navigateBack()
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    state: TaskDataState,
    onAction: (TaskScreenAction) -> Unit
) {
    var isMenuExtended by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.task),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier =
                        Modifier.clickable {
                            onAction(
                                TaskScreenAction.Back
                            )
                        }
                    )
                },
                actions = {
                    Box(
                        modifier =
                        Modifier
                            .padding(8.dp)
                            .clickable {
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Add Task",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        DropdownMenu(
                            expanded = isMenuExtended,
                            modifier =
                            Modifier.background(
                                color = MaterialTheme.colorScheme.surfaceContainerHighest
                            ),
                            onDismissRequest = { isMenuExtended = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.delete_all),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    isMenuExtended = false
                                    onAction(TaskScreenAction.OnDeleteAllTasks)
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAction(TaskScreenAction.OnAddTask)
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier =
            Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                SummaryInfo(
                    date = state.date,
                    tasksSummary = state.summary,
                    completedTasks = state.completedTask.size,
                    totalTask = state.pendingTask.size + state.completedTask.size
                )
            }

            stickyHeader {
                SectionTitle(
                    modifier =
                    Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        ).fillParentMaxWidth(),
                    title = stringResource(R.string.pending_tasks)
                )
            }

            items(
                items = state.pendingTask,
                key = { task -> task.id }
            ) { task ->
                TaskItem(
                    modifier =
                    Modifier
                        .clip(
                            RoundedCornerShape(8.dp)
                        ).animateItem(),
                    task = task,
                    onItemSelected = { action, id ->
                        when (action) {
                            ActionOnSelected.DELETE -> {
                                onAction(TaskScreenAction.OnDeleteTask(task))
                            }

                            else -> {
                                onAction(TaskScreenAction.OnClickTask(task.id))
                            }
                        }
                    },
                    onToggleCompletion = {
                        onAction(TaskScreenAction.OnToggleTask(task))
                    }
                )
            }

            stickyHeader {
                SectionTitle(
                    modifier =
                    Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface
                        ).fillParentMaxWidth(),
                    title = stringResource(R.string.completed_tasks)
                )
            }

            items(
                items = state.completedTask,
                key = { task -> task.id }
            ) { task ->
                TaskItem(
                    modifier =
                    Modifier
                        .clip(
                            RoundedCornerShape(8.dp)
                        ).animateItem(),
                    task = task,
                    onItemSelected = { action, id ->
                        when (action) {
                            ActionOnSelected.DELETE -> {
                                onAction(TaskScreenAction.OnDeleteTask(task))
                            }

                            else -> {
                                onAction(TaskScreenAction.OnClickTask(task.id))
                            }
                        }
                    },
                    onToggleCompletion = {
                        onAction(TaskScreenAction.OnToggleTask(task))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun TaskScreenPreviewLight(
    @PreviewParameter(TaskScreenPreviewProvider::class) state: TaskDataState
) {
    NoteAppTheme {
        TaskScreen(
            state = state,
            onAction = {
            }
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TaskScreenPreviewDark(
    @PreviewParameter(TaskScreenPreviewProvider::class) state: TaskDataState
) {
    NoteAppTheme {
        TaskScreen(
            state = state,
            onAction = {
            }
        )
    }
}
