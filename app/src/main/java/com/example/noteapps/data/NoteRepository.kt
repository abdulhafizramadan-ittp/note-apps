package com.example.noteapps.data

import androidx.lifecycle.LiveData
import com.example.noteapps.data.local.dao.NoteDao
import com.example.noteapps.data.local.entity.Note
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NoteRepository(private val noteDao: NoteDao) : NoteDataSource {

    private val executorService: ExecutorService =
        Executors.newSingleThreadExecutor()

    override fun getAllNotes(): LiveData<List<Note>> =
        noteDao.getAllNotes()

    override fun insertNote(note: Note): Unit =
        executorService.execute { noteDao.insert(note) }

    override fun updateNote(note: Note): Unit =
        executorService.execute { noteDao.update(note) }

    override fun deleteNote(note: Note): Unit =
        executorService.execute { noteDao.delete(note) }

}