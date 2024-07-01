package com.example.catlistexample.ui.screens.favcat

import com.example.catlistexample.data.repository.CatDataRepository
import com.example.catlistexample.data.repository.CatListRepositoryImpl
import com.example.catlistexample.model.CatDataResponseItem
import com.example.catlistexample.model.FavCatDataResponseItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FavoriteCatsViewModelTest{
    private lateinit var catRepository: CatDataRepository
    private lateinit var viewModel: FavoriteCatsViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        catRepository = mockk()

        // Mock getFavoriteCats() to return an empty list initially
        coEvery { catRepository.getFavoriteCats() } returns flowOf(emptyList())

        viewModel = FavoriteCatsViewModel(catRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should call fetchFavoriteCatList`() = runTest {
        // Verify that getFavoriteCats() was called during initialization
        coVerify(exactly = 1) { catRepository.getFavoriteCats() }
    }

    @Test
    fun `fetchFavoriteCatList updates favoriteCatList state flow`() = runTest {
        val favoriteCats = listOf(
            FavCatDataResponseItem(
                CatDataResponseItem(
                    breeds = emptyList(),
                    height = 100,
                    id = "1",
                    url = "https://example.com/cat1.jpg",
                    width = 100
                ), true
            ),
            FavCatDataResponseItem(
                CatDataResponseItem(
                    breeds = emptyList(),
                    height = 100,
                    id = "2",
                    url = "https://example.com/cat2.jpg",
                    width = 100
                ), true
            )
        )

        // Mock getFavoriteCats() to return the list of favorite cats
        coEvery { catRepository.getFavoriteCats() } returns flowOf(favoriteCats)

        // Trigger the fetching of favorite cats
        viewModel.fetchFavoriteCatList()

        advanceUntilIdle()

        // Verify that the state flow is updated with the favorite cats
        val state = viewModel.favoriteCatList.value
        assertEquals(favoriteCats, state)
    }
}