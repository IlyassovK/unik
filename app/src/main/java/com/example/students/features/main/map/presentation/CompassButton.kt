package com.example.students.features.main.map.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.example.students.databinding.CompasButtonLayoutBinding

class CompassButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CardView(context, attrs) {

    private lateinit var binding: CompasButtonLayoutBinding
    init {
        binding = CompasButtonLayoutBinding.inflate(
            LayoutInflater.from(context),
            this,
        )
    }

    fun rotateNeedle(degree: Float) {
        binding.compassNeedle.rotation = 360 - degree
    }

}