package com.mercel.mealmate.presentation.discover.viewmodel

import app.cash.turbine.test
import com.mercel.mealmate.domain.model.Ingredient
import com.mercel.mealmate.domain.model.Nutrients
import com.mercel.mealmate.domain.model.Recipe
import com.mercel.mealmate.domain.usecase.SearchRecipesUseCase
import com.mercel.mealmate.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverViewModelTest {

    private lateinit var searchRecipesUseCase: SearchRecipesUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var viewModel: DiscoverViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        searchRecipesUseCase = mockk()
        toggleFavoriteUseCase = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state and search flow`() = runTest {
        // Given
        val mockRecipes = listOf(createMockRecipe("1", "Test Recipe"))
        coEvery { 
            searchRecipesUseCase(any(), any(), any()) 
        } returns flowOf(Result.success(mockRecipes))

        // When
        viewModel = DiscoverViewModel(searchRecipesUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("chicken", state.searchQuery) // ViewModel initializes with "chicken"
            assertEquals(1, state.recipes.size)
            assertEquals("Test Recipe", state.recipes[0].title)
            assertFalse(state.isLoading) // Loading should be false after search completes
            assertEquals(null, state.error)
        }
    }

    @Test
    fun `searchRecipes updates state with success`() = runTest {
        // Given
        val mockRecipes = listOf(
            createMockRecipe("1", "Pasta"),
            createMockRecipe("2", "Pizza")
        )
        coEvery { 
            searchRecipesUseCase(any(), any(), any()) 
        } returns flowOf(Result.success(mockRecipes))

        // When
        viewModel = DiscoverViewModel(searchRecipesUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(2, state.recipes.size)
            assertEquals("Pasta", state.recipes[0].title)
            assertEquals("Pizza", state.recipes[1].title)
            assertEquals(null, state.error)
        }
    }

    @Test
    fun `searchRecipes updates state with error`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { 
            searchRecipesUseCase(any(), any(), any()) 
        } returns flowOf(Result.failure(Exception(errorMessage)))

        // When
        viewModel = DiscoverViewModel(searchRecipesUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(emptyList<Recipe>(), state.recipes)
            assertEquals(errorMessage, state.error)
        }
    }

    @Test
    fun `onSearchQueryChange updates search query`() = runTest {
        // Given
        val mockRecipes = listOf(createMockRecipe("1", "Test"))
        coEvery { 
            searchRecipesUseCase(any(), any(), any()) 
        } returns flowOf(Result.success(mockRecipes))
        viewModel = DiscoverViewModel(searchRecipesUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onSearchQueryChange("pasta")

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("pasta", state.searchQuery)
        }
    }

    @Test
    fun `onDietChange updates diet and triggers search`() = runTest {
        // Given
        val mockRecipes = listOf(createMockRecipe("1", "Test"))
        coEvery { 
            searchRecipesUseCase(any(), any(), any()) 
        } returns flowOf(Result.success(mockRecipes))
        viewModel = DiscoverViewModel(searchRecipesUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onDietChange("vegan")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("vegan", state.selectedDiet)
        }
        coVerify(atLeast = 2) { searchRecipesUseCase(any(), any(), any()) }
    }

    @Test
    fun `toggleFavorite calls use case`() = runTest {
        // Given
        val mockRecipes = listOf(createMockRecipe("1", "Test"))
        coEvery { 
            searchRecipesUseCase(any(), any(), any()) 
        } returns flowOf(Result.success(mockRecipes))
        viewModel = DiscoverViewModel(searchRecipesUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.toggleFavorite("1")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { toggleFavoriteUseCase("1") }
    }

    private fun createMockRecipe(id: String, title: String): Recipe {
        return Recipe(
            id = id,
            title = title,
            imageUrl = "https://example.com/image.jpg",
            servings = 4,
            readyInMinutes = 30,
            cuisines = listOf("Italian"),
            diets = listOf("Vegetarian"),
            ingredients = listOf(
                Ingredient("Tomato", "2 cups", "Produce")
            ),
            steps = listOf("Step 1", "Step 2"),
            nutrients = Nutrients(
                calories = 300,
                protein = 10.0,
                carbs = 40.0,
                fat = 5.0
            ),
            isFavorite = false
        )
    }
}
