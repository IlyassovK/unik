package com.example.students.utils.ui.customViews

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.autofill.AutofillManager
import android.widget.FrameLayout
import androidx.autofill.HintConstants.AUTOFILL_HINT_SMS_OTP
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.students.R
import com.example.students.databinding.LayoutOtpViewBinding
import com.example.students.utils.onTextChanged
import com.example.students.utils.ui.Utils


class OtpView : FrameLayout {

    private lateinit var binding: LayoutOtpViewBinding

    enum class OtpType {
        AUTHORIZED, UNAUTHORIZED, APPLICATION, REGISTRATION, BIOMETRY_RECOVERY, REGISTRATION_PRODUCT, PRODUCT_RECOVERY
    }

    enum class DigitStyle {
        LARGE, SMALL
    }

    data class OtpParams(val length: Int, val digitStyle: DigitStyle) {
        companion object {
            fun build(otpType: OtpType): OtpParams {
                return when (otpType) {
                    OtpType.AUTHORIZED, OtpType.APPLICATION -> OtpParams(6, DigitStyle.SMALL)
                    OtpType.UNAUTHORIZED -> OtpParams(4, DigitStyle.LARGE)
                    OtpType.REGISTRATION -> OtpParams(4, DigitStyle.LARGE)
                    OtpType.BIOMETRY_RECOVERY -> OtpParams(4, DigitStyle.LARGE)
                    OtpType.REGISTRATION_PRODUCT -> OtpParams(4, DigitStyle.LARGE)
                    OtpType.PRODUCT_RECOVERY -> OtpParams(4, DigitStyle.LARGE)
                }
            }
        }
    }

    interface Listener {
        fun onStateChanged(isCompleted: Boolean)
    }

    var listener: Listener? = null

    private var type: OtpType = OtpType.UNAUTHORIZED
    private var digitStyle: DigitStyle = DigitStyle.SMALL
    private var otpLength = 6

    private var isAutoFillEnable = false
    private var isFocusEnabled = true

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        parseAttrs(attrs)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        parseAttrs(attrs)
        initView()
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    private fun parseAttrs(attrs: AttributeSet?) {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.OtpView)
        isAutoFillEnable = a.getBoolean(R.styleable.OtpView_isAutofillEnable, true)
        a.recycle()


