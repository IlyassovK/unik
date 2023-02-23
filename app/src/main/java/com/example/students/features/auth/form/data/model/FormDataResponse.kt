package com.example.students.features.auth.form.data.model

import com.example.students.features.auth.form.presentation.model.FormData
import com.google.gson.annotations.SerializedName

data class FormDataResponse(
    @SerializedName("data")
    val data: List<Data>,
)

data class Data(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updatedAt: String,
)

object Converter {
    fun convert2Ui(web: FormDataResponse): List<FormData> {
        return web.data.map {
            FormData(id = it.id, title = it.title)
        }
    }
}