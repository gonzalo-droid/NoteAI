package com.gondroid.noteai.data.local.note

import com.gondroid.noteai.domain.Note
import com.gondroid.noteai.domain.repository.NoteLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomNoteLocalDataSource
@Inject
constructor(
    private val noteDao: NoteDao,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO
) : NoteLocalDataSource {
    override val notesFlow: Flow<List<Note>>
        get() =
            noteDao
                .getAllNotes()
                .map {
                    it.map { noteEntity -> noteEntity.toNote() }
                }.flowOn(dispatcherIO)

    override suspend fun addNote(note: Note) =
        withContext(dispatcherIO) {
            noteDao.upsertNote(NoteEntity.fromNote(note))
        }

    override suspend fun updateNote(updatedNote: Note) =
        withContext(dispatcherIO) {
            noteDao.upsertNote(NoteEntity.fromNote(updatedNote))
        }

    override suspend fun removeNote(note: Note) =
        withContext(dispatcherIO) {
            noteDao.deleteNoteById(note.id)
        }

    override suspend fun deleteAllNotes() =
        withContext(dispatcherIO) {
            noteDao.deleteAllNotes()
        }

    override suspend fun getNoteById(noteId: String): Note? =
        withContext(dispatcherIO) {
            noteDao.getNoteById(noteId)?.toNote()
        }
}
