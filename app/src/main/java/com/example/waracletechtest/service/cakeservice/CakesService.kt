package com.example.waracletechtest.service.cakeservice

import com.example.waracletechtest.data.Cake
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class CakesService {
    private val cakesApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(CakesApi::class.java)
    }

    suspend fun getCakesList(): List<Cake> {
        val cakesResponse = cakesApi.listCakes()
        if (!cakesResponse.isSuccessful) throw IOException(cakesResponse.errorBody().toString())
        else return cakesResponse.body()
            ?.distinct()
            ?.sortedBy { c -> c.title }
            ?.toList() ?: listOf()
    }
}