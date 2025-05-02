package com.gondroid.noteai.data.local.note

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gondroid.noteai.domain.Note
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val content: String?,
    val category: String? = null,
    val date: Long
) {
    companion object {
        fun fromNote(note: Note): NoteEntity =
            NoteEntity(
                id = note.id,
                title = note.title,
                content = note.content,
                category = note.category,
                date =
                note.date
                    .atZone(
                        ZoneId.systemDefault()
                    ).toInstant()
                    .toEpochMilli()
            )
    }

    fun toNote(): Note =
        Note(
            id = id,
            title = title,
            content = content,
            category = category,
            date =
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(date),
                ZoneId.systemDefault()
            )
        )
}
