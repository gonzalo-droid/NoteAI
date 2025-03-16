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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
        savedStateHandle =
            SavedStateHandle(
                mapOf("noteData" to NoteCreateScreenRoute(noteId = noteId))
            )

        println("SavedStateHandle has noteId: ${savedStateHandle.get<NoteCreateScreenRoute>("noteData")?.noteId}")

        // 1️⃣ Mock de la respuesta con delay simulado
        coEvery { localDataSource.getNoteById(noteId) } coAnswers {
            delay(300)
            Note(
                id = noteId,
                title = "Test Note",
                content = "Test Content",
                category = "Personal"
            )
        }

        viewModel =
            NoteCreateViewModel(
                savedStateHandle,
                localDataSource,
                voiceRecorderLocalDataSource
            )

        // 2️⃣ Crear el ViewModel después del mock
        println("init viewmodel")


        // 3️⃣ Avanzar la ejecución de corutinas
        advanceUntilIdle()

        // 4️⃣ Verificar que `getNoteById` se llamó dentro del ViewModelScope
        coVerify(exactly = 1) { localDataSource.getNoteById(noteId) }

        // 5️⃣ Validar estado del ViewModel
        assertEquals("Test Note", viewModel.state.title.text)
        assertEquals("Test Content", viewModel.state.content.text)
        assertEquals("Personal", viewModel.state.category)
    }

    @Test
    fun `when initialized without noteId, should start with empty state`() = runTest {
        // ✅ Caso 2: No se recibe un noteId, se debe crear una nueva nota
        savedStateHandle =
            SavedStateHandle(mapOf("noteArgs" to NoteCreateScreenRoute(noteId = null)))

        // Crear el ViewModel
        viewModel =
            NoteCreateViewModel(savedStateHandle, localDataSource, voiceRecorderLocalDataSource)

        advanceUntilIdle() // Esperar a que terminen las corutinas

        // ✅ Verificar que `getNoteById` NO se llamó
        coVerify(exactly = 0) { localDataSource.getNoteById("123") }

        // ✅ Validar que el estado está vacío
        assertEquals("", viewModel.state.title.text)
        assertEquals("", viewModel.state.content.text)
        assertEquals(null, viewModel.state.category)
    }
}