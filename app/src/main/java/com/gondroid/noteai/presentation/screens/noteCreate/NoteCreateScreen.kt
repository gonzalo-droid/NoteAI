package com.gondroid.noteai.presentation.screens.noteCreate

import android.Manifest
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gondroid.noteai.R
import com.gondroid.noteai.domain.Category
import com.gondroid.noteai.presentation.screens.components.AudioPlayerItemRoot
import com.gondroid.noteai.presentation.screens.noteCreate.providers.NoteCreateScreenStatePreviewProvider
import com.gondroid.noteai.presentation.screens.voiceRecorder.WhisperTranscriber
import com.gondroid.noteai.presentation.util.DateUtil
import com.gondroid.noteai.ui.theme.NoteAppTheme

@Composable
fun NoteCreateScreenRoot(
    navigateBack: () -> Boolean,
    navigateToVoiceRecorder: () -> Unit,
    navigateToMyTask: (String) -> Unit,
    viewModel: NoteCreateViewModel
) {
    val state = viewModel.state
    val event = viewModel.event
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }

    val whisperTranscriber = remember { WhisperTranscriber() }

    // Solicita el permiso antes de grabar
    RequestAudioPermission { granted ->
        hasPermission = granted
    }

    val recordedFilePath by viewModel.recordedFilePath.collectAsState()
    LaunchedEffect(recordedFilePath) {
        recordedFilePath?.takeIf { it.isNotBlank() }?.let { filePath ->
            viewModel.saveAudioNoteToDatabase(filePath)
        }
    }

    LaunchedEffect(true) {
        event.collect { event ->
            when (event) {
                is NoteCreateEvent.NoteCreated -> {
                    Toast
                        .makeText(
                            context,
                            context.getString(R.string.note_created),
                            Toast.LENGTH_SHORT
                        ).show()
                    navigateBack()
                }

                is NoteCreateEvent.SaveVoiceRecorder -> {
                    Toast
                        .makeText(
                            context,
                            context.getString(R.string.note_save_success),
                            Toast.LENGTH_SHORT
                        ).show()
                }

                is NoteCreateEvent.TranscriptionUpdate -> {
                    Toast
                        .makeText(
                            context,
                            context.getString(R.string.transcription_update),
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        }
    }

    NoteCreateScreen(
        state = state,
        whisperTranscriber = whisperTranscriber,
        onAction = { action ->
            when (action) {
                is ActionNoteCreate.Back -> {
                    navigateBack()
                }

                is ActionNoteCreate.AddVoiceRecorder -> {
                    navigateToVoiceRecorder()
                }

                is ActionNoteCreate.MyTask -> {
                    state.noteId?.let {
                        navigateToMyTask(it)
                    }
                }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NoteCreateScreen(
    whisperTranscriber: WhisperTranscriber,
    modifier: Modifier = Modifier,
    state: NoteCreateScreenState,
    onAction: (ActionNoteCreate) -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(R.string.note)
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
                                ActionNoteCreate.Back
                            )
                        }
                    )
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier =
            Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier =
                Modifier
                    .padding(bottom = 100.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                            Modifier.clickable {
                                isExpanded = true
                            }
                        ) {
                            Text(
                                text =
                                state.category?.toString()
                                    ?: stringResource(R.string.category),
                                style =
                                MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier =
                                Modifier
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(8.dp)
                                    ).padding(8.dp)
                            )

                            Box(
                                modifier = Modifier.padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Add Note",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                DropdownMenu(
                                    modifier =
                                    Modifier.background(
                                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                                    ),
                                    expanded = isExpanded,
                                    onDismissRequest = { isExpanded = false }
                                ) {
                                    Column {
                                        Category.entries.forEach { category ->
                                            Text(
                                                text = category.name,
                                                style =
                                                MaterialTheme.typography.bodyMedium.copy(
                                                    color = MaterialTheme.colorScheme.onSurface
                                                ),
                                                modifier =
                                                Modifier
                                                    .padding(8.dp)
                                                    .padding(
                                                        8.dp
                                                    ).clickable {
                                                        isExpanded = false
                                                        onAction(
                                                            ActionNoteCreate.ChangeNoteCategory(
                                                                category = category
                                                            )
                                                        )
                                                    }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    BasicTextField(
                        state = state.title,
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
                                    state.title.text
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
                    FlowRow(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ItemSheep(
                            modifier,
                            onAction = {
                                onAction(
                                    ActionNoteCreate.AddVoiceRecorder
                                )
                            },
                            name = "Nota de voz",
                            imageVector = Icons.Default.Mic
                        )
                        ItemSheep(
                            modifier,
                            onAction = {
                                onAction(
                                    ActionNoteCreate.MyTask
                                )
                            },
                            name = "Mis tareas",
                            imageVector = Icons.Default.Task
                        )
                    }

                    FieldContent(
                        state = state,
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(
                    items = state.voiceRecordings,
                    key = { record -> record.id }
                ) { record ->
                    AudioPlayerItemRoot(
                        modifier = Modifier.fillMaxWidth(),
                        audioName = record.name ?: "",
                        date = DateUtil.formatDateTime(record.date),
                        audioUri = Uri.parse(record.path),
                        transcription = record.transcription, // Ahora mostramos la transcripción
                        onTranscribe = {
                            if (record.transcription.isNullOrEmpty()) {
                                whisperTranscriber.transcribeAudio(record.path) { text ->
                                    onAction(
                                        ActionNoteCreate.SaveVoiceRecorder(
                                            record.id,
                                            text ?: ""
                                        )
                                    )
                                }
                            }
                        }
                    )
                }
            }

            ButtonSection(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                state = state,
                onAction = {
                    onAction(
                        ActionNoteCreate.SaveNote
                    )
                }
            )
        }
    }
}

@Composable
fun ButtonSection(
    modifier: Modifier,
    state: NoteCreateScreenState,
    onAction: () -> Unit
) {
    Box(
        modifier =
        modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 8.dp)
    ) {
        Button(
            enabled = state.canSaveNote,
            onClick = {
                onAction()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.save),
                style = MaterialTheme.typography.titleMedium,
                color =
                if (state.canSaveNote) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onPrimaryContainer
                }
            )
        }
    }
}

@Composable
fun FieldContent(
    state: NoteCreateScreenState,
    modifier: Modifier
) {
    var isDescriptionFocus by remember {
        mutableStateOf(false)
    }

    BasicTextField(
        state = state.content,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
        textStyle =
        MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        lineLimits =
        if (isDescriptionFocus) {
            TextFieldLineLimits.MultiLine(
                minHeightInLines = 1,
                maxHeightInLines = 20
            )
        } else {
            TextFieldLineLimits.Default
        },
        modifier =
        modifier
            .onFocusChanged {
                isDescriptionFocus = it.isFocused
            },
        decorator = { innerTextField ->
            Column {
                if (state.content.text
                    .toString()
                    .isEmpty() &&
                    !isDescriptionFocus
                ) {
                    Text(
                        text = stringResource(R.string.write_your_ideas),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                innerTextField() // Renderiza el campo de texto dentro del Box
            }
        }
    )
}

@Composable
fun ItemSheep(
    modifier: Modifier,
    onAction: (ActionNoteCreate) -> Unit,
    name: String,
    imageVector: ImageVector
) {
    Row(
        modifier =
        Modifier
            .wrapContentSize()
            .border(0.dp, Color.Gray, RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable {
                onAction(ActionNoteCreate.AddVoiceRecorder)
            }.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "Nota de voz",
            tint = Color.Unspecified,
            modifier =
            Modifier
                .size(24.dp)
                .padding(4.dp)
        )

        Text(
            text = name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun RequestAudioPermission(onPermissionGranted: (Boolean) -> Unit) {
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                if (granted) {
                    onPermissionGranted(true)
                } else {
                    Log.d("AudioRecorder", "Permiso de audio denegado")
                }
            }
        )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
}

@Composable
@Preview
fun NoteScreenLightPreview(
    @PreviewParameter(NoteCreateScreenStatePreviewProvider::class) state: NoteCreateScreenState
) {
    NoteAppTheme {
        NoteCreateScreen(
            state = state,
            onAction = {},
            whisperTranscriber = WhisperTranscriber()
        )
    }
}

@Composable
@Preview(
    uiMode = UI_MODE_NIGHT_YES
)
fun NoteScreenDarkPreview(
    @PreviewParameter(NoteCreateScreenStatePreviewProvider::class) state: NoteCreateScreenState
) {
    NoteAppTheme {
        NoteCreateScreen(
            state = state,
            onAction = {},
            whisperTranscriber = WhisperTranscriber()
        )
    }
}
