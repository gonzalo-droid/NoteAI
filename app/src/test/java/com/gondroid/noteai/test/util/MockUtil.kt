package com.gondroid.noteai.test.util

import com.gondroid.noteai.domain.Note
import com.gondroid.noteai.domain.Task
import java.time.LocalDateTime

object MockUtil {

    fun mockNote(
        id: String = "1",
        title: String = "Nota 1",
        content: String = "Contenido de la nota 1",
        category: String = "Personal"
    ) = Note(
        id = id,
        title = title,
        content = content,
        category = category,
        date = LocalDateTime.now()
    )

    fun mockNoteList() = listOf(mockNote())

    fun mockTask(
        id: String = "1",
        noteId: String = "1",
        title: String = "Tarea 1",
        description: String = "Descripción de la tarea 1",
        isCompleted: Boolean = false
    ) = Task(
        id = id,
        noteId = noteId,
        title = title,
        description = description,
        isCompleted = isCompleted,
        date = LocalDateTime.now()
    )
}
