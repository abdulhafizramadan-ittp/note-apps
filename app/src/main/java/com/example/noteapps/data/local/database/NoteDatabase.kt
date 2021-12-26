package com.example.noteapps.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.noteapps.data.local.dao.NoteDao
import com.example.noteapps.data.local.entity.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private const val DATABASE_NAME = "note_database"

        @Volatile
        private var instance: NoteDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): NoteDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DATABASE_NAME
                ).build()
                    .apply { instance = this }
            }
    }
}