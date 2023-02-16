package com.example.students.utils

import android.widget.EditText

fun EditText.cursorToEnd() {
    this.setSelection(this.text.length)
}

fun String.phoneNumberToRaw(): String{
    var result = this.replace("+7", "7")
    result = result.replace("(", "")
    result = result.replace(")", "")
    result = result.replace(" ", "")
    return result
}