package com.gondroid.noteai.presentation.screens.notes

import com.gondroid.noteai.domain.Note

data class NotesDataState(
    val notes: List<Note> = emptyList(),
    val date: String = "",
)