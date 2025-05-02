package com.gondroid.noteai.presentation.screens.voiceRecorder

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gondroid.noteai.R
import com.gondroid.noteai.ui.theme.NoteAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun VoiceRecorderScreenRoot(
    navigateBack: () -> Boolean,
    onRecordingFinished: (String?) -> Unit
) {
    val context = LocalContext.current
    var isPaused by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var timerJob by remember { mutableStateOf<Job?>(null) }
    val audioRecorder = remember { AudioRecorder() }
    var recordedFilePath by remember { mutableStateOf<String?>(null) }

    fun resetRecorderState() {
        isPaused = false
        timerJob?.cancel()
        elapsedTime = 0
        recordedFilePath = null
    }

    VoiceRecorderScreen(
        onAction = { action ->
            when (action) {
                is ActionVoiceRecorder.Back -> {
                    navigateBack()
                }
            }
        },
        elapsedTime = elapsedTime,
        isPaused = isPaused,
        isRecording = recordedFilePath != null,
        audioRecorder = audioRecorder,
        cancelRecording = {
            audioRecorder.cancelRecording()
            resetRecorderState()
            Toast
                .makeText(
                    context,
                    context.getString(R.string.delete_voice_recorder),
                    Toast.LENGTH_SHORT
                ).show()
            navigateBack()
        },
        stopRecording = {
            audioRecorder.stopRecording()
            isPaused = false
            timerJob?.cancel()
            elapsedTime = 0
            Toast
                .makeText(
                    context,
                    context.getString(R.string.saving_voice_recorder),
                    Toast.LENGTH_SHORT
                ).show()
            onRecordingFinished(recordedFilePath)
        },
        pauseRecording = {
            audioRecorder.pauseRecording()
            isPaused = true
            timerJob?.cancel()
        },
        startRecording = {
            recordedFilePath = audioRecorder.startRecording()
            if (recordedFilePath != null) {
                isPaused = false
                timerJob?.cancel()
                timerJob =
                    CoroutineScope(Dispatchers.Main).launch {
                        while (isActive) {
                            delay(1000)
                            elapsedTime++
                        }
                    }
            }
        },
        resumeRecording = {
            audioRecorder.resumeRecording()
            isPaused = false
            timerJob =
                CoroutineScope(Dispatchers.Main).launch {
                    while (isActive) {
                        delay(1000)
                        elapsedTime++
                    }
                }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceRecorderScreen(
    onAction: (ActionVoiceRecorder) -> Unit,
    elapsedTime: Long,
    isPaused: Boolean,
    isRecording: Boolean,
    audioRecorder: AudioRecorder,
    cancelRecording: () -> Unit,
    stopRecording: () -> Unit,
    pauseRecording: () -> Unit,
    startRecording: () -> Unit,
    resumeRecording: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(R.string.voice_recorder)
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
                                ActionVoiceRecorder.Back
                            )
                        }
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = String.format("%02d:%02d", elapsedTime / 60, elapsedTime % 60),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            AudioVisualizer(
                audioRecorder,
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isRecording) {
                    IconButton(
                        onClick = cancelRecording,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel Recording",
                            tint = Color.White,
                            modifier =
                            Modifier
                                .size(48.dp)
                                .background(Color.Gray, CircleShape)
                                .padding(12.dp)
                        )
                    }

                    IconButton(
                        onClick = { if (isPaused) resumeRecording() else pauseRecording() },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = if (isPaused) "Resume Recording" else "Pause Recording",
                            tint = Color.White,
                            modifier =
                            Modifier
                                .size(48.dp)
                                .background(if (isPaused) Color.Green else Color.Red, CircleShape)
                                .padding(12.dp)
                        )
                    }

                    IconButton(
                        onClick = stopRecording,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Stop Recording",
                            tint = Color.White,
                            modifier =
                            Modifier
                                .size(48.dp)
                                .background(Color.Gray, CircleShape)
                                .padding(12.dp)
                        )
                    }
                } else {
                    IconButton(
                        onClick = startRecording,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Start Recording",
                            tint = Color.White,
                            modifier =
                            Modifier
                                .size(48.dp)
                                .background(Color.Red, CircleShape)
                                .padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AudioVisualizer(
    audioRecorder: AudioRecorder,
    modifier: Modifier = Modifier
) {
    val amplitudes by audioRecorder.getAmplitudeFlow().collectAsState(initial = List(50) { 0.1f })

    Canvas(modifier = modifier.fillMaxSize()) {
        val barWidth = size.width / amplitudes.size
        val maxHeight = size.height

        amplitudes.forEachIndexed { index, amplitude ->
            val barHeight = maxHeight * amplitude
            drawRoundRect(
                color = Color(0xFFE65100),
                topLeft = Offset(index * barWidth, (maxHeight - barHeight) / 2),
                size = Size(barWidth * 0.5f, barHeight),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVoiceRecorderScreen() {
    NoteAppTheme {
        VoiceRecorderScreen(
            onAction = {},
            elapsedTime = 0L,
            isRecording = true,
            isPaused = true,
            audioRecorder = AudioRecorder(),
            stopRecording = {},
            pauseRecording = {},
            startRecording = {},
            resumeRecording = {},
            cancelRecording = {}
        )
    }
}
