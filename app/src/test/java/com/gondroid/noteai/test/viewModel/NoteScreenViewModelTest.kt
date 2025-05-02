package com.gondroid.noteai.test.viewModel

import com.gondroid.noteai.domain.Note
import com.gondroid.noteai.domain.repository.NoteLocalDataSource
import com.gondroid.noteai.presentation.screens.notes.NoteScreenViewModel
import com.gondroid.noteai.test.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteScreenViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var viewModel: NoteScreenViewModel

    private val noteLocalDataSource: NoteLocalDataSource = mockk(relaxed = true)

    @Before
    fun setup() {
        every { noteLocalDataSource.notesFlow } returns flowOf(emptyList()) // Simula flujo vacío
        viewModel = NoteScreenViewModel(noteLocalDataSource)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `state updates when notesFlow emits new data`() = runTest {
        val fakeNotes = listOf(
            Note(
                id = "1",
                title = "Lista de Compras 🛒",
                content =
                """ Antes de ir al supermercado, asegúrate de llevar una lista:  
                            🥦 Frutas y verduras frescas  
                            🥩 Proteínas saludables  
                            🥛 Lácteos y cereales  
                            Evita compras impulsivas y aprovecha ofertas. ¡Ahorra dinero!  
                """.trimIndent(),
                category = "COMPRAS"
            )
        )

        val notesFlow = MutableStateFlow(fakeNotes)

        every { noteLocalDataSource.notesFlow } returns notesFlow.asStateFlow()

        viewModel = NoteScreenViewModel(noteLocalDataSource)

        // Esperamos que el estado se actualice
        advanceUntilIdle() // Espera a que coroutines finalicen

        assertEquals(fakeNotes, viewModel.state.notes)
    }
}
