package com.example.students.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class PhoneTextWatcher(
    val context: Context,
    private val editText: EditText,
    val onTextChanged: (String) -> Unit
) : TextWatcher {

    private var showSoftInputOnFocus = false

    private var phoneNumberFormatted = StringBuilder()
    private var phoneNumberRaw = StringBuilder()
    private var mask = "XX (XXX) XXX XX XX"

    private var isDelete = false

    override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (number.toString() != phoneNumberFormatted.toString()) {
            type(number.toString(), number.toString().length)
            editText.requestFocus()
            editText.cursorToEnd()
        }
    }

    fun type(digit: String, position: Int = editText.selectionEnd) {
        if (digit.length == 1 && position >= mask.length)
            return

        if (editText.text?.isEmpty() == true) {
            phoneNumberRaw = StringBuilder()
            phoneNumberFormatted = StringBuilder()
            return
        }

        if(digit == "+7 ("){
            phoneNumberFormatted = StringBuilder()
            setNumber(phoneNumberFormatted.toString(), 0)
            return
        }

        val inputString = formatInput(digit)

        if (isTypeEnable(position)) {
            var validPosition = if (position < mask.length) position else mask.length - 1
            if (validPosition < 2) validPosition = 2

            insertToRawNumber(inputString, validPosition)
            applyMask()
            setNumber(phoneNumberFormatted.toString(), calculateCursorPosition(validPosition))
        }
        isDelete = false
    }

    private fun calculateCursorPosition(position: Int): Int {
        return if (!isDelete)
            calculateCursorPositionForInsert(position)
        else
            calculateCursorPositionForDelete(position)
    }

    private fun calculateCursorPositionForInsert(position: Int): Int {
        val formattedNumber = StringBuilder()

        var rawTotal = 0
        for (i in 0 until position) {
            if (mask[i] == 'X')
                rawTotal++
        }

        var rawIndex = 0
        for (charInMask in mask) {
            if (rawIndex > rawTotal)
                break
            if (charInMask == 'X') {
                if (rawIndex < phoneNumberRaw.length)
                    formattedNumber.append(phoneNumberRaw[rawIndex])
                rawIndex++
            } else {
                formattedNumber.append(charInMask)
            }
        }

        return formattedNumber.length
    }

    private fun calculateCursorPositionForDelete(position: Int): Int {
        val formattedNumber = StringBuilder()

        var rawTotal = 0
        for (i in 0 until position + 1) {
            if (mask[i] == 'X')
                rawTotal++
        }

        var rawIndex = 0
        for (charInMask in mask) {
            if (rawIndex >= rawTotal)
                break
            if (charInMask == 'X') {
                if (rawIndex < phoneNumberRaw.length)
                    formattedNumber.append(phoneNumberRaw[rawIndex])
                rawIndex++
            } else {
                formattedNumber.append(charInMask)
            }
        }

        return formattedNumber.length
    }

    private fun setNumber(phoneNumber: String, cursorPosition: Int) {
        editText.setText(phoneNumber)
        editText.requestFocus()
        if (cursorPosition < phoneNumber.length)
            editText.setSelection(cursorPosition)
        else
            editText.cursorToEnd()

        onTextChanged.invoke(phoneNumber)
    }


    private fun applyMask() {
        phoneNumberFormatted = StringBuilder()
        var i = 0
        for (charInMask in mask) {
            if (i == phoneNumberRaw.length)
                break
            if (charInMask == 'X') {
                phoneNumberFormatted.append(phoneNumberRaw[i])
                i++
            } else {
                phoneNumberFormatted.append(charInMask)
            }
        }
    }

    private fun insertToRawNumber(inputString: String, position: Int) {
        if (phoneNumberRaw.isNotEmpty()) {
            val rawPosition = calculateRawPositionForInsert(position)
            if (phoneNumberFormatted.length < mask.length) {
                phoneNumberRaw.insert(rawPosition, inputString)
            } else {
                var p = rawPosition
                for (d in inputString) {
                    phoneNumberRaw.setCharAt(p, d)
                    p++
                }
            }
        } else phoneNumberRaw.append(inputString)
    }

    private fun calculateRawPositionForInsert(position: Int): Int {
        val rawNumber = StringBuilder()
        for (i in 0 until position) {
            if (i < mask.length && mask[i] == 'X') {
                if (i < phoneNumberFormatted.length)
                    rawNumber.append(phoneNumberFormatted[i])
            }
        }
        return rawNumber.length
    }

    private fun isTypeEnable(position: Int): Boolean {
        return position >= 1 || showSoftInputOnFocus || editText.text?.isEmpty() == true
    }

    private fun formatInput(input: String): String {
        var inputBuilder = java.lang.StringBuilder(input)
        return if (inputBuilder.isNotEmpty() || showSoftInputOnFocus) {
            phoneNumberRaw = StringBuilder()

            var str = inputBuilder.toString()
                .replace("+7", "")
                .replace("+", "")
                .replace(" ", "")
                .replace(")", "")
                .replace("(", "")
                .replace("-", "")
                .replace("/", "")
            if (str.startsWith("8")) str = str.replaceFirst("8", "")
            inputBuilder = StringBuilder(str)
            inputBuilder.insert(0, "+7")

            if (inputBuilder.length > 12)
                inputBuilder.substring(0, 12)
            else
                inputBuilder.toString()
        } else {
            if (phoneNumberRaw.isEmpty()) {
                inputBuilder.insert(0, "+7")
            } else {
                inputBuilder.insert(0, "")
            }
            inputBuilder.toString()
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
    }
}