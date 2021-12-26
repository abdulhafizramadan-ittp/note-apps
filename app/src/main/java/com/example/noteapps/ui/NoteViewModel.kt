package com.example.noteapps.ui

import androidx.lifecycle.ViewModel
import com.example.noteapps.data.NoteDataSource
import com.example.noteapps.data.NoteRepository

class NoteViewModel(
    private val noteRepository: NoteRepository
) : ViewModel(), NoteDataSource by noteRepository