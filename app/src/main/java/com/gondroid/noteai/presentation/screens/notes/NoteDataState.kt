package com.gondroid.noteai.presentation.screens.notes

import com.gondroid.noteai.domain.Note

data class NoteDataState(
    val notes: List<Note> = emptyList(),
)
