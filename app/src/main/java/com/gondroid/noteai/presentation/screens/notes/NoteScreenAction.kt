package com.gondroid.noteai.presentation.screens.notes

sealed interface NoteScreenAction {
    data object OnAddNote : NoteScreenAction

    data class OnClickNote(
        val noteId: String
    ) : NoteScreenAction
}