        val b =
            context.obtainStyledAttributes(attrs, R.styleable.OtpView)
        isFocusEnabled = a.getBoolean(R.styleable.OtpView_isFocusEnabled, true)
        b.recycle()
    }

    private fun initView() {

        binding = LayoutOtpViewBinding.inflate(LayoutInflater.from(context), this, true)

        binding.digitContainer.addDigits()
        binding.digitContainer.initDigits()

        binding.otpAutofill.showSoftInputOnFocus = false
        if (isAutoFillEnable) {
            setUpAutoFill()
        } else {
            if (isFocusEnabled)
                focusOnFirstDigit()
        }
    }

    fun setParams(otpParams: OtpParams) {
        digitStyle = otpParams.digitStyle
        otpLength = otpParams.length
        binding.digitContainer.addDigits()
        binding.digitContainer.initDigits()
        if (isFocusEnabled)
            focusOnFirstDigit()
    }

    fun setOtpCode(code: String) {
        code.forEachIndexed { index, digit ->
            val digitView = getDigitContainer().getChildAt(index) as DigitEditText
            digitView.setText(digit.toString())
            digitView.isEnabled = false
        }
        listener?.onStateChanged(isCompleted = true)
    }

    fun type(digit: String) {
        getDigitContainer().typeDigit(digit)
    }

    fun typeNew(digit: String) {
        getDigitContainer().typeDigitNew(digit)
    }

    fun deleteLast() {
        getDigitContainer().deleteDigit()
    }


    fun deleteLastNew() {
        getDigitContainer().deleteDigitNew()
    }

    fun getCode(): String {
        val code = StringBuilder()
        val container = getDigitContainer()
        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            if (child is DigitEditText) {
                code.append(child.text)
            }
        }
        return code.toString()
    }

    fun clean(withAnimation: Boolean) {
        if (withAnimation) {
            val animShake = AnimationUtils.loadAnimation(this.context, R.anim.shake)
            animShake.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    clean()
                }

                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
            })
            val container = getDigitContainer()
            for (i in 0 until container.childCount) {
                val child = container.getChildAt(i)
                if (child is DigitEditText) {
                    child.startAnimation(animShake)
                }
            }
        } else {
            clean()
        }
    }

    fun cleanNew(withAnimation: Boolean) {
        if (withAnimation) {
            val animShake = AnimationUtils.loadAnimation(this.context, R.anim.shake)
            animShake.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    cleanNew()
                }

                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
            })
            val container = getDigitContainer()
            for (i in 0 until container.childCount) {
                val child = container.getChildAt(i)
                if (child is DigitEditText) {
                    child.startAnimation(animShake)
                }
            }
        } else {
            cleanNew()
        }
    }

    private fun focusOnFirstDigit() {
        val container = getDigitContainer()
        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            if (child is DigitEditText) {
                child.isEnabled = true
                child.setFocusableInTouchMode(true)
                child.requestFocus()
                break
            }
        }
    }

    private fun clean() {
        for (i in 0..otpLength) {
            getDigitContainer().deleteDigit()
        }
    }

    private fun cleanNew() {
        for (i in 0..otpLength) {
            getDigitContainer().deleteDigitNew()
        }
    }

    private fun getDigitContainer(): ViewGroup {
        return binding.digitContainer
    }

    private fun ViewGroup.deleteDigit() {
        for (i in this.childCount downTo 0) {
            val child = this.getChildAt(i)
            if (child is DigitEditText) {
                val current = child.text
                if (!current.isNullOrEmpty()) {
                    child.isEnabled = true
                    child.setFocusableInTouchMode(true)
                    child.setText("")
                    child.requestFocus()
                    if (i == this.childCount - 1) {
                        listener?.onStateChanged(isCompleted = false)
                    }
                    return
                } else {
                    if (i != 0) {
                        child.isEnabled = false
                        child.setFocusable(false)
                    }
                }
            }
        }
    }

    private fun ViewGroup.deleteDigitNew() {
        for (i in this.childCount downTo 0) {
            val child = this.getChildAt(i)
            if (child is DigitEditText) {
                val current = child.text
                if (!current.isNullOrEmpty()) {
                    child.isEnabled = true
                    child.setText("")
                    if (i == this.childCount - 1) {
                        listener?.onStateChanged(isCompleted = false)
                    }
                    return
                } else {
                    if (i != 0) {
                        child.isEnabled = false
                        child.setFocusable(false)
                    }
                }
            }
        }
    }

    private fun ViewGroup.typeDigit(digit: String) {
        for (i in 0 until this.childCount) {
            val child = this.getChildAt(i)
            if (child is DigitEditText) {
                val current = child.text
                if (current.isNullOrEmpty()) {
                    child.setText(digit)
                    move2NextDigit()
                    return
                }
            }
        }
    }

    private fun ViewGroup.typeDigitNew(digit: String) {
        for (i in 0 until this.childCount) {
            val child = this.getChildAt(i)
            if (child is DigitEditText) {
                val current = child.text
                if (current.isNullOrEmpty()) {
                    child.setText(digit)
                    checkIsFull()
                    return
                }
            }
        }
    }

    private fun ViewGroup.move2NextDigit() {
        for (i in 0 until this.childCount) {
            val child = this.getChildAt(i)
            if (child is DigitEditText) {
                val current = child.text
                if (current.isNullOrEmpty()) {
                    child.isEnabled = true
                    child.setFocusableInTouchMode(true)
                    child.requestFocus()
                    return
                } else {
                    child.isEnabled = false
                    child.setFocusable(false)
                    if (i == this.childCount - 1) {
                        listener?.onStateChanged(isCompleted = true)
                    }
                }
            }
        }
    }

    private fun ViewGroup.checkIsFull() {
        for (i in 0 until this.childCount) {
            val child = this.getChildAt(i)
            if (child is DigitEditText) {
                val current = child.text
                if (!current.isNullOrEmpty()) {
                    child.isEnabled = false
                    child.setFocusable(false)
                    if (i == this.childCount - 1) {
                        listener?.onStateChanged(isCompleted = true)
                    }
                }
            }
        }
    }

    private fun ConstraintLayout.addDigits() {
        val container = this
        val lf = LayoutInflater.from(context)

        val otpDigits = mutableListOf<View>()
        for (i in 0 until otpLength) {
            val layoutRes = when (digitStyle) {
                DigitStyle.LARGE -> R.layout.layout_otp_digit_large
                DigitStyle.SMALL -> R.layout.layout_otp_digit_small
            }
            otpDigits.add(lf.inflate(layoutRes, container, false))
        }

        container.removeAllViews()
        for (otpDigitView in otpDigits) {
            otpDigitView.id = View.generateViewId()
            container.addView(otpDigitView)
        }

        val constraintSet = ConstraintSet()
        constraintSet.clone(container)

        var previousItem: View? = null
        for (otpDigitView in otpDigits) {
            val lastItem = otpDigits.indexOf(otpDigitView) == otpDigits.size - 1
            if (previousItem == null) {
                constraintSet.connect(
                    otpDigitView.id,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.LEFT
                )
            } else {
                constraintSet.connect(
                    otpDigitView.id,
                    ConstraintSet.LEFT,
                    previousItem.id,
                    ConstraintSet.RIGHT
                )
                if (lastItem) {
                    constraintSet.connect(
                        otpDigitView.id,
                        ConstraintSet.RIGHT,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.RIGHT
                    )
                }
            }
            previousItem = otpDigitView
        }

        val viewIds: IntArray = otpDigits.map { obj: View -> obj.id }.toIntArray()

        constraintSet.createHorizontalChain(
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT,
            viewIds,
            null,
            ConstraintSet.CHAIN_SPREAD
        )
        constraintSet.applyTo(container)
    }

    private fun ViewGroup.initDigits() {
        val container = this
        for (i in 0 until container.childCount) {
            val digitView = container.getChildAt(i)
            if (digitView is DigitEditText) {
                digitView.showSoftInputOnFocus = false
                digitView.isEnabled = false
                digitView.setFocusableInTouchMode(false)
                digitView.listener = object : DigitEditText.Listener {
                    override fun onTextPaste() {
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        if (clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true) {
                            val item = clipboard.primaryClip!!.getItemAt(0)
                            val otpText = item.text.toString()
                            if (Utils.Validation.isValidOtp(otpText, otpLength))
                                setOtpCode(otpText)
                        }
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    digitView.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO)
                }
            }
        }
    }

    private fun setUpAutoFill() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.apply {
                otpAutofill.visibility = View.VISIBLE
                otpAutofill.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_YES
                otpAutofill.setAutofillHints(AUTOFILL_HINT_SMS_OTP)
                otpAutofill.onTextChanged(afterTextChanged = { otpCode ->
                    if (Utils.Validation.isValidOtp(otpCode, otpLength))
                        setOtpCode(otpCode)
                })

                val afm = context.getSystemService(AutofillManager::class.java)
                afm?.registerCallback(object : AutofillManager.AutofillCallback() {
                    override fun onAutofillEvent(view: View, event: Int) {
                        super.onAutofillEvent(view, event)
                        when (event) {
                            EVENT_INPUT_HIDDEN -> {
                                otpAutofill.visibility = View.INVISIBLE
                                otpAutofill.clearFocus()
                                if (isFocusEnabled)
                                    focusOnFirstDigit()
                            }
                            EVENT_INPUT_SHOWN -> {
                                // The autofill affordance associated with the view was shown.
                            }
                            EVENT_INPUT_UNAVAILABLE -> {
                                // Autofill isn't available.
                            }
                        }
                    }
                })
                afm?.requestAutofill(otpAutofill)
                otpAutofill.requestFocus()
            }
        }
    }
}
