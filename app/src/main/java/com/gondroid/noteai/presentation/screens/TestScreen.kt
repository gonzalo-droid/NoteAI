package com.gondroid.noteai.presentation.screens

import android.Manifest
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gondroid.noteai.presentation.screens.voiceRecorder.AudioRecorder
import com.gondroid.noteai.presentation.screens.voiceRecorder.WhisperTranscriber
import com.gondroid.noteai.ui.theme.NoteAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TestScreenRoot() {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }
    val audioRecorder = remember { AudioRecorder() }
    val whisperTranscriber = remember { WhisperTranscriber() }
    var recordedFilePath by remember { mutableStateOf<String?>(null) }
    val mediaPlayer = remember { MediaPlayer() }
    var isPlaying by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var duration by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    var whisperText by remember { mutableStateOf<String?>(null) }

    // Solicita el permiso antes de grabar
    RequestAudioPermission { granted ->
        hasPermission = granted
    }

    TestScreen(
        progress = progress,
        duration = duration,
        hasPermission = hasPermission,
        recordedFilePath = recordedFilePath,
        isPlaying = isPlaying,
        whisperText = whisperText,
        onStartRecorder = {
            audioRecorder.stopRecording()
            Log.d("AudioRecorder", "Grabación guardada")

            whisperTranscriber.transcribeAudio(recordedFilePath!!) { text ->
                whisperText = text ?: "Error en la transcripción"
            }
        },
        onStopRecorder = {
            recordedFilePath = audioRecorder.startRecording()
            Log.d("AudioRecorder", "Grabando...")
        },
        onPauseMediaPlayer = {
            mediaPlayer.pause()
            isPaused = true
        },
        onFinishMediaPlayer = {
            mediaPlayer.stop()
            mediaPlayer.reset()
            isPlaying = false
            isPaused = false
            progress = 0f
        },
        onMediaPlayer = { filePath ->

            if (!isPlaying) {
                try {
                    mediaPlayer.setDataSource(filePath)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    duration = mediaPlayer.duration
                    isPlaying = true

                    coroutineScope.launch {
                        while (mediaPlayer.isPlaying) {
                            progress =
                                mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration.toFloat()
                            delay(500)
                        }
                    }

                    mediaPlayer.setOnCompletionListener {
                        isPlaying = false
                        isPaused = false
                        progress = 0f
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al reproducir audio", Toast.LENGTH_SHORT).show()
                }
            } else if (isPaused) {
                mediaPlayer.start()
                isPaused = false
            }
        }
    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(
    hasPermission: Boolean,
    recordedFilePath: String?,
    isPlaying: Boolean,
    onStartRecorder: () -> Unit = {},
    onStopRecorder: () -> Unit = {},
    onPauseMediaPlayer: () -> Unit,
    onMediaPlayer: (String) -> Unit,
    onFinishMediaPlayer: () -> Unit,
    progress: Float,
    duration: Int,
    whisperText: String?
) {
    var isRecording by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        style = MaterialTheme.typography.headlineSmall,
                        text = "Recorder"
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier =
                        Modifier.clickable {
                        }
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Botón de grabación
            VoiceNoteRecorder(
                modifier = Modifier.fillMaxWidth(),
                isRecording = isRecording,
                onRecordToggle = {
                    if (hasPermission) {
                        if (isRecording) {
                            onStartRecorder()
                        } else {
                            onStopRecorder()
                        }
                        isRecording = !isRecording
                    } else {
                        Log.d("AudioRecorder", "Permiso de micrófono requerido")
                    }
                }
            )

            // Mostrar archivo guardado
            recordedFilePath?.let { filePath ->
                Text(text = "Archivo guardado en: $filePath", fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                // Barra de progreso del audio
                Slider(
                    value = progress,
                    onValueChange = { },
                    valueRange = 0f..1f,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(text = "Duración: ${duration / 1000} segundos")

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para reproducir el audio grabado
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = {
                        onMediaPlayer(filePath)
                    }) {
                        Text(if (isPlaying) "Detener" else "Reproducir")
                    }
                    Button(onClick = {
                        if (isPlaying) {
                            onPauseMediaPlayer()
                        }
                    }, enabled = isPlaying) {
                        Text("Pausar")
                    }

                    Button(onClick = {
                        if (isPlaying) {
                            onFinishMediaPlayer()
                        }
                    }, enabled = isPlaying) {
                        Text("Detener")
                    }
                }
                whisperText?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun VoiceNoteRecorder(
    modifier: Modifier,
    isRecording: Boolean,
    onRecordToggle: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Estado de la grabación
        Text(
            text = if (isRecording) "Grabando..." else "Presiona para grabar",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isRecording) Color.Red else Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botón de grabación con icono dinámico
        IconButton(
            onClick = onRecordToggle,
            modifier =
            Modifier
                .size(80.dp)
                .background(
                    color = if (isRecording) Color.Red else Color.Gray,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Close else Icons.Default.Favorite,
                contentDescription = "Record Button",
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TestScreenPreview() {
    NoteAppTheme {
        TestScreen(
            hasPermission = true,
            recordedFilePath = "file",
            isPlaying = true,
            onStartRecorder = {},
            onStopRecorder = {},
            onPauseMediaPlayer = {},
            onMediaPlayer = {},
            onFinishMediaPlayer = {},
            progress = 1f,
            duration = 10,
            whisperText = "whisperText"
        )
    }
}
