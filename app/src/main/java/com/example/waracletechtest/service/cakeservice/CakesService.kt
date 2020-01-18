package com.example.waracletechtest.service.cakeservice

import com.example.waracletechtest.data.Cake
import java.io.IOException

//TODO ideally we would use dependency injection here (e.g. Dagger) instead of these top level factory functions
fun createCakesService(): CakesService {
    return CakesService(createCakesApi())
}

class CakesService(private val cakesApi: CakesApi) {
    suspend fun getCakesList(): List<Cake> {
        val cakesResponse = cakesApi.listCakes()
        if (!cakesResponse.isSuccessful) throw IOException(cakesResponse.errorBody().toString())
        else return cakesResponse.body()
            ?.distinct()
            ?.sortedBy { c -> c.title }
            ?.toList() ?: listOf()
    }
}