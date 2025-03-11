package com.gondroid.noteai.presentation.screens.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gondroid.noteai.domain.repository.NoteLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class NoteScreenViewModel
@Inject
constructor(
    private val noteLocalDataSource: NoteLocalDataSource,
) : ViewModel() {
    var state by mutableStateOf(NoteDataState())
        private set

    private val eventChannel = Channel<NoteScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        state =
            state.copy(
                date =
                LocalDate.now().let {
                    DateTimeFormatter.ofPattern("EEEE, MMMM dd yyyy").format(it)
                },
            )

        noteLocalDataSource.notesFlow
            .onEach { notes ->
                state =
                    state.copy(
                        notes = notes,
                    )
            }.launchIn(viewModelScope)
    }
}
