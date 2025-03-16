package com.gondroid.noteai.test.repository

import com.gondroid.noteai.data.local.note.NoteDao
import com.gondroid.noteai.data.local.note.NoteEntity
import com.gondroid.noteai.data.local.note.RoomNoteLocalDataSource
import com.gondroid.noteai.test.util.MainDispatcherRule
import com.gondroid.noteai.test.util.MockUtil.mockNote
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteLocalDataSourceTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var dataSource: RoomNoteLocalDataSource
    private val noteDao: NoteDao = mockk(relaxed = true)

    @Before
    fun setup() {
        dataSource = RoomNoteLocalDataSource(noteDao, coroutineRule.testDispatcher)
    }
    
    @Test
    fun `notesFlow should emit transformed notes`() = runTest {
        val notesEntities = listOf(
            NoteEntity.fromNote(mockNote(id = "1")),
            NoteEntity.fromNote(mockNote(id = "2", title = "Title", content = "Content")),
            NoteEntity.fromNote(mockNote(id = "3"))
        )
        every { noteDao.getAllNotes() } returns flowOf(notesEntities)

        val result = dataSource.notesFlow.first()

        assertEquals(3, result.size)
        assertEquals("Title", result[1].title)
        assertEquals("Content", result[1].content)
    }

    @Test
    fun `addNote should call upsertNote`() = runTest {
        val note = mockNote()

        dataSource.addNote(note)

        coVerify { noteDao.upsertNote(NoteEntity.fromNote(note)) }
    }

    @Test
    fun `removeNote should call deleteNoteById`() = runTest {
        val note = mockNote()
        dataSource.removeNote(note)
        coVerify { noteDao.deleteNoteById(note.id) }
    }

    @Test
    fun `getNoteById should return correct note`() = runTest {
        val noteEntity =
            NoteEntity.fromNote(mockNote(id = "2", title = "Title", content = "Content"))
        coEvery { noteDao.getNoteById("1") } returns noteEntity

        val result = dataSource.getNoteById("1")

        assertEquals("Title", result?.title)
        assertEquals("Content", result?.content)
    }
}