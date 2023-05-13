package com.example.students.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.students.R
import com.example.students.utils.ui.SafeClickListener
import java.sql.Timestamp

//EditText
fun EditText.cursorToEnd() {
    this.setSelection(this.text.length)
}

fun EditText.onTextChanged(
    afterTextChanged: ((String) -> Unit)? = null,
    beforeTextChanged: ((String) -> Unit)? = null,
    onTextChanged: ((String) -> Unit)? = null
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged?.invoke(s.toString())
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged?.invoke(s.toString())
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged?.invoke(editable.toString())
        }
    })
}

fun String.phoneNumberToRaw(): String {
    var result = this.replace("+7", "7")
    result = result.replace("(", "")
    result = result.replace(")", "")
    result = result.replace(" ", "")
    return result
}

// Prevent double click
fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }

    setOnClickListener(safeClickListener)
}

fun Timestamp.parse(): String {
    return "${hours.toString()}:${minutes.toString()}"
}

fun ImageView.setImage(url: String) {
    Glide
        .with(this)
        .load(url)
        .placeholder(R.drawable.ic_empty_avatar)
        .into(this)
}