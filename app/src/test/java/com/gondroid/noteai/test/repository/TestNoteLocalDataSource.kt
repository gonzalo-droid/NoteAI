package com.gondroid.noteai.test.repository

import com.gondroid.noteai.domain.Note
import com.gondroid.noteai.domain.repository.NoteLocalDataSource
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.annotations.TestOnly

class TestNoteLocalDataSource : NoteLocalDataSource  {

    /**
     * Backing hot flow para simular el flujo de notas.
     * Se configura con replay = 1 para que siempre emita el estado más reciente.
     */
    private val testNoteFlow: MutableSharedFlow<List<Note>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    // Lista interna que almacena las notas en memoria.
    private val notes = mutableListOf<Note>()


    // Propiedad pública que expone el flujo de notas.
    override val notesFlow: Flow<List<Note>>
        get() = testNoteFlow

    // Inicializamos el flujo con una lista vacía.
    init {
        testNoteFlow.tryEmit(emptyList())
    }

    override suspend fun addNote(note: Note) {
        notes.add(note)
        // Emite el estado actualizado de las notas.
        testNoteFlow.tryEmit(notes.toList())
    }

    override suspend fun updateNote(updatedNote: Note) {
        // Busca la nota a actualizar por su ID.
        val index = notes.indexOfFirst { it.id == updatedNote.id }
        if (index != -1) {
            notes[index] = updatedNote
            testNoteFlow.tryEmit(notes.toList())
        }
    }

    override suspend fun removeNote(note: Note) {
        // Elimina la nota que tenga el mismo ID.
        notes.removeIf { it.id == note.id }
        testNoteFlow.tryEmit(notes.toList())
    }

    override suspend fun deleteAllNotes() {
        notes.clear()
        testNoteFlow.tryEmit(emptyList())
    }

    override suspend fun getNoteById(noteId: String): Note? {
        return notes.find { it.id == noteId }
    }
}