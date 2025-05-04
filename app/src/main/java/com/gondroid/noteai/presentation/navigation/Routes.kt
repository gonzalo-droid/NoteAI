package com.gondroid.noteai.presentation.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class TaskScreenRoute(
    val noteId: String? = null
)

@Serializable
object NoteScreenRoute

@Serializable
data class TaskCreateScreenRoute(
    val noteId: String,
    val taskId: String? = null
)

@Serializable
data class NoteCreateScreenRoute(
    val noteId: String? = null
)

@Serializable
object VoiceRecorderRoute
