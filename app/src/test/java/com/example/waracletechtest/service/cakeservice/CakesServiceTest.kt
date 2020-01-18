package com.example.waracletechtest.service.cakeservice

import com.example.waracletechtest.data.Cake
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import kotlin.test.*

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.io.IOException

class CakesServiceTest {
    @Mock
    private lateinit var cakesApi: CakesApi

    private lateinit var cakesService: CakesService

    private val testCakes = listOf(
        Cake("1", "2", "3"),
        Cake("1", "4", "5"),
        Cake("0", "6", "7"),
        Cake("2", "6", "7")
    )

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        cakesService = CakesService(cakesApi)
    }

    @Test
    fun getCakesListSuccess() = runBlocking {
        `when`(cakesApi.listCakes()).thenAnswer { Response.success(testCakes) }
        val cakes = cakesService.getCakesList()
        val expectedCakes =listOf(
            Cake("0", "6", "7"),
            Cake("1", "2", "3"),
            Cake("2", "6", "7")
        )
        assertEquals("Duplicates should be removed and cakes should be in order by title", expectedCakes, cakes)
        verify(cakesApi, times(1)).listCakes()
        return@runBlocking
    }

    @Test
    fun getCakesListFail() = runBlocking {
        val errorBody = "some error"
        `when`(cakesApi.listCakes()).thenAnswer { Response.error<Cake>(404,
            ResponseBody.create(MediaType.get("application/json;charset=utf-8"), errorBody))
        }
        assertFailsWith<IOException>(errorBody) { cakesService.getCakesList() }
        verify(cakesApi, times(1)).listCakes()
        return@runBlocking
    }
}