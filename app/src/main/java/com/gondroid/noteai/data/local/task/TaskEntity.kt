package com.gondroid.noteai.data.local.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gondroid.noteai.domain.Task
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = "note_id")
    val noteId: String,
    val title: String,
    val description: String?,
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean,
    val date: Long
) {
    companion object {
        fun fromTask(task: Task): TaskEntity =
            TaskEntity(
                id = task.id,
                noteId = task.noteId,
                title = task.title,
                description = task.description,
                isCompleted = task.isCompleted,
                date =
                task.date
                    .atZone(
                        ZoneId.systemDefault()
                    ).toInstant()
                    .toEpochMilli()
            )
    }

    fun toTask(): Task =
        Task(
            id = id,
            noteId = noteId,
            title = title,
            description = description,
            isCompleted = isCompleted,
            date =
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(date),
                ZoneId.systemDefault()
            )
        )
}
