package com.gondroid.noteai.data.local.voiceRecorder

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gondroid.noteai.domain.VoiceRecorder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "voice_recorder")
data class VoiceRecorderEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = "note_id")
    val noteId: String,
    val name: String?,
    val path: String,
    val transcription: String?,
    val date: Long
) {
    companion object {
        fun fromVoiceRecorder(voiceRecorder: VoiceRecorder): VoiceRecorderEntity =
            VoiceRecorderEntity(
                id = voiceRecorder.id,
                noteId = voiceRecorder.noteId,
                name = voiceRecorder.name,
                path = voiceRecorder.path,
                transcription = voiceRecorder.transcription,
                date =
                voiceRecorder.date
                    .atZone(
                        ZoneId.systemDefault()
                    ).toInstant()
                    .toEpochMilli()
            )
    }

    fun toVoiceRecorder(): VoiceRecorder =
        VoiceRecorder(
            id = id,
            noteId = noteId,
            name = name,
            path = path,
            transcription = transcription,
            date =
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(date),
                ZoneId.systemDefault()
            )
        )
}
