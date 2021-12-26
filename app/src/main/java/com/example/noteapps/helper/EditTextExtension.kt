package com.example.noteapps.helper

import android.widget.EditText

fun EditText.isEmpty(): Boolean =
    text.isEmpty()

fun EditText.toText(): String =
    text.trim().toString()