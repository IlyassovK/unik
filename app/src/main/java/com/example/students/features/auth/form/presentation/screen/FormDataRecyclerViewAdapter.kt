package com.example.students.features.auth.form.presentation.screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.students.databinding.ItemFormDataBinding
import com.example.students.features.auth.form.presentation.model.FormData

class FormDataRecyclerViewAdapter(
    private val dataSet: List<FormData>,
    private val mClickListener: (data: FormData) -> Unit = { },
) :
    RecyclerView.Adapter<FormDataRecyclerViewAdapter.FormDataViewHolder>() {

    inner class FormDataViewHolder(val binding: ItemFormDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormDataViewHolder {
        val binding =
            ItemFormDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FormDataViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    var isSelected: Boolean = false

    override fun onBindViewHolder(holder: FormDataViewHolder, position: Int) {
        val data = dataSet[position]
        holder.binding.apply {
            text.text = data.title
            root.setOnClickListener {
                if (!isSelected) {
                    selectedImage.visibility = View.VISIBLE
                    isSelected = true
                } else {
                    selectedImage.visibility = View.GONE
                    isSelected = false
                }
                mClickListener.invoke(data)
            }
        }
    }
}