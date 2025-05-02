package com.gondroid.noteai.presentation.screens.noteCreate

import androidx.compose.foundation.text.input.TextFieldState
import com.gondroid.noteai.domain.VoiceRecorder

data class NoteCreateScreenState(
    val noteId: String? = null,
    val title: TextFieldState = TextFieldState(),
    val content: TextFieldState = TextFieldState(),
    val category: String? = null,
    val canSaveNote: Boolean = false,
    val voiceRecordings: List<VoiceRecorder> = emptyList()
)
