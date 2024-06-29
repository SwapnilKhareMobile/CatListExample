package com.example.catlistexample.ui.screens.catlist

import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.catlistexample.model.CatDataResponseItem
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CatListScreenKtTest {
    // Initialize the Compose test rule
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCatListScreen() {
        val mockViewModel = mockk<CatListViewModel>()

        val mockCats = listOf(
            CatDataResponseItem(emptyList(), 100, "cat1", "url1", 100, false),
            CatDataResponseItem(emptyList(), 100, "cat2", "url2", 100, false)
        )
        val mockStateFlow = MutableStateFlow<CatListUIState>(CatListUIState.Success(mockCats))
        every { mockViewModel.catList } returns mockStateFlow

        composeTestRule.setContent {
            val viewModel = remember { mockViewModel }
            CatListScreen(
                onCatItemClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText("cat1").assertExists()
        composeTestRule.onNodeWithText("cat2").assertExists()

    }
}