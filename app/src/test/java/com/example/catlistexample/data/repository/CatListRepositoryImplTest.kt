package com.example.catlistexample.data.repository

import com.example.catlistexample.data.resource.RemoteCatListSource
import com.example.catlistexample.datastore.CatDataStore
import com.example.catlistexample.model.CatDataResponse
import com.example.catlistexample.model.CatDataResponseItem
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CatListRepositoryImplTest {
    private lateinit var remoteDataSource: RemoteCatListSource
    private lateinit var localDataStore: CatDataStore
    private lateinit var catRepository: CatListRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        localDataStore = mockk()
        catRepository = CatListRepositoryImpl(remoteDataSource, localDataStore)
    }

    @Test
    fun `getCats returns mapped list with favorites`() = runBlocking {
        val catDataResponseItem =
            CatDataResponseItem(emptyList(), 0, "1", "https://example.com/cat1.jpg", 0)
        val catDataResponse = CatDataResponse().apply {
            add(catDataResponseItem)
        }
        val response = retrofit2.Response.success(catDataResponse)
        val remoteFlow = flowOf(response)

        coEvery { remoteDataSource.fetchCatData() } returns remoteFlow
        coEvery { localDataStore.isCatSaved("1") } returns flowOf(true)

        val result = catRepository.getCats().first()

        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
        assertEquals(true, result[0].isFavorite)
    }

    @Test
    fun `toggleCatFavorite removes cat if already favorite`() = runBlocking {
        val catDataResponseItem = CatDataResponseItem(
            emptyList(),
            0,
            "1",
            "https://example.com/cat1.jpg",
            0,
            isFavorite = true
        )
        coEvery { localDataStore.isCatSaved("1") } returns flowOf(true)
        coEvery { localDataStore.removeSavedCat("1") } just Runs

        catRepository.toggleCatFavorite(catDataResponseItem)

        coVerify { localDataStore.removeSavedCat("1") }
    }

    @Test
    fun `toggleCatFavorite adds cat if not favorite`() = runBlocking {
        val catDataResponseItem = CatDataResponseItem(
            emptyList(),
            0,
            "1",
            "https://example.com/cat1.jpg",
            0,
            isFavorite = false
        )
        coEvery { localDataStore.isCatSaved("1") } returns flowOf(false)
        coEvery { localDataStore.setSavedCat("1", "https://example.com/cat1.jpg") } just Runs

        catRepository.toggleCatFavorite(catDataResponseItem)

        coVerify { localDataStore.setSavedCat("1", "https://example.com/cat1.jpg") }
    }

    @Test
    fun `getFavoriteCats returns saved cats`() = runBlocking {
        // Given
        val savedCats = mapOf("1" to "https://example.com/cat1.jpg")
        coEvery { localDataStore.getAllSavedCats() } returns flowOf(savedCats)

        val result = catRepository.getFavoriteCats().first()

        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
        assertEquals("https://example.com/cat1.jpg", result[0].url)
        assertEquals(true, result[0].isFavorite)
    }
}