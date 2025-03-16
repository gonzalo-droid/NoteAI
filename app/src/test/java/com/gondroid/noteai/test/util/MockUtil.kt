package com.gondroid.noteai.test.util

import com.gondroid.noteai.domain.Note
import java.time.LocalDateTime


object MockUtil {

    fun mockNote(
        id: String = "1",
        title: String = "Nota 1",
        content: String = "Contenido de la nota 1",
        category: String = "Personal",
    ) = Note(
        id = id,
        title = title,
        content = content,
        category = category,
        date = LocalDateTime.now()
    )

    fun mockNoteList() = listOf(mockNote())

}