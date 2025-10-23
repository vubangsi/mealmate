# MealMate - Smart Meal Planning & Recipe Discovery App

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-brightgreen.svg)](https://developer.android.com/jetpack/compose)

## 📱 Introduction

**MealMate** is a comprehensive Android application designed to revolutionize meal planning and recipe discovery. Built with modern Android development practices, MealMate helps users discover recipes, plan weekly meals, generate shopping lists, and leverage AI-powered features for personalized meal recommendations.

### Key Features
- 🔍 **Recipe Discovery**: Search and filter recipes by cuisine, diet, and intolerances
- 📅 **Weekly Meal Planning**: Organize meals for the entire week with drag-and-drop functionality
- 🛒 **Smart Shopping Lists**: Auto-generate shopping lists from meal plans with AI optimization
- 🤖 **AI-Powered Features**: Generate meal plans, recipe summaries, and ingredient substitutions using OpenAI
- 📸 **Instant Meal Maker**: Scan your fridge/pantry to get instant meal suggestions
- 💾 **Offline Support**: Full offline functionality with Room database caching
- 🎨 **Modern UI**: Beautiful Material 3 design with dark theme support

---

## 📸 App Screenshots

### Login & Authentication
<img src="images/login screen.png" alt="Login Screen - User authentication with email and password" width="60%">

*Login screen with secure authentication and modern Material 3 design*

### Sign Up
<img src="images/signup screen.png" alt="Sign Up Screen - New user registration" width="60%">

*User registration screen for creating new accounts*

### Home Screen
<img src="images/home screen.png" alt="Home Screen - Main dashboard with featured recipes and quick actions" width="60%">

*Main dashboard displaying featured recipes, quick access to discover recipes, and instant meal maker*

### Recipe Search & Discovery
<img src="images/recipe search screen.png" alt="Recipe Search Screen - Browse and filter recipes by various criteria" width="60%">

*Recipe discovery screen with search functionality, diet filters, and intolerance options*

### Recipe Details
<img src="images/recipe detail.png" alt="Recipe Detail Screen - Comprehensive recipe information with ingredients and instructions" width="60%">

*Detailed recipe view showing ingredients, cooking instructions, nutritional information, and AI-generated summary*

### Weekly Meal Plan
<img src="images/weekly meal plan.png" alt="Weekly Meal Plan - Organize meals for the entire week" width="60%">

*Weekly meal planner with organized breakfast, lunch, and dinner slots for each day*

### Shopping List
<img src="images/AI generated shoping list.png" alt="Shopping List - AI-optimized shopping list with checkboxes" width="60%">

*Smart shopping list with AI optimization, organized by store sections, and check-off functionality*

### AI-Generated Recipe Summary
<img src="images/AI-generated recipe summary.png" alt="AI Recipe Summary - OpenAI-powered recipe description" width="60%">

*AI-generated recipe summary providing engaging descriptions and key highlights*

### Profile Screen
<img src="images/profile screen.png" alt="Profile Screen - User profile with dietary preferences" width="60%">

*User profile displaying personal information and dietary preferences*

### Settings
<img src="images/settings screen.png" alt="Settings Screen - App configuration and preferences" width="60%">

*Settings screen for managing dietary restrictions, calorie goals, theme preferences, and notifications*

---

## 🎯 Feature Implementation Documentation

This section maps each grading criterion to its specific implementation in the codebase.

### 1. Navigation & Single Activity Architecture (10 points)

**Implementation**: All screens are managed under a single `MainActivity` using Navigation Component 3 with a navigation graph and proper back stack management.

**Screen**: All screens

**Code Reference**: 
- [MainActivity.kt#L27-L37](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/MainActivity.kt#L27-L37) - Single Activity setup with Navigation
- [NavGraph.kt#L34-L122](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/presentation/navigation/NavGraph.kt#L34-L122) - Navigation graph definition

**Code Snippet**:
```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MealMateTheme {
                MealMateApp()
            }
        }
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Auth.route) { AuthScreen(...) }
        composable(Screen.Home.route) { HomeScreen(...) }
        composable(Screen.Discover.route) { DiscoverScreen(...) }
        // ... other screens with proper back stack management
    }
}
```

---

### 2. Room Database (Local Persistence) (10 points)

**Implementation**: Complete Room database implementation with entities, DAOs, and CRUD operations for recipes, meal plans, shopping items, and users.

**Screen**: All screens (data persistence layer)

**Code Reference**:
- [MealMateDatabase.kt#L18-L33](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/data/local/MealMateDatabase.kt#L18-L33) - Database definition
- [RecipeDao.kt](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/data/local/dao/RecipeDao.kt) - Recipe CRUD operations
- [MealPlanDao.kt](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/data/local/dao/MealPlanDao.kt) - Meal plan operations
- [ShoppingDao.kt](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/data/local/dao/ShoppingDao.kt) - Shopping list operations

**Code Snippet**:
```kotlin
@Database(
    entities = [
        RecipeEntity::class,
        MealEntryEntity::class,
        ShoppingItemEntity::class,
        UserEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MealMateDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun shoppingDao(): ShoppingDao
    abstract fun userDao(): UserDao
}
```

---

### 3. DataStore (User Preferences) (5 points)

**Implementation**: DataStore Preferences implementation for saving and retrieving user preferences including diet, intolerances, calorie goals, budget, and theme.

**Screen**: Settings Screen, Profile Screen

**Code Reference**:
- [PreferencesRepositoryImpl.kt#L14-L87](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/data/repository/PreferencesRepositoryImpl.kt#L14-L87) - DataStore implementation
- [DataStoreModule.kt](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/di/DataStoreModule.kt) - DataStore dependency injection

**Code Snippet**:
```kotlin
class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository {

    private object PreferencesKeys {
        val DIET = stringPreferencesKey("diet")
        val INTOLERANCES = stringSetPreferencesKey("intolerances")
        val CALORIES_PER_DAY = intPreferencesKey("calories_per_day")
        val BUDGET_PER_WEEK = intPreferencesKey("budget_per_week")
        val THEME = stringPreferencesKey("theme")
    }

    override val userPrefs: Flow<UserPrefs> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserPrefs(
                diet = preferences[PreferencesKeys.DIET],
                intolerances = preferences[PreferencesKeys.INTOLERANCES] ?: emptySet(),
                caloriesPerDay = preferences[PreferencesKeys.CALORIES_PER_DAY],
                budgetPerWeek = preferences[PreferencesKeys.BUDGET_PER_WEEK],
                theme = AppTheme.valueOf(
                    preferences[PreferencesKeys.THEME] ?: AppTheme.SYSTEM.name
                )
            )
        }
}
```

---

### 4. Retrofit (Remote Data Source) (10 points)

**Implementation**: Retrofit integration with Spoonacular API for recipe search and details, including coroutine support and proper error handling.

**Screen**: Discover Screen, Recipe Detail Screen

**Code Reference**:
- [SpoonacularApi.kt#L9-L27](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/data/remote/api/SpoonacularApi.kt#L9-L27) - API interface
- [NetworkModule.kt](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/di/NetworkModule.kt) - Retrofit configuration
- [RecipeRepositoryImpl.kt](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/data/repository/RecipeRepositoryImpl.kt) - Repository implementation

**Code Snippet**:
```kotlin
interface SpoonacularApi {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("diet") diet: String? = null,
        @Query("intolerances") intolerances: String? = null,
        @Query("addRecipeInformation") addRecipeInformation: Boolean = true,
        @Query("addRecipeNutrition") addRecipeNutrition: Boolean = true,
        @Query("number") number: Int = 20,
        @Query("apiKey") apiKey: String
    ): RecipeSearchResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") id: Int,
        @Query("includeNutrition") includeNutrition: Boolean = true,
        @Query("apiKey") apiKey: String
    ): RecipeDto
}
```

---

### 5. WorkManager (Background Task Scheduling) (5 points)

**Implementation**: WorkManager implementation for weekly meal plan reminders and background data synchronization.

**Screen**: Background tasks (triggered automatically)

**Code Reference**:
- [WeeklyReminderWorker.kt#L19-L73](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/worker/WeeklyReminderWorker.kt#L19-L73) - Weekly reminder worker
- [SyncWorker.kt](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/worker/SyncWorker.kt) - Background sync worker

**Code Snippet**:
```kotlin
@HiltWorker
class WeeklyReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            createNotificationChannel()
            sendNotification()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Plan Your Week")
            .setContentText("Don't forget to plan your meals for the upcoming week!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        // ... notification logic
    }
}
```

---

### 6. Clean Architecture Implementation (10 points)

**Implementation**: Complete Clean Architecture with data, domain, and presentation layers. Each feature is sub-packaged into screen, uistate, and viewmodel.

**Screen**: All screens

**Code Reference**:
- [Domain Layer](https://github.com/[owner]/[repo]/tree/main/app/src/main/java/com/mercel/mealmate/domain) - Use cases and models
- [Data Layer](https://github.com/[owner]/[repo]/tree/main/app/src/main/java/com/mercel/mealmate/data) - Repositories and data sources
- [Presentation Layer](https://github.com/[owner]/[repo]/tree/main/app/src/main/java/com/mercel/mealmate/presentation) - ViewModels and UI

**Package Structure**:
```
com.mercel.mealmate/
├── core/                          # Core utilities
│   └── util/
├── data/                          # Data layer
│   ├── local/                     # Room database
│   │   ├── dao/
│   │   ├── entity/
│   │   └── converter/
│   ├── remote/                    # API services
│   │   ├── api/
│   │   └── dto/
│   ├── mapper/                    # Data mappers
│   └── repository/                # Repository implementations
├── domain/                        # Domain layer
│   ├── model/                     # Domain models
│   ├── repository/                # Repository interfaces
│   └── usecase/                   # Use cases
├── presentation/                  # Presentation layer
│   ├── auth/
│   │   ├── screen/
│   │   ├── uistate/
│   │   └── viewmodel/
│   ├── discover/
│   │   ├── screen/
│   │   ├── uistate/
│   │   └── viewmodel/
│   ├── detail/
│   ├── home/
│   ├── plan/
│   ├── shopping/
│   ├── profile/
│   ├── settings/
│   ├── instantmeal/
│   └── navigation/
├── di/                            # Dependency injection
└── worker/                        # Background workers
```

---

### 7. Package Hierarchy (Mandatory) (5 points)

**Implementation**: Clearly structured folders reflecting Clean Architecture layers as shown above in section 6.

**Code Reference**: See package structure in section 6 above.

---

### 8. Testing (Mandatory - ViewModel Only) (10 points)

**Implementation**: Comprehensive ViewModel unit tests using JUnit, MockK, and Turbine for testing state flows.

**Screen**: Discover Screen (ViewModel testing)

**Code Reference**:
- [DiscoverViewModelTest.kt#L27-L199](https://github.com/[owner]/[repo]/blob/main/app/src/test/java/com/mercel/mealmate/presentation/discover/viewmodel/DiscoverViewModelTest.kt#L27-L199) - Complete ViewModel test suite

**Code Snippet**:
```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverViewModelTest {

    private lateinit var searchRecipesUseCase: SearchRecipesUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var viewModel: DiscoverViewModel
    private val testDispatcher = StandardTestDispatcher()

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
}
```

---

### 9. Generative AI Integration (Mandatory) (10 points)

**Implementation**: OpenAI GPT-3.5-turbo integration for multiple AI features including meal plan generation, recipe summaries, shopping list optimization, and instant meal suggestions.

**Screen**: Weekly Meal Plan, Recipe Detail, Shopping List, Instant Meal Maker

**Code Reference**:
- [AiRepositoryImpl.kt#L12-L361](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/data/repository/AiRepositoryImpl.kt#L12-L361) - AI implementation
- [OpenAiApi.kt](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/data/remote/api/OpenAiApi.kt) - OpenAI API interface

#### AI Feature 1: Weekly Meal Plan Generation

**Prompt**:
```
Generate a weekly meal plan with the following preferences:
- diet: vegetarian
- calories: 2000
- intolerances: dairy

Provide a balanced meal plan for breakfast, lunch, and dinner for 7 days.
Format the response as a clear weekly schedule.
```

**Code Snippet**:
```kotlin
override suspend fun generateWeeklyPlan(preferences: Map<String, Any>): Result<String> {
    return try {
        val prompt = buildString {
            append("Generate a weekly meal plan with the following preferences:\n")
            preferences.forEach { (key, value) ->
                append("- $key: $value\n")
            }
            append("\nProvide a balanced meal plan for breakfast, lunch, and dinner for 7 days. ")
            append("Format the response as a clear weekly schedule.")
        }

        val response = withTimeoutOrNull(TIMEOUT_MS) {
            openAiApi.generateCompletion(
                authHeader = "Bearer $apiKey",
                request = OpenAiRequest(
                    model = "gpt-3.5-turbo",
                    messages = listOf(
                        ChatMessage(role = "system", content = "You are a helpful meal planning assistant."),
                        ChatMessage(role = "user", content = prompt)
                    ),
                    temperature = 0.7,
                    maxTokens = 2048
                )
            )
        }
        
        Result.success(response?.choices?.firstOrNull()?.message?.content ?: "No plan generated")
    } catch (e: Exception) {
        Result.failure(Exception("Failed to generate meal plan: ${e.message}"))
    }
}
```

**Screenshot**: See "Weekly Meal Plan" screenshot above

---

#### AI Feature 2: Recipe Summary Generation

**Prompt**:
```
Create a brief, engaging summary for this recipe:
Title: Chicken Parmesan
Ingredients: chicken breast, marinara sauce, mozzarella cheese, parmesan cheese, breadcrumbs

Provide a 2-3 sentence description highlighting key flavors and appeal.
```

**Code Snippet**:
```kotlin
override suspend fun generateRecipeSummary(
    recipeTitle: String,
    ingredients: List<String>
): Result<String> {
    return try {
        val prompt = """
            Create a brief, engaging summary for this recipe:
            Title: $recipeTitle
            Ingredients: ${ingredients.joinToString(", ")}

            Provide a 2-3 sentence description highlighting key flavors and appeal.
        """.trimIndent()

        val response = withTimeoutOrNull(TIMEOUT_MS) {
            openAiApi.generateCompletion(
                authHeader = "Bearer $apiKey",
                request = OpenAiRequest(
                    model = "gpt-3.5-turbo",
                    messages = listOf(
                        ChatMessage(role = "system", content = "You are a helpful culinary assistant."),
                        ChatMessage(role = "user", content = prompt)
                    ),
                    temperature = 0.7,
                    maxTokens = 500
                )
            )
        }
        
        Result.success(response?.choices?.firstOrNull()?.message?.content ?: "No summary generated")
    } catch (e: Exception) {
        Result.failure(Exception("Failed to generate summary: ${e.message}"))
    }
}
```

**Screenshot**: See "AI-Generated Recipe Summary" screenshot above

---

#### AI Feature 3: Shopping List Optimization

**Prompt**:
```
Optimize this shopping list by grouping items by store section:
- Tomatoes
- Chicken breast
- Milk
- Bread
- Apples

Group items into: Produce, Dairy, Meat, Pantry, Frozen, Bakery, Other
```

**Code Snippet**:
```kotlin
override suspend fun optimizeShoppingList(items: List<String>): Result<String> {
    return try {
        val prompt = """
            Optimize this shopping list by grouping items by store section:
            ${items.joinToString("\n")}

            Group items into: Produce, Dairy, Meat, Pantry, Frozen, Bakery, Other
        """.trimIndent()

        val response = withTimeoutOrNull(TIMEOUT_MS) {
            openAiApi.generateCompletion(
                authHeader = "Bearer $apiKey",
                request = OpenAiRequest(
                    model = "gpt-3.5-turbo",
                    messages = listOf(
                        ChatMessage(role = "system", content = "You are a helpful shopping assistant."),
                        ChatMessage(role = "user", content = prompt)
                    ),
                    temperature = 0.7,
                    maxTokens = 1000
                )
            )
        }
        
        Result.success(response?.choices?.firstOrNull()?.message?.content ?: "No optimization generated")
    } catch (e: Exception) {
        Result.failure(Exception("Failed to optimize shopping list: ${e.message}"))
    }
}
```

**Screenshot**: See "AI Generated Shopping List" screenshot above

---

#### AI Feature 4: Instant Meal Plan from Available Ingredients

**Prompt**:
```
Create an instant meal plan for today (breakfast, lunch, dinner) using ONLY these available ingredients:

AVAILABLE INGREDIENTS:
- Eggs
- Bread
- Tomatoes
- Chicken breast
- Rice

REQUIREMENTS:
- Use only ingredients from the list above
- Create balanced, nutritious meals
- Provide quick, simple recipes (15-30 minutes max)
- Include cooking instructions and estimated prep/cook times
```

**Code Snippet**:
```kotlin
override suspend fun generateInstantMealPlan(availableIngredients: List<String>): Result<String> {
    return try {
        val prompt = """
        Create an instant meal plan for today (breakfast, lunch, dinner) using ONLY these available ingredients:
        
        AVAILABLE INGREDIENTS:
        ${availableIngredients.joinToString("\n")}
        
        REQUIREMENTS:
        - Use only ingredients from the list above
        - Create balanced, nutritious meals
        - Provide quick, simple recipes (15-30 minutes max)
        - Suggest substitutions if certain ingredients are missing
        - Include cooking instructions and estimated prep/cook times
        
        FORMAT:
        BREAKFAST:
        - Recipe Name
        - Ingredients used: [list]
        - Instructions: [brief steps]
        - Prep time: X minutes
        
        LUNCH:
        [same format]
        
        DINNER:
        [same format]
        
        SHOPPING NOTES:
        [Any critical missing ingredients needed for better meals]
        """

        val response = withTimeoutOrNull(TIMEOUT_MS) {
            openAiApi.generateCompletion(
                authHeader = "Bearer $apiKey",
                request = OpenAiRequest(
                    model = "gpt-3.5-turbo",
                    messages = listOf(
                        ChatMessage(role = "system", content = "You are an expert chef and meal planning assistant."),
                        ChatMessage(role = "user", content = prompt)
                    ),
                    temperature = 0.7,
                    maxTokens = 2048
                )
            )
        }
        
        Result.success(response?.choices?.firstOrNull()?.message?.content ?: "Unable to generate meal plan")
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

---

### 10. Project Documentation (README) (10 points)

**Implementation**: This comprehensive README file with all required sections.

---

### 11. User Interface & UX Design (5 points)

**Implementation**: Modern UI built with Jetpack Compose and Material 3, featuring dynamic color theming, dark mode support, and responsive layouts.

**Screen**: All screens

**Code Reference**:
- [Theme.kt](https://github.com/[owner]/[repo]/blob/main/app/src/main/java/com/mercel/mealmate/ui/theme/Theme.kt) - Material 3 theme
- All screen implementations use Compose with Material 3 components

---

### 12. Functionality & Stability (5 points)

**Implementation**: App runs without crashes, handles configuration changes, and provides proper error handling throughout.

**Features**:
- Proper error states with retry actions
- Loading states for async operations
- Configuration change handling via ViewModels
- Offline support with Room caching

---

### 13. Wholeness / SCI Connection (5 points)

**Reflection**: 

MealMate embodies the Science of Creative Intelligence principle of **"Harmony exists in the diversity of Natural Law"**. Just as nature maintains perfect balance through diverse ecosystems, MealMate harmonizes various aspects of healthy living:

- **Nutritional Balance**: The app helps users maintain dietary harmony by considering multiple factors - calories, nutrients, dietary restrictions, and personal preferences.
- **Time Management**: By planning meals in advance, users create harmony between their busy schedules and healthy eating habits.
- **Resource Optimization**: The AI-powered shopping list optimization reflects nature's efficiency, minimizing waste and maximizing utility.
- **Wholeness in Technology**: The integration of multiple technologies (AI, local storage, cloud APIs) works in harmony to create a seamless user experience.

This principle reminds us that true wellness comes from balancing all aspects of life - nutrition, planning, and mindful consumption - just as MealMate integrates diverse features into a unified, harmonious whole.

---

## 🏗️ Technical Details

### System Architecture

MealMate follows **Clean Architecture** principles with clear separation of concerns across three main layers:

#### Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     Presentation Layer                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Screens    │  │   ViewModels │  │   UI States  │      │
│  │  (Compose)   │◄─┤   (Logic)    │◄─┤   (State)    │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  Use Cases   │  │  Repositories│  │    Models    │      │
│  │  (Business)  │─►│  (Interfaces)│  │   (Entities) │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                       Data Layer                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ Repositories │  │  Local (Room)│  │ Remote (API) │      │
│  │     Impl     │─►│   Database   │  │   Retrofit   │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
```

#### Data Flow

1. **User Interaction** → Screen (Compose UI)
2. **Screen** → ViewModel (Event)
3. **ViewModel** → Use Case (Business Logic)
4. **Use Case** → Repository (Data Request)
5. **Repository** → Local DB / Remote API
6. **Data** → Repository → Use Case → ViewModel
7. **ViewModel** → UI State → Screen (Update UI)

---

### Technology Stack

| Category | Technology | Purpose |
|----------|-----------|---------|
| **Language** | Kotlin | Primary programming language |
| **UI Framework** | Jetpack Compose | Modern declarative UI |
| **Design System** | Material 3 | UI components and theming |
| **Architecture** | Clean Architecture | Separation of concerns |
| **Dependency Injection** | Hilt | Dependency management |
| **Local Database** | Room | Offline data persistence |
| **Preferences** | DataStore | User preferences storage |
| **Networking** | Retrofit + Moshi | API communication |
| **Image Loading** | Coil | Async image loading |
| **Background Tasks** | WorkManager | Scheduled tasks |
| **Navigation** | Navigation Compose | Screen navigation |
| **Concurrency** | Coroutines + Flow | Async operations |
| **AI Integration** | OpenAI API | Generative AI features |
| **Recipe API** | Spoonacular API | Recipe data source |
| **Testing** | JUnit + MockK + Turbine | Unit testing |

---

### API Integration

#### Spoonacular API
- **Base URL**: `https://api.spoonacular.com/`
- **Endpoints**:
  - `GET /recipes/complexSearch` - Search recipes with filters
  - `GET /recipes/{id}/information` - Get recipe details

#### OpenAI API
- **Base URL**: `https://api.openai.com/v1/`
- **Endpoints**:
  - `POST /chat/completions` - Generate AI responses
- **Model**: GPT-3.5-turbo

---

### Domain Models

#### Recipe
```kotlin
data class Recipe(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val servings: Int,
    val readyInMinutes: Int,
    val cuisines: List<String>,
    val diets: List<String>,
    val ingredients: List<Ingredient>,
    val steps: List<String>,
    val nutrients: Nutrients,
    val isFavorite: Boolean
)
```

#### MealEntry
```kotlin
data class MealEntry(
    val id: String,
    val recipeId: String,
    val dayOfWeek: Int,
    val slot: MealSlot
)

enum class MealSlot { BREAKFAST, LUNCH, DINNER }
```

#### ShoppingItem
```kotlin
data class ShoppingItem(
    val id: String,
    val ingredientName: String,
    val quantity: String,
    val section: String?,
    val checked: Boolean
)
```

#### UserPrefs
```kotlin
data class UserPrefs(
    val diet: String?,
    val intolerances: Set<String>,
    val caloriesPerDay: Int?,
    val budgetPerWeek: Int?,
    val theme: AppTheme
)

enum class AppTheme { SYSTEM, LIGHT, DARK }
```

---

## 🚀 Setup Instructions

### Prerequisites
- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: 21
- **Android SDK**: API level 26+ (Android 8.0+)
- **Spoonacular API Key**: [Get one here](https://spoonacular.com/food-api)
- **OpenAI API Key**: [Get one here](https://platform.openai.com/api-keys)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/[owner]/[repo].git
   cd mealmate
   ```

2. **Configure API Keys**
   
   Create a `local.properties` file in the root directory (use `local.properties.template` as reference):
   ```properties
   sdk.dir=/path/to/Android/sdk
   SPOONACULAR_API_KEY=your_spoonacular_api_key_here
   OPENAI_API_KEY=your_openai_api_key_here
   GEMINI_API_KEY=your_gemini_api_key_here  # Optional
   ```

3. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned `mealmate` directory
   - Wait for Gradle sync to complete

4. **Build the project**
   ```bash
   ./gradlew build
   ```

5. **Run the app**
   - Connect an Android device or start an emulator (API 26+)
   - Click the "Run" button in Android Studio or use:
   ```bash
   ./gradlew installDebug
   ```

### Configuration

#### Build Variants
- **Debug**: Development build with logging enabled
- **Release**: Production build with ProGuard optimization

#### Minimum Requirements
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 36 (Android 14)
- **Compile SDK**: 35

---

## 🧪 Testing

### Running Tests

#### Unit Tests
```bash
./gradlew test
```

#### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Test Coverage

The project includes comprehensive ViewModel unit tests covering:
- State management and updates
- Use case interactions
- Error handling
- User input validation
- Asynchronous operations

**Test Report Summary**:

<img src="images/Test Report Summary.png" alt="Test Report Summary - Comprehensive test results" width="60%">

*Complete test report showing successful ViewModel testing with JUnit, MockK, and Turbine*

---

## 🔐 Security

- **API Keys**: Stored securely in `local.properties` (not committed to version control)
- **BuildConfig**: API keys injected at build time
- **Network Security**: HTTPS-only connections
- **Data Privacy**: User data stored locally with Room encryption support

---

## 📱 Features Overview

### Core Features
- ✅ **User Authentication**: Secure login and registration
- ✅ **Recipe Discovery**: Search with filters (diet, intolerances, cuisine)
- ✅ **Recipe Details**: Comprehensive information with AI summaries
- ✅ **Favorites**: Save and manage favorite recipes
- ✅ **Weekly Meal Planning**: Organize meals by day and meal type
- ✅ **Shopping Lists**: Auto-generate from meal plans
- ✅ **AI Optimization**: Smart shopping list organization
- ✅ **Instant Meal Maker**: Generate meals from available ingredients
- ✅ **Offline Mode**: Full functionality without internet
- ✅ **Dark Theme**: System-aware theme switching
- ✅ **Notifications**: Weekly meal planning reminders

### Bonus Features
- ✅ **UI Animations**: Smooth transitions and motion
- ✅ **Offline Sync**: Automatic data synchronization
- ✅ **Camera Integration**: Scan fridge/pantry for ingredients

---

## 🔮 Future Enhancements

- [ ] **Social Features**: Share recipes and meal plans with friends
- [ ] **Nutrition Tracking**: Detailed calorie and macro tracking
- [ ] **Grocery Store Integration**: Direct ordering from shopping lists
- [ ] **Voice Commands**: Hands-free cooking instructions
- [ ] **Recipe Import**: Import recipes from websites
- [ ] **Meal Prep Mode**: Batch cooking suggestions
- [ ] **Multi-language Support**: Localization for more languages
- [ ] **Wear OS Support**: Smartwatch companion app
- [ ] **Widget Support**: Home screen widgets for quick access

---

## 👨‍💻 Author / Contributors

**Mercel Vubangsi**
- **MIU ID**: [Your MIU ID]
- **Course**: CS473 - Mobile Application Development
- **Block**: October 2025
- **Email**: [your.email@miu.edu]

---

## 📄 License

This project is developed as part of the CS473 course at Maharishi International University.

---

## 🙏 Acknowledgments

- **Spoonacular API** for providing comprehensive recipe data
- **OpenAI** for GPT-3.5-turbo AI capabilities
- **Android Jetpack** team for modern development tools
- **MIU Faculty** for guidance and support

---

## 📞 Support

For questions or issues, please contact:
- Email: [your.email@miu.edu]
- GitHub Issues: [https://github.com/[owner]/[repo]/issues](https://github.com/[owner]/[repo]/issues)

---

**Built with ❤️ using Kotlin and Jetpack Compose**

