package com.example.students.features.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.students.databinding.DialogCreatePointBinding
import com.example.students.features.main.map.data.model.CreatePointRequest
import com.example.students.features.main.map.presentation.MapViewModel
import com.example.students.features.main.map.presentation.model.CreatePointState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CreatePointBottomDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogCreatePointBinding

    private val viewModel: MapViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogCreatePointBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupObservers()
    }

    private fun initViews() {
        binding.apply {
            closeBtn.setOnClickListener {
                dismiss()
            }

            if (viewModel.createLatitude != 0.0 && viewModel.createLongitude != 0.0) {
                latEditText.setText(viewModel.createLatitude.toString())
                longEditText.setText(viewModel.createLongitude.toString())

                latEditText.isEnabled = false
                longEditText.isEnabled = false
            }

            createBtn.setOnClickListener {
                if (isFieldsValid()) {
                    viewModel.createMapPoint(
                        request = CreatePointRequest(
                            latitude = binding.latEditText.text.toString().toDouble(),
                            longitude = binding.longEditText.text.toString().toDouble(),
                            name = binding.titleEditText.text.toString(),
                            placeDescription = binding.bodyEditText.text.toString(),
                        )
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Fill the fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.createState.collect { state ->
                when (state) {
                    CreatePointState.Error -> {
                        binding.createBtn.showNormal()
                        Toast.makeText(
                            requireContext(),
                            "Create post error",
                            Toast.LENGTH_SHORT
                        ).show()
                        dismiss()
                    }
                    CreatePointState.Loading -> {
                        binding.createBtn.showLoading()
                    }
                    CreatePointState.PointCreated -> {
                        binding.createBtn.showNormal()
                        Toast.makeText(
                            requireContext(),
                            "Post created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        dismiss()
                    }
                }
            }
        }
    }

    private fun isFieldsValid(): Boolean {
        return !binding.titleEditText.text.isNullOrEmpty() && !binding.latEditText.text.isNullOrEmpty() && !binding.longEditText.text.isNullOrEmpty() && !binding.bodyEditText.text.isNullOrEmpty()
    }

    companion object {
        const val CREATE_POINT_LAT = "create_point_latitude"
        const val CREATE_POINT_LONG = "create_point_longitude"
    }
}