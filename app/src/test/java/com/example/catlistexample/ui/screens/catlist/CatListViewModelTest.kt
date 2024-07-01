package com.example.catlistexample.ui.screens.catlist

import com.example.catlistexample.data.repository.CatDataRepository
import com.example.catlistexample.data.repository.CatListRepositoryImpl
import com.example.catlistexample.model.CatDataResponseItem
import com.example.catlistexample.model.FavCatDataResponseItem
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CatListViewModelTest {
    private lateinit var catRepository: CatDataRepository
    private lateinit var viewModel: CatListViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        catRepository = mockk()

        // Mock combinedData() to return an empty list initially
        coEvery { catRepository.combinedData() } returns flowOf(emptyList())

        // Mock getFavoriteCats() to return an empty list initially
        coEvery { catRepository.getFavoriteCats() } returns flowOf(emptyList())

        viewModel = CatListViewModel(catRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCatListData should update state to Success when data is fetched`() = runTest {
        val catDataResponseItem = FavCatDataResponseItem(
            CatDataResponseItem(
                breeds = emptyList(),
                height = 100,
                id = "1",
                url = "https://example.com/cat1.jpg",
                width = 100
            ), false
        )
        val catList = listOf(catDataResponseItem)

        // Mock combinedData() to return a list of catList when called
        coEvery { catRepository.combinedData() } returns flowOf(catList)

        // Call fetchCatListData() method
        viewModel.fetchCatListData()

        advanceUntilIdle()

        // Assert that the state is updated to Success with the fetched data
        val state = viewModel.catList.value
        assertTrue(state is CatListUIState.Success)
        assertEquals(catList, (state as CatListUIState.Success).cats)
    }
    @Test
    fun `onToggleCatData should update favorite status in repository`() = runTest {
        val catDataResponseItem = CatDataResponseItem(
            breeds = emptyList(),
            height = 100,
            id = "1",
            url = "https://example.com/cat1.jpg",
            width = 100
        )

        // Mock the repository method to toggle favorite status
        coEvery { catRepository.toggleFavoriteCats("1", "https://example.com/cat1.jpg", true) } just Runs

        // Call onToggleCatData() method with a non-favorite cat
        viewModel.onToggleCatData(catDataResponseItem)

        // Verify that the repository method was called with the correct arguments
        coVerify {
            catRepository.toggleFavoriteCats("1", "https://example.com/cat1.jpg", true)
        }
    }
}