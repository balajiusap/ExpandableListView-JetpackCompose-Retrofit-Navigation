package com.example.expandablelist

import com.example.expandablelist.data.Author
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("v2/list")
    suspend fun getAuthorList() : List<Author>

    companion object {
        private val baseUrl = "https://picsum.photos/"
        private val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        val apiService : ApiService by lazy { retrofit.create(ApiService::class.java) }
    }
}