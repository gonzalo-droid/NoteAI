package com.gondroid.noteai.data.di

import android.content.Context
import androidx.room.Room
import com.gondroid.noteai.data.NotesDatabase
import com.gondroid.noteai.data.local.note.NoteDao
import com.gondroid.noteai.data.local.note.RoomNoteLocalDataSource
import com.gondroid.noteai.data.local.task.RoomTaskLocalDataSource
import com.gondroid.noteai.data.local.task.TaskDao
import com.gondroid.noteai.data.local.voiceRecorder.RoomVoiceRecorderLocalDataSource
import com.gondroid.noteai.data.local.voiceRecorder.VoiceRecorderDao
import com.gondroid.noteai.domain.repository.NoteLocalDataSource
import com.gondroid.noteai.domain.repository.TaskLocalDataSource
import com.gondroid.noteai.domain.repository.VoiceRecorderLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext
        context: Context
    ): NotesDatabase =
        Room
            .databaseBuilder(
                context.applicationContext,
                NotesDatabase::class.java,
                "notes_database"
            )
            // Esto elimina y recrea la base de datos en cambios de esquema
            // solo para entorno de prueba
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideTaskDao(database: NotesDatabase): TaskDao = database.taskDao()

    @Provides
    @Singleton
    fun provideNoteDao(database: NotesDatabase): NoteDao = database.noteDao()

    @Provides
    @Singleton
    fun provideVoiceRecorderDao(database: NotesDatabase): VoiceRecorderDao = database.voiceRecorderDao()

    @Provides
    @Singleton
    fun provideTaskLocalDataSource(
        taskDao: TaskDao,
        @Named("dispatcherIO")
        dispatcherIO: CoroutineDispatcher
    ): TaskLocalDataSource = RoomTaskLocalDataSource(taskDao, dispatcherIO)

    @Provides
    @Singleton
    fun provideNoteLocalDataSource(
        noteDao: NoteDao,
        @Named("dispatcherIO")
        dispatcherIO: CoroutineDispatcher
    ): NoteLocalDataSource = RoomNoteLocalDataSource(noteDao, dispatcherIO)

    @Provides
    @Singleton
    fun provideVoiceRecorderLocalDataSource(
        voiceRecorderDao: VoiceRecorderDao,
        @Named("dispatcherIO")
        dispatcherIO: CoroutineDispatcher
    ): VoiceRecorderLocalDataSource = RoomVoiceRecorderLocalDataSource(voiceRecorderDao, dispatcherIO)
}
