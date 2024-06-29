package com.example.catlistexample.ui.screens.catlist

import androidx.lifecycle.SavedStateHandle
import com.example.catlistexample.data.repository.CatListRepositoryImpl
import com.example.catlistexample.model.CatDataResponseItem
import io.mockk.Runs
import io.mockk.coEvery
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
    private lateinit var catRepository: CatListRepositoryImpl
    private lateinit var viewModel: CatListViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        catRepository = mockk()

        coEvery { catRepository.getCats() } returns flowOf(emptyList())

        viewModel = CatListViewModel(catRepository, SavedStateHandle())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCatListData should update state to Success when data is fetched`() = runTest {
        val catDataResponseItem = CatDataResponseItem(
            breeds = emptyList(),
            height = 100,
            id = "1",
            url = "https://example.com/cat1.jpg",
            width = 100
        )
        val catList = listOf(catDataResponseItem)

        coEvery { catRepository.getCats() } returns flowOf(catList)

        viewModel.fetchCatListData()


        advanceUntilIdle()

        val state = viewModel.catList.value
        assertTrue(state is CatListUIState.Success)
        assertEquals(catList, (state as CatListUIState.Success).cats)
    }

    @Test
    fun `fetchCatListData should update state to Error when exception is thrown`() = runTest {

        coEvery { catRepository.getCats() } returns flow { throw RuntimeException("Error fetching data") }


        viewModel.fetchCatListData()


        advanceUntilIdle()

        val state = viewModel.catList.value
        assertTrue(state is CatListUIState.Error)
    }

    @Test
    fun `onToggleCatData should toggle favorite status`() = runTest {
        val catDataResponseItem = CatDataResponseItem(
            breeds = emptyList(),
            height = 100,
            id = "1",
            url = "https://example.com/cat1.jpg",
            width = 100,
            isFavorite = false
        )
        val initialList = listOf(catDataResponseItem)

        viewModel = CatListViewModel(catRepository, SavedStateHandle())
        viewModel.mCatList.value = CatListUIState.Success(initialList)

        coEvery { catRepository.toggleCatFavorite(any()) } just Runs

        coEvery { catRepository.getCats() } returns flowOf(emptyList())

        viewModel.onToggleCatData(catDataResponseItem)


        advanceUntilIdle()

        val state = viewModel.catList.value
        assertTrue(state is CatListUIState.Success)
        assertEquals(true, (state as CatListUIState.Success).cats[0].isFavorite)
    }
}