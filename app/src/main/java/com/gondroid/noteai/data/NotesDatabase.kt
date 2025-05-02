package com.gondroid.noteai.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gondroid.noteai.data.local.note.NoteDao
import com.gondroid.noteai.data.local.note.NoteEntity
import com.gondroid.noteai.data.local.task.TaskDao
import com.gondroid.noteai.data.local.task.TaskEntity
import com.gondroid.noteai.data.local.voiceRecorder.VoiceRecorderDao
import com.gondroid.noteai.data.local.voiceRecorder.VoiceRecorderEntity

@Database(
    entities = [
        TaskEntity::class,
        NoteEntity::class,
        VoiceRecorderEntity::class
    ],
    version = 1
)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    abstract fun noteDao(): NoteDao

    abstract fun voiceRecorderDao(): VoiceRecorderDao
}
