package com.example.students.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.example.students.utils.ui.SafeClickListener

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

fun String.phoneNumberToRaw(): String{
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