package com.example.students.features.main.feed.data.model

import com.example.students.features.main.feed.presentation.model.Category
import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("data")
    val data: List<Data>,
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("deleted_at")
        val deletedAt: Any,
        @SerializedName("id")
        val id: Int,
        @SerializedName("status")
        val status: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("updated_at")
        val updatedAt: String,
    )
}

fun CategoryResponse.Data.toUiCategory(): Category {
    return Category(
        title = this.title,
        id = this.id
    )
}
