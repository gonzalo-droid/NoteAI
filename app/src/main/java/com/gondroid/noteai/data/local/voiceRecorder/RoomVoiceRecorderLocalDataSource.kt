package com.gondroid.noteai.data.local.voiceRecorder

import com.gondroid.noteai.domain.VoiceRecorder
import com.gondroid.noteai.domain.repository.VoiceRecorderLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomVoiceRecorderLocalDataSource
@Inject
constructor(
    private val voiceRecorderDao: VoiceRecorderDao,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO
) : VoiceRecorderLocalDataSource {
    override val voiceRecordingsFlow: Flow<List<VoiceRecorder>>
        get() =
            voiceRecorderDao
                .getAllVoiceRecordings()
                .map {
                    it.map { voiceRecorderEntity -> voiceRecorderEntity.toVoiceRecorder() }
                }.flowOn(dispatcherIO)

    override suspend fun addVoiceRecorder(voiceRecorder: VoiceRecorder) =
        withContext(dispatcherIO) {
            voiceRecorderDao.upsertVoiceRecorder(VoiceRecorderEntity.fromVoiceRecorder(voiceRecorder))
        }

    override suspend fun updateVoiceRecorder(updatedVoiceRecorder: VoiceRecorder) =
        withContext(dispatcherIO) {
            voiceRecorderDao.upsertVoiceRecorder(
                VoiceRecorderEntity.fromVoiceRecorder(
                    updatedVoiceRecorder
                )
            )
        }

    override suspend fun removeVoiceRecorder(voiceRecorder: VoiceRecorder) =
        withContext(dispatcherIO) {
            voiceRecorderDao.deleteVoiceRecorderById(voiceRecorder.id)
        }

    override suspend fun deleteAllVoiceRecorders() =
        withContext(dispatcherIO) {
            voiceRecorderDao.deleteAllVoiceRecorders()
        }

    override suspend fun getVoiceRecorderById(id: String): VoiceRecorder? =
        withContext(dispatcherIO) {
            voiceRecorderDao.getVoiceRecorderById(id)?.toVoiceRecorder()
        }

    override suspend fun getVoiceRecorderByNoteId(noteId: String): VoiceRecorder? =
        withContext(dispatcherIO) {
            voiceRecorderDao.getVoiceRecorderByNoteId(noteId)?.toVoiceRecorder()
        }
}
