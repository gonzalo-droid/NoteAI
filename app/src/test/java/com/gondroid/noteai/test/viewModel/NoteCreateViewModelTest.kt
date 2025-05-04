package com.gondroid.noteai.test.viewModel

import androidx.lifecycle.SavedStateHandle
import com.gondroid.noteai.domain.Note
import com.gondroid.noteai.domain.repository.NoteLocalDataSource
import com.gondroid.noteai.domain.repository.VoiceRecorderLocalDataSource
import com.gondroid.noteai.presentation.navigation.NoteCreateScreenRoute
import com.gondroid.noteai.presentation.screens.noteCreate.NoteCreateViewModel
import com.gondroid.noteai.test.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteCreateViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var viewModel: NoteCreateViewModel

    private val localDataSource: NoteLocalDataSource = mockk(relaxed = true)
    private val voiceRecorderLocalDataSource: VoiceRecorderLocalDataSource = mockk(relaxed = true)
    private var savedStateHandle: SavedStateHandle = mockk(relaxed = true)

    @Before
    fun setup() {
        every { voiceRecorderLocalDataSource.voiceRecordingsFlow } returns flowOf(emptyList())
    }

    @Test
    fun `when initialized with existing note, state should reflect note details`() = runTest {
        val noteId = "123"
        val testRoute = NoteCreateScreenRoute(noteId = noteId)
        val json = Json.encodeToString(NoteCreateScreenRoute.serializer(), testRoute)
        val savedStateHandle = SavedStateHandle(mapOf("route" to json))

        coEvery { localDataSource.getNoteById(noteId) } returns Note(
            id = noteId,
            title = "Test Note",
            content = "Test Content",
            category = "Personal"
        )

        viewModel = NoteCreateViewModel(
            savedStateHandle = savedStateHandle,
            localDataSource = localDataSource,
            voiceRecorderLocalDataSource = voiceRecorderLocalDataSource
        )

        advanceUntilIdle()

        coVerify(exactly = 1) { localDataSource.getNoteById(noteId) }

        assertEquals("Test Note", viewModel.state.title.text)
        assertEquals("Test Content", viewModel.state.content.text)
        assertEquals("Personal", viewModel.state.category)
    }

    @Test
    fun `when initialized without noteId, should start with empty state`() = runTest {
        savedStateHandle =
            SavedStateHandle(mapOf("noteArgs" to NoteCreateScreenRoute(noteId = null)))

        viewModel =
            NoteCreateViewModel(savedStateHandle, localDataSource, voiceRecorderLocalDataSource)

        advanceUntilIdle() // Esperar a que terminen las corutinas

        coVerify(exactly = 0) { localDataSource.getNoteById("123") }

        assertEquals("", viewModel.state.title.text)
        assertEquals("", viewModel.state.content.text)
        assertEquals(null, viewModel.state.category)
    }
}
