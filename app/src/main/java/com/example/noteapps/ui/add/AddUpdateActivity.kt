package com.example.noteapps.ui.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.noteapps.R
import com.example.noteapps.data.local.entity.Note
import com.example.noteapps.databinding.ActivityAddUpdateBinding
import com.example.noteapps.helper.DateHelper
import com.example.noteapps.helper.isEmpty
import com.example.noteapps.helper.toText
import com.example.noteapps.ui.NoteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddUpdateActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateBinding

    private val noteViewModel: NoteViewModel by viewModel()

    private var isEdit = false
    private var note: Note? = null
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        note = intent.getParcelableExtra(EXTRA_NOTE)

        if (note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            note = Note()
        }

        if (isEdit) {
            binding.apply {
                edtTitle.setText(note?.title)
                edtDescription.setText(note?.description)
            }
        }

        val actionBarTitle = if (isEdit) getString(R.string.change) else getString(R.string.add)
        val btnTitle = if (isEdit) getString(R.string.change) else getString(R.string.save)

        supportActionBar?.apply {
            title = actionBarTitle
            setDisplayHomeAsUpEnabled(true)
        }

        binding.btnSubmit.apply {
            text = btnTitle
            setOnClickListener(this@AddUpdateActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val alertDialogType = when (item.itemId) {
            R.id.action_delete -> ALERT_DIALOG_DELETE
            android.R.id.home -> ALERT_DIALOG_CLOSE
            else -> 0
        }
        showAlertDialog(alertDialogType)
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog(dialogType: Int) {
        val isDialogClose = dialogType == ALERT_DIALOG_CLOSE

        val dialogTitle = if (isDialogClose) R.string.cancel else R.string.message_cancel
        val dialogMessage = if (isDialogClose) R.string.delete else R.string.message_delete

        val alertDialog = AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(R.string.yes) { _, _ ->
                if (!isDialogClose) {
                    noteViewModel.deleteNote(note as Note)
                    val intent = Intent().apply {
                        putExtra(EXTRA_POSITION, position)
                    }
                    setResult(RESULT_DELETE, intent)
                }
                finish()
            }
            .create()

        alertDialog.show()
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_submit) {
            val listEditText = arrayOf(binding.edtTitle, binding.edtDescription)

            listEditText.forEach { editText ->
                if (editText.isEmpty()) {
                    editText.error = getString(R.string.empty)
                    return@onClick
                }
            }

            val title = binding.edtTitle.toText()
            val description = binding.edtDescription.toText()

            note?.let {
                it.title = title
                it.description = description
            }

            val intent = Intent().apply {
                putExtra(EXTRA_NOTE, note)
                putExtra(EXTRA_POSITION, position)
            }

            if (isEdit) {
                noteViewModel.updateNote(note as Note)
                setResult(RESULT_UPDATE, intent)
                finish()
            } else {
                note?.date = DateHelper.getCurrentDate()
                noteViewModel.insertNote(note as Note)
                setResult(RESULT_ADD, intent)
                finish()
            }
        }
    }

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val RESULT_ADD = 201
        const val RESULT_UPDATE = 301
        const val RESULT_DELETE = 401
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }
}