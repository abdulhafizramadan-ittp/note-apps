package com.example.noteapps.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapps.data.local.entity.Note
import com.example.noteapps.databinding.ItemNoteBinding
import com.example.noteapps.helper.NoteDiffCallback
import com.example.noteapps.ui.add.AddUpdateActivity

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val listNotes = arrayListOf<Note>()

    fun setNotes(listNotes: List<Note>) {
        val diffCallback = NoteDiffCallback(this.listNotes, listNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.listNotes.apply {
            clear()
            addAll(listNotes)
        }

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    override fun getItemCount(): Int =
        listNotes.size

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.apply {
                tvItemTitle.text = note.title
                tvItemDescription.text = note.description
                tvItemDate.text = note.date

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, AddUpdateActivity::class.java).apply {
                        putExtra(AddUpdateActivity.EXTRA_POSITION, adapterPosition)
                        putExtra(AddUpdateActivity.EXTRA_NOTE, note)
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}