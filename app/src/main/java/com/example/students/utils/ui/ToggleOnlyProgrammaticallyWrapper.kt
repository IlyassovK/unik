package com.example.students.utils.ui

import android.widget.CompoundButton
import android.widget.ToggleButton

class ToggleOnlyProgrammaticallyWrapper(val toggleButton: ToggleButton) {

    private var listener: CompoundButton.OnCheckedChangeListener? = null

    fun setListener(onCheckedChanged: CompoundButton.OnCheckedChangeListener) {
        toggleButton.setInterceptedListener(onCheckedChanged)
    }

    fun setCheckedWithoutListener(isChecked: Boolean){
        toggleButton.setCheckedWithoutListener(isChecked)
    }

    /**
     * Use this if you want your compoundButton to do something when (un) checked,
     * but not change it's state by itself.
     *
     * It will block the actual changing of the button, but instead run [onCheckedChanged]
     * which should in turn eventually lead to setting `isChecked` to get proper setting of the switch
     * after its action has been performed
     * This might cause some trouble as it will trigger on programmatic sets outside of this listener.
     * To get around that, use [setCheckedWithoutListener] if you don't want that to happen.
     */
    private fun CompoundButton.setInterceptedListener(onCheckedChanged: CompoundButton.OnCheckedChangeListener) {
        listener = onCheckedChanged
        setOnCheckedChangeListener { compoundButton, b ->
            compoundButton.isChecked = !compoundButton.isChecked
            onCheckedChanged.onCheckedChanged(compoundButton, b)
        }
    }

    /**
     * Set value without triggering the listener added in [setInterceptedListener]
     */
    private fun CompoundButton.setCheckedWithoutListener(isChecked: Boolean) {
        listener?.let { l ->
            setOnCheckedChangeListener(null)
            this.isChecked = isChecked
            setOnCheckedChangeListener(l)
        }
    }
}