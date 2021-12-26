package com.example.noteapps.di

import com.example.noteapps.data.NoteRepository
import com.example.noteapps.data.local.database.NoteDatabase
import com.example.noteapps.ui.NoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val noteModules = module {
    single { NoteDatabase.getInstance(get()).noteDao() }
    single { NoteRepository(get()) }

    viewModel { NoteViewModel(get()) }
}