package com.gondroid.noteai.presentation.screens.taskCreate

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.gondroid.noteai.R
import com.gondroid.noteai.presentation.screens.taskCreate.providers.TaskCreateScreenStatePreviewProvider
import com.gondroid.noteai.ui.theme.NoteAppTheme

@Composable
fun TaskCreateScreenRoot(
    navigateBack: () -> Boolean,
    viewModel: TaskCreateViewModel
) {
    val state = viewModel.state
    val event = viewModel.event

    val context = LocalContext.current

    LaunchedEffect(true) {
        event.collect { event ->
            when (event) {
                is TaskCreateEvent.TaskCreated -> {
                    Toast
                        .makeText(
                            context,
                            context.getString(R.string.task_created),
                            Toast.LENGTH_SHORT
                        ).show()
                    navigateBack()
                }
            }
        }
    }

    TaskCreateScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is ActionTask.Back -> {
                    navigateBack()
                }
                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreateScreen(
    modifier: Modifier = Modifier,
    state: TaskCreateScreenState,
    onAction: (ActionTask) -> Unit
) {
    var isDescriptionFocus by remember {
        mutableStateOf(false)
    }
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        style = MaterialTheme.typography.headlineSmall,
                        text = stringResource(R.string.task)
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
                                ActionTask.Back
                            )
                        }
                    )
                }
            )
        }
    ) { paddingValues ->

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.done),
                    style =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(8.dp)
                )

                Checkbox(
                    checked = state.isTaskDone,
                    onCheckedChange = {
                        onAction(
                            ActionTask.ChangeTaskDone(
                                isTaskDone = it
                            )
                        )
                    }
                )
            }

            BasicTextField(
                state = state.taskName,
                textStyle =
                MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                decorator = { innerTextField ->
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if ((
                            state.taskName.text
                                .toString()
                                .isEmpty()
                            )
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.title),
                                color =
                                MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.5f
                                ),
                                style =
                                MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        } else {
                            innerTextField()
                        }
                    }
                }
            )

            BasicTextField(
                state = state.taskDescription,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
                textStyle =
                MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                lineLimits =
                if (isDescriptionFocus) {
                    TextFieldLineLimits.MultiLine(
                        minHeightInLines = 1,
                        maxHeightInLines = 5
                    )
                } else {
                    TextFieldLineLimits.Default
                },
                modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .onFocusChanged {
                        isDescriptionFocus = it.isFocused
                    },
                decorator = { innerTextField ->
                    Column {
                        if (state.taskDescription.text
                            .toString()
                            .isEmpty() &&
                            !isDescriptionFocus
                        ) {
                            Text(
                                text = stringResource(R.string.task_description),
                                color =
                                MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.5f
                                )
                            )
                        } else {
                            innerTextField()
                        }
                    }
                }
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Button(
                enabled = state.canSaveTask,
                onClick = {
                    onAction(
                        ActionTask.SaveTask
                    )
                },
                modifier =
                Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.titleMedium,
                    color =
                    if (state.canSaveTask) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                )
            }
        }
    }
}

@Composable
@Preview
fun TaskCreateScreenLightPreview(
    @PreviewParameter(TaskCreateScreenStatePreviewProvider::class) state: TaskCreateScreenState
) {
    NoteAppTheme {
        TaskCreateScreen(
            state = state,
            onAction = {}
        )
    }
}

@Composable
@Preview(
    uiMode = UI_MODE_NIGHT_YES
)
fun TaskCreateScreenDarkPreview(
    @PreviewParameter(TaskCreateScreenStatePreviewProvider::class) state: TaskCreateScreenState
) {
    NoteAppTheme {
        TaskCreateScreen(
            state = state,
            onAction = {}
        )
    }
}
