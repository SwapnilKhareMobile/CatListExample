package com.example.catlistexample.data.repository

import com.example.catlistexample.data.resource.LocalCatDataSourceImpl
import com.example.catlistexample.data.resource.RemoteCatDataSourceImpl
import com.example.catlistexample.data.resource.RemoteDataSource
import com.example.catlistexample.datastore.CatDataStore
import com.example.catlistexample.model.CatDataResponse
import com.example.catlistexample.model.CatDataResponseItem
import com.example.catlistexample.model.FavCatDataResponseItem
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
@ExperimentalCoroutinesApi
class CatListRepositoryImplTest {

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataResource: LocalCatDataSourceImpl
    private lateinit var localDataStore: CatDataStore
    private lateinit var catRepository: CatDataRepository

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        localDataResource = mockk()
        localDataStore = mockk()
        catRepository = CatListRepositoryImpl(remoteDataSource, localDataResource)
    }

    @After
    fun tearDown() {
        // Clean up resources if needed
    }

    @Test
    fun `toggleCatFavorite adds cat if not favorite`() = runBlocking {
        // Given a non-favorite cat
        val catDataResponseItem = FavCatDataResponseItem(
            CatDataResponseItem(
                emptyList(),
                0,
                "1",
                "https://example.com/cat1.jpg",
                0,
            ),
            isFavorite = false
        )

        // Mock behavior to simulate the cat is not saved initially
        coEvery { localDataStore.isCatSaved("1") } returns flowOf(false)

        // Mock the method to save the cat
        coEvery {
            localDataResource.toggleFavoriteCats("1", "https://example.com/cat1.jpg", true)
        } just Runs

        // When the repository toggles the favorite status to true
        catRepository.toggleFavoriteCats(
            catDataResponseItem.catItem.id,
            catDataResponseItem.catItem.url,
            true
        )

        // Then verify the cat was added as a favorite
        coVerify {
            localDataResource.toggleFavoriteCats("1", "https://example.com/cat1.jpg", true)
        }
    }

    @Test
    fun `toggleCatFavorite removes cat if already favorite`() = runBlocking {
        // Given a favorite cat
        val catDataResponseItem = FavCatDataResponseItem(
            CatDataResponseItem(
                emptyList(),
                0,
                "1",
                "https://example.com/cat1.jpg",
                0,
            ),
            isFavorite = true
        )

        // Mock behavior to simulate the cat is already saved
        coEvery { localDataStore.isCatSaved("1") } returns flowOf(true)

        // Mock the method to remove the cat from favorites
        coEvery {
            localDataResource.toggleFavoriteCats("1", "https://example.com/cat1.jpg", false)
        } just Runs

        // When the repository toggles the favorite status to false
        catRepository.toggleFavoriteCats(
            catDataResponseItem.catItem.id,
            catDataResponseItem.catItem.url,
            false
        )

        // Then verify the cat was removed from favorites
        coVerify {
            localDataResource.toggleFavoriteCats("1", "https://example.com/cat1.jpg", false)
        }
    }

    @Test
    fun `combinedData returns cat list with correct favorite status`() = runTest {
        // Given a list of cats from remote source
        val remoteCats = listOf(
            CatDataResponseItem(emptyList(), 100, "1", "https://example.com/cat1.jpg", 100),
            CatDataResponseItem(emptyList(), 100, "2", "https://example.com/cat2.jpg", 100)
        )

        // And a list of favorite cat IDs from local source
        val favoriteCatIds = setOf("1")

        // Mock the remote and local data sources
        coEvery { remoteDataSource.fetchCatData() } returns flowOf(remoteCats)
        coEvery { localDataResource.favoriteStream } returns flowOf(favoriteCatIds.toList())

        // When the repository combines the data
        val combinedData = catRepository.combinedData().first()

        // Then verify the combined data includes the correct favorite status
        assertEquals(2, combinedData.size)
        assertEquals(true, combinedData[0].isFavorite) // Cat with ID "1" should be favorite
        assertEquals(false, combinedData[1].isFavorite) // Cat with ID "2" should not be favorite
    }
    }