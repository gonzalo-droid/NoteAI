package com.gondroid.noteai.presentation.screens.components

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Textsms
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.gondroid.noteai.ui.theme.NoteAppTheme
import kotlinx.coroutines.delay

@Composable
fun AudioPlayerItemRoot(
    modifier: Modifier,
    audioName: String,
    transcription: String?,
    date: String,
    audioUri: Uri,
    onTranscribe: () -> Unit
) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var duration by remember { mutableFloatStateOf(1f) }

    DisposableEffect(Unit) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(context, audioUri)
            mediaPlayer.prepare()
            duration = mediaPlayer.duration.toFloat()
        } catch (e: Exception) {
            Log.d("ErrorAudio", e.message.toString())
            e.printStackTrace()
        }

        mediaPlayer.setOnCompletionListener {
            isPlaying = false
            progress = 0f
        }

        onDispose {
            mediaPlayer.release()
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            progress = mediaPlayer.currentPosition.toFloat()
            delay(500) // Actualiza cada 500ms
        }
    }

    AudioPlayerItem(
        modifier = modifier,
        progress = progress,
        duration = duration,
        audioName = audioName,
        transcription = transcription,
        date = date,
        onTranscribe = onTranscribe,
        isPlaying = isPlaying,
        onTogglePlay = {
            if (isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
            }
            isPlaying = !isPlaying
        }
    )
}

@Composable
fun AudioPlayerItem(
    modifier: Modifier,
    audioName: String,
    transcription: String?,
    date: String,
    onTranscribe: () -> Unit,
    progress: Float,
    duration: Float,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit
) {
    var isOpen by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier =
            modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.inversePrimary)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.MusicNote, contentDescription = "Audio Icon")
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = audioName, fontWeight = FontWeight.Bold)
                Text(text = date, style = MaterialTheme.typography.bodySmall)
                Slider(
                    value = progress,
                    onValueChange = {},
                    valueRange = 0f..duration,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            IconButton(onClick = { onTogglePlay() }) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause"
                )
            }

            if (!isLoading) {
                IconButton(onClick = {
                    onTranscribe()
                    isLoading = true
                }) {
                    Icon(imageVector = Icons.Default.Textsms, contentDescription = "More Options")
                }
            } else {
                CircularProgressIndicator(
                    modifier =
                    Modifier
                        .padding(0.dp)
                        .align(Alignment.CenterVertically)
                        .width(30.dp)
                        .height(30.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }

        transcription?.let {
            isLoading = false
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.tertiary),
                verticalArrangement = Arrangement.Top
            ) {
                Button(
                    onClick = { isOpen = !isOpen },
                    modifier =
                    Modifier
                        .fillMaxWidth(),
                    shape =
                    RoundedCornerShape(
                        topStart = 8.dp,
                        topEnd = 8.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    ),
                    colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text(
                        text = "Trancripción"
                    )
                    Icon(
                        imageVector = if (isOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }

                AnimatedVisibility(visible = isOpen) {
                    Box(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.tertiary,
                                shape =
                                RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 0.dp,
                                    bottomEnd = 8.dp,
                                    bottomStart = 8.dp
                                )
                            )
                    ) {
                        Text(
                            text = it,
                            modifier =
                            Modifier
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTooltipExample() {
    var showTooltip by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { showTooltip = !showTooltip }) {
            Text("Show Tooltip")
        }

        if (showTooltip) {
            Popup {
                Box(
                    modifier =
                    Modifier
                        .background(
                            Color.Black.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(8.dp)
                        ).padding(8.dp)
                ) {
                    Text("Custom Tooltip", color = Color.White)
                }
            }
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun AudioPlayerItemDark() {
    NoteAppTheme {
        AudioPlayerItem(
            modifier = Modifier,
            audioName = "Audio Name",
            date = "Date",
            onTranscribe = {},
            progress = 1f,
            duration = 3f,
            onTogglePlay = {},
            isPlaying = false,
            transcription = "transcription on real time"
        )
    }
}
