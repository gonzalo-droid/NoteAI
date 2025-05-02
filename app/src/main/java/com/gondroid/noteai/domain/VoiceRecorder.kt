package com.gondroid.noteai.domain

import java.time.LocalDateTime

data class VoiceRecorder(
    val id: String,
    val noteId: String,
    val name: String?,
    val path: String,
    val transcription: String?,
    val date: LocalDateTime = LocalDateTime.now()
)
