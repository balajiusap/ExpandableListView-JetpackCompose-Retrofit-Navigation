package com.example.expandablelist.data


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Author(

	@field:SerializedName("author")
	@Expose
	val author: String? = null,

	@field:SerializedName("width")
	@Expose
	val width: Int? = null,

	@field:SerializedName("download_url")
	@Expose
	val downloadUrl: String? = null,

	@field:SerializedName("id")
	@Expose
	val id: String? = null,

	@field:SerializedName("url")
	@Expose
	val url: String? = null,

	@field:SerializedName("height")
	@Expose
	val height: Int? = null
)
