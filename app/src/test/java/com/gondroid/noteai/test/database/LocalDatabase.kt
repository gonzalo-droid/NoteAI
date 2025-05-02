package com.gondroid.noteai.test.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.gondroid.noteai.data.NotesDatabase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
abstract class LocalDatabase {

    lateinit var db: NotesDatabase

    @Before
    fun initDB() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NotesDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDB() {
        db.close()
    }
}
