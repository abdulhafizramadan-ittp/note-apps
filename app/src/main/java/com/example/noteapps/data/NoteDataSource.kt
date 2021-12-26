package com.example.noteapps.data

import androidx.lifecycle.LiveData
import com.example.noteapps.data.local.entity.Note

interface NoteDataSource {

    fun getAllNotes(): LiveData<List<Note>>

    fun insertNote(note: Note)

    fun updateNote(note: Note)

    fun deleteNote(note: Note)
}