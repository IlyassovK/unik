package com.example.students.utils.ui.customViews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class DigitEditText(context: Context?, attrs: AttributeSet?) : AppCompatEditText(context!!, attrs) {

    interface Listener {
        fun onTextPaste()
    }

    var listener: Listener? = null

    init {
        setTextIsSelectable(true)
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        val consumed = super.onTextContextMenuItem(id)
        when (id) {
            android.R.id.paste -> listener?.onTextPaste()
        }
        return consumed
    }
}