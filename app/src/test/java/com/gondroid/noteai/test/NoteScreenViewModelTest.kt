package com.gondroid.noteai.test

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.gondroid.noteai.domain.Note
import com.gondroid.noteai.domain.repository.NoteLocalDataSource
import com.gondroid.noteai.presentation.screens.notes.NoteDataState
import com.gondroid.noteai.presentation.screens.notes.NoteScreenViewModel
import com.gondroid.noteai.test.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteScreenViewModelTest {

    // Regla para manejar tareas de manera síncrona
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mocks
    private lateinit var viewModel: NoteScreenViewModel
    private lateinit var mockNoteRepository: NoteLocalDataSource

    @Before
    fun setup() {
        mockNoteRepository = mockk()
        viewModel = NoteScreenViewModel(mockNoteRepository)
    }

    @Test
    fun `init load notes updates state with repository notes`() = runTest {
        // Arrange
        val notesList = listOf(
            Note(
                id = "1",
                title = "Lista de Compras 🛒",
                content =
                """
                            Antes de ir al supermercado, asegúrate de llevar una lista:  
                            🥦 Frutas y verduras frescas  
                            🥩 Proteínas saludables  
                            🥛 Lácteos y cereales  
                            Evita compras impulsivas y aprovecha ofertas. ¡Ahorra dinero!  
                            """.trimIndent(),
                category = "COMPRAS",
            ),
            Note(
                id = "2",
                title = "Proyecto Nuevo 🚀",
                content =
                """
                            Para iniciar un proyecto exitoso, sigue estos pasos:  
                            1️⃣ Define objetivos claros 🎯  
                            2️⃣ Establece un plan de acción 📅  
                            3️⃣ Asigna tareas y plazos ⏳  
                            4️⃣ Evalúa avances y ajusta estrategias 🔄  
                            ¡La organización es clave para el éxito!  
                            """.trimIndent(),
                category = "TRABAJO",
            ),
        )

        // Stubear la propiedad notesFlow para que emita la lista de notas de prueba
        coEvery { mockNoteRepository.notesFlow } returns flowOf(notesList)

        // Assert: Verifica que el estado del ViewModel contenga la lista de notas emitida
        assertEquals(notesList, viewModel.state.notes)

    }
}