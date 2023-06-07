package com.example.students.features

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.example.students.R
import com.example.students.databinding.DialogLanguageBinding
import com.example.students.utils.LocaleManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LanguageDialog : BottomSheetDialogFragment() {

    interface LangDialogListener {
        fun onLanguageSelected(lang: LocaleManager.SupportedLanguages)
    }

    var listener: LangDialogListener? = null

    private lateinit var binding: DialogLanguageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.iconClose.setOnClickListener { dismiss() }

        initViews()
        initListeners()
    }

    private fun initViews() = with(binding) {
        dialogTitle.text = getString(R.string.dialog_settings_interface_lang)
        firstTitle.text = getString(R.string.dialog_settings_interface_lang_kz)
        firstIcon.setImageResource(R.drawable.ic_kzt)
        secondTitle.text = getString(R.string.dialog_settings_interface_lang_ru)
        secondIcon.setImageResource(R.drawable.ic_rub)
        thirdTitle.text = getString(R.string.dialog_settings_interface_lang_en)
        thirdIcon.setImageResource(R.drawable.ic_gbp)
        updateCheckBoxes()
    }

    private fun initListeners() = with(binding) {
        firstContainer.setOnClickListener {
            updateCheckBoxes()
            getClickCallback()?.onLanguageSelected(LocaleManager.SupportedLanguages.KAZAKH)
        }
        secondContainer.setOnClickListener {
            updateCheckBoxes()
            getClickCallback()?.onLanguageSelected(LocaleManager.SupportedLanguages.RUSSIAN)
        }
        thirdContainer.setOnClickListener {
            updateCheckBoxes()
            getClickCallback()?.onLanguageSelected(LocaleManager.SupportedLanguages.ENGLISH)
        }
    }


    private fun updateCheckBoxes() = with(binding) {
        val currentLocale = LocaleManager.currentLocale(requireContext()).language

        firstCheck.isVisible =
            currentLocale == LocaleManager.SupportedLanguages.KAZAKH.isoCode()
        secondCheck.isVisible =
            currentLocale == LocaleManager.SupportedLanguages.RUSSIAN.isoCode()
        thirdCheck.isVisible =
            currentLocale == LocaleManager.SupportedLanguages.ENGLISH.isoCode()


        val typeface = ResourcesCompat.getFont(requireContext(), R.font.default_font)
        firstTitle.setTypeface(typeface, checkTypeFace(firstCheck.isVisible))
        secondTitle.setTypeface(typeface, checkTypeFace(secondCheck.isVisible))
        thirdTitle.setTypeface(typeface, checkTypeFace(thirdCheck.isVisible))
    }

    private fun checkTypeFace(isSelected: Boolean) =
        if (isSelected) Typeface.BOLD else Typeface.NORMAL


    override fun onStart() {
        super.onStart()
        val bottomSheet: View? =
            dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
        bottomSheetBehavior.halfExpandedRatio = 0.0001f
        bottomSheetBehavior.peekHeight = 1
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun getClickCallback(): LangDialogListener? =
        (activity as? LangDialogListener).also { dismiss() }

}