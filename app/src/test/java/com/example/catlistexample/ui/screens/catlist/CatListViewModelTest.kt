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
class CatListViewModelTest{
    private lateinit var catRepository: CatListRepositoryImpl
    private lateinit var viewModel: CatListViewModel

    // Define a TestDispatcher to control the coroutine execution
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // Set the main dispatcher to a test dispatcher for coroutine testing
        Dispatchers.setMain(testDispatcher)

        // Initialize the repository mock
        catRepository = mockk()

        // Stub the getCats method to prevent initialization issues
        coEvery { catRepository.getCats() } returns flowOf(emptyList())

        // Initialize the ViewModel with the mocked repository
        viewModel = CatListViewModel(catRepository, SavedStateHandle())
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher after tests
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCatListData should update state to Success when data is fetched`() = runTest {
        // Given
        val catDataResponseItem = CatDataResponseItem(
            breeds = emptyList(),
            height = 100,
            id = "1",
            url = "https://example.com/cat1.jpg",
            width = 100
        )
        val catList = listOf(catDataResponseItem)

        // Mock the repository to return a flow with the expected cat list
        coEvery { catRepository.getCats() } returns flowOf(catList)

        // When
        viewModel.fetchCatListData()

        // Then
        // Allow the coroutines to run
        advanceUntilIdle() // Make sure to use this to wait for all coroutines to complete

        // Check the state after coroutine execution
        val state = viewModel.catList.value
        assertTrue(state is CatListUIState.Success)
        assertEquals(catList, (state as CatListUIState.Success).cats)
    }

    @Test
    fun `fetchCatListData should update state to Error when exception is thrown`() = runTest {
        // Given
        // Mock the repository to throw an exception
        coEvery { catRepository.getCats() } returns flow { throw RuntimeException("Error fetching data") }

        // When
        viewModel.fetchCatListData()

        // Then
        // Allow the coroutines to run
        advanceUntilIdle() // Make sure to use this to wait for all coroutines to complete

        // Check the state after coroutine execution
        val state = viewModel.catList.value
        assertTrue(state is CatListUIState.Error)
    }

    @Test
    fun `onToggleCatData should toggle favorite status`() = runTest {
        // Given
        val catDataResponseItem = CatDataResponseItem(
            breeds = emptyList(),
            height = 100,
            id = "1",
            url = "https://example.com/cat1.jpg",
            width = 100,
            isFavorite = false
        )
        val initialList = listOf(catDataResponseItem)

        // Set the initial state to Success with the initial list
        viewModel = CatListViewModel(catRepository, SavedStateHandle())
        viewModel.mCatList.value = CatListUIState.Success(initialList)

        // Mock the repository to return Unit when toggling the favorite status
        coEvery { catRepository.toggleCatFavorite(any()) } just Runs

        // Mock the repository getCats call to avoid unstubbed call issues during ViewModel init
        coEvery { catRepository.getCats() } returns flowOf(emptyList())

        // When
        viewModel.onToggleCatData(catDataResponseItem)

        // Then
        // Allow the coroutines to run
        advanceUntilIdle() // Make sure to use this to wait for all coroutines to complete

        // Check the state after coroutine execution
        val state = viewModel.catList.value
        assertTrue(state is CatListUIState.Success)
        assertEquals(true, (state as CatListUIState.Success).cats[0].isFavorite)
    }
}