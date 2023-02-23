package com.example.students.features.form.presentation.screen

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.students.R
import com.example.students.databinding.DialogFormBinding
import com.example.students.features.form.presentation.FormViewModel
import com.example.students.features.form.presentation.FormViewModel.FormBottomSheetType.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FormBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogFormBinding
    private val viewModel: FormViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initViews()
    }

    private fun initViews() {
        binding.apply {
            when (viewModel.selectedBottomSheet) {
                UNIVERSITY -> {
                    title.text = getString(R.string.form_universities)
                    recyclerView.adapter = FormDataRecyclerViewAdapter(viewModel.universities) {
                        viewModel.selectedUniversity = it
                        dismiss()
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
                }
                HOBBY -> {
                    title.text = getString(R.string.form_hobbies)
                    recyclerView.adapter = FormDataRecyclerViewAdapter(viewModel.hobbies) {
                        viewModel.selectedHobbies.add(it)
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
                }
                SPECIALITY -> {
                    title.text = getString(R.string.form_specialities)
                    recyclerView.adapter = FormDataRecyclerViewAdapter(viewModel.specialities) {
                        viewModel.selectedSpeciality = it
                        dismiss()
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
                }
                CITY -> {
                    title.text = getString(R.string.form_city)
                    recyclerView.adapter = FormDataRecyclerViewAdapter(viewModel.cities) {
                        viewModel.selectedCity = it
                        dismiss()
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.onFormDialogClose()
    }


    companion object {
        const val ON_CLOSE_LISTENER = "on_close_listener"
    }
}