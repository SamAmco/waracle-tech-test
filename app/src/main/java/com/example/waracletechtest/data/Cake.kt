package com.example.waracletechtest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cake(
    @Expose @SerializedName("title") val title: String,
    @Expose @SerializedName("desc") val desc: String,
    @Expose @SerializedName("image") val imageUrl: String
) {
    override fun equals(other: Any?) = other is Cake && other.title == title
    override fun hashCode() = title.hashCode()
    override fun toString() = "$title - $desc"
}