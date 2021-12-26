package com.example.noteapps.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapps.R
import com.example.noteapps.databinding.ActivityMainBinding
import com.example.noteapps.ui.NoteViewModel
import com.example.noteapps.ui.add.AddUpdateActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter

    private val noteViewModel: NoteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteAdapter = NoteAdapter()

        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddUpdateActivity::class.java)
            resultLauncher.launch(intent)
        }

        observeNotes()
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val message = when (result.resultCode) {
            AddUpdateActivity.RESULT_ADD -> R.string.added
            AddUpdateActivity.RESULT_UPDATE -> R.string.changed
            AddUpdateActivity.RESULT_DELETE -> R.string.deleted
            else -> R.string.cancel_action
        }
        Snackbar.make(binding.root, getString(message), Snackbar.LENGTH_SHORT).show()
    }

    private fun observeNotes() {
        noteViewModel.getAllNotes().observe(this) { listNotes ->
            if (listNotes != null) {
                noteAdapter.setNotes(listNotes)
            }
        }
    }
}