package com.example.students.utils.ui.customViews

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.students.R


class ProgressButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    @LayoutRes
    private var layout: Int = R.layout.progress_button

    private lateinit var mainBtn: Button
    private lateinit var payProgress: ProgressBar

    private lateinit var buttonText: String
    private var btnEnabled: Boolean = false

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton)
        initAttrs(typedArray)

        val view = inflate(context, layout, null)
        addView(view)
        findViews()
        updateView()
    }

    private fun findViews() {
        mainBtn = findViewById(R.id.main_btn)
        payProgress = findViewById(R.id.pay_progress)
        mainBtn.elevation = 0f
    }

    private fun updateView() {
        mainBtn.isEnabled = btnEnabled
        mainBtn.text = buttonText
    }

    private fun initAttrs(typedArray: TypedArray) {
        layout = typedArray.getResourceId(R.styleable.ProgressButton_layout, R.layout.progress_button)
        btnEnabled = typedArray.getBoolean(R.styleable.ProgressButton_android_enabled, true)
        buttonText = typedArray.getString(R.styleable.ProgressButton_android_text)
            ?: resources.getString(R.string.msg_continue)
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        mainBtn.setOnClickListener(listener)
    }

    override fun isEnabled(): Boolean {
        return mainBtn.isEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        mainBtn.isEnabled = enabled
    }

    fun setText(text: String) {
        this.buttonText = text
        mainBtn.text = text
    }

    fun setText(@StringRes textResource: Int) {
        setText(resources.getString(textResource))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setBackground(@DrawableRes background: Int) {
        mainBtn.background = resources.getDrawable(background, null)
    }

    fun setContentColor(@ColorRes contentColorResource: Int) {
        val contentColor = ContextCompat.getColor(context, contentColorResource)
        mainBtn.setTextColor(contentColor)
        payProgress.indeterminateTintList = ColorStateList.valueOf(contentColor)
    }

    fun showLoading() {
        payProgress.isVisible = true
        mainBtn.text = ""
        mainBtn.isEnabled = false
    }

    fun showNormal() {
        payProgress.isVisible = false
        mainBtn.text = buttonText
        mainBtn.isEnabled = true
    }
}