package com.gondroid.noteai.test.database

import com.gondroid.noteai.data.local.note.NoteDao
import com.gondroid.noteai.data.local.note.NoteEntity
import com.gondroid.noteai.test.util.MockUtil.mockNote
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NoteDaoTest : LocalDatabase() {

    private lateinit var noteDao: NoteDao

    @Before
    fun init() {
        noteDao = db.noteDao()
    }

    @Test
    fun upsertNoteTest() = runBlocking {
        val mockData = NoteEntity.fromNote(mockNote())
        noteDao.upsertNote(mockData)
        val retrievedNote = noteDao.getNoteById("1")
        assertEquals(retrievedNote.toString(), mockData.toString())
    }

    @Test
    fun deleteNoteByIdTest() = runBlocking {
        val mockData = NoteEntity.fromNote(mockNote())
        noteDao.upsertNote(mockData)
        noteDao.deleteNoteById("1")
        val retrievedNote = noteDao.getNoteById("1")
        assertEquals(retrievedNote, null)
    }

    @Test
    fun notesFlowTest() = runBlocking {
        val data = listOf<NoteEntity>(
            NoteEntity.fromNote(mockNote(id = "1", title = "Nota 1")),
            NoteEntity.fromNote(mockNote(id = "2", title = "Nota 2"))
        )

        data.forEach { note -> noteDao.upsertNote(note) }

        val notes = noteDao.getAllNotes().first()

        assertEquals(2, notes.size)
        assertEquals("Nota 1", notes[0].title)
        assertEquals("Nota 2", notes[1].title)
    }
}
