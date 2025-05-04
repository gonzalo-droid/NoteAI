package com.gondroid.noteai.presentation.screens.noteCreate

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.gondroid.noteai.domain.Note
import com.gondroid.noteai.domain.VoiceRecorder
import com.gondroid.noteai.domain.repository.NoteLocalDataSource
import com.gondroid.noteai.domain.repository.VoiceRecorderLocalDataSource
import com.gondroid.noteai.presentation.navigation.NoteCreateScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NoteCreateViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val localDataSource: NoteLocalDataSource,
    private val voiceRecorderLocalDataSource: VoiceRecorderLocalDataSource
) : ViewModel() {
    var state by mutableStateOf(NoteCreateScreenState())
        private set

    private var eventChannel = Channel<NoteCreateEvent>()

    // de channel to flow, UI event listening event
    val event = eventChannel.receiveAsFlow()
    private val canSaveNote = snapshotFlow { state.title.text.toString() }
    private val noteData: NoteCreateScreenRoute = savedStateHandle.toRoute<NoteCreateScreenRoute>()
    private var editedNote: Note? = null

    private val _recordedFilePath = MutableStateFlow<String?>(null)
    val recordedFilePath: StateFlow<String?> = _recordedFilePath

    fun updateRecordedFilePath(filePath: String?) {
        _recordedFilePath.value = filePath
    }

    init {
        state = state.copy(noteId = noteData.noteId ?: UUID.randomUUID().toString())
        println("SavedStateHandle keys: ${savedStateHandle.keys()}")
        println("SavedStateHandle noteData: ${savedStateHandle.get<NoteCreateScreenRoute>("route")}")
        println("noteData: ${noteData.noteId}")

        noteData.noteId?.let { noteId ->
            println("get noteId: $noteId")
            viewModelScope.launch {
                localDataSource.getNoteById(noteId)?.let { note ->
                    editedNote = note
                    state =
                        state.copy(
                            title = TextFieldState(note.title),
                            content = TextFieldState(note.content ?: ""),
                            category = note.category?.toString()
                        )
                }
            }

            voiceRecorderLocalDataSource.voiceRecordingsFlow
                .onEach { voiceRecordings ->
                    state =
                        state.copy(
                            voiceRecordings = voiceRecordings.filter { it.noteId == noteId }
                        )
                }.launchIn(viewModelScope)
        }

        canSaveNote
            .onEach {
                state = state.copy(canSaveNote = it.isNotEmpty())
            }.launchIn(viewModelScope)
    }

    fun saveAudioNoteToDatabase(filePath: String) {
        updateRecordedFilePath(null)
        editedNote?.id?.let { noteId ->
            viewModelScope.launch {
                val voiceRecorder =
                    VoiceRecorder(
                        id = UUID.randomUUID().toString(),
                        noteId = noteId,
                        name = "Note Voice",
                        path = filePath,
                        transcription = null
                    )
                voiceRecorderLocalDataSource.addVoiceRecorder(voiceRecorder)
                eventChannel.send(NoteCreateEvent.SaveVoiceRecorder)
            }
        }
    }

    fun onAction(action: ActionNoteCreate) {
        viewModelScope.launch {
            when (action) {
                is ActionNoteCreate.ChangeNoteCategory -> {
                    state = state.copy(category = action.category.toString())
                }

                is ActionNoteCreate.SaveNote -> {
                    editedNote?.let {
                        this@NoteCreateViewModel.localDataSource.updateNote(
                            updatedNote =
                            it.copy(
                                id = it.id,
                                title = state.title.text.toString(),
                                content = state.content.text.toString(),
                                category = state.category
                            )
                        )
                    } ?: run {
                        state.noteId?.let {
                            val note =
                                Note(
                                    id = it,
                                    title = state.title.text.toString(),
                                    content = state.content.text.toString(),
                                    category = state.category
                                )
                            localDataSource.addNote(
                                note = note
                            )
                        }
                    }
                    eventChannel.send(NoteCreateEvent.NoteCreated)
                }

                is ActionNoteCreate.SaveVoiceRecorder -> {
                    val updatedRecordings =
                        state.voiceRecordings.map { record ->
                            if (record.id == action.recordId) {
                                val updatedRecord =
                                    record.copy(transcription = action.transcription)
                                voiceRecorderLocalDataSource.updateVoiceRecorder(updatedRecord)
                                updatedRecord
                            } else {
                                record
                            }
                        }

                    state = state.copy(voiceRecordings = updatedRecordings)

                    eventChannel.send(NoteCreateEvent.TranscriptionUpdate)
                }

                else -> Unit
            }
        }
    }
}
