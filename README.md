# MealMate - Smart Meal Planning Assistant

## Problem Description

Planning healthy, balanced meals for the week can be time-consuming and overwhelming. Many people struggle with:
- Finding recipes that match their dietary preferences and restrictions
- Creating a balanced weekly meal plan
- Generating organized shopping lists from their meal plans
- Tracking nutritional information
- Managing favorite recipes

**MealMate** solves these problems by providing an intelligent meal planning assistant that helps users discover recipes, plan their weekly meals, generate shopping lists, and leverage AI to optimize their meal planning experience.

**Target Users:** Health-conscious individuals, busy professionals, families, and anyone looking to simplify meal planning and grocery shopping.

## Features Overview

### Core Features
- **Recipe Discovery**: Search and filter recipes by cuisine, diet, and intolerances
- **Recipe Details**: View comprehensive recipe information including ingredients, instructions, and nutritional data
- **Favorites**: Save favorite recipes for quick access
- **Weekly Meal Planning**: Organize meals for breakfast, lunch, and dinner across 7 days
- **AI-Powered Plan Generation**: Automatically generate balanced weekly meal plans using Gemini AI
- **Shopping List**: Auto-generate shopping lists from meal plans with item checking
- **User Preferences**: Customize dietary preferences, calorie targets, and budget
- **Offline Support**: Access cached recipes and meal plans without internet connection
- **Background Sync**: Automatic cache refresh and weekly reminders via WorkManager

### Advanced Features
- **AI Recipe Summaries**: Generate engaging recipe descriptions using Gemini AI
- **AI Shopping List Optimization**: Organize shopping items by store section
- **AI Ingredient Substitutions**: Get dietary-friendly ingredient alternatives
- **Material 3 Design**: Modern, accessible UI with dynamic theming
- **Smooth Animations**: Enhanced UX with transition animations

## System Architecture

MealMate follows **Clean Architecture** principles with clear separation of concerns:

### Architecture Layers

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  (UI, ViewModels, UI States, Screens)  │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│          Domain Layer                   │
│  (Use Cases, Models, Repository         │
│   Interfaces)                           │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│           Data Layer                    │
│  (Repository Implementations, Room,     │
│   Retrofit, DataStore)                  │
└─────────────────────────────────────────┘
```

### Key Architectural Patterns
- **MVVM (Model-View-ViewModel)**: Separates UI logic from business logic
- **Repository Pattern**: Abstracts data sources (local and remote)
- **Dependency Injection**: Hilt for managing dependencies
- **Single Activity Architecture**: Navigation Component with single MainActivity
- **Offline-First**: Room database as source of truth with API fallback

## Use Case Diagram

```
                    ┌─────────────┐
                    │    User     │
                    └──────┬──────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────┐
│Search Recipes│   │  Plan Meals  │   │Manage Shopping│
└──────┬───────┘   └──────┬───────┘   └──────┬───────┘
       │                  │                  │
       ▼                  ▼                  ▼
┌──────────────┐   ┌──────────────┐   ┌──────────────┐
│View Details  │   │Generate Plan │   │Generate List │
└──────┬───────┘   │  (AI)        │   │  (AI)        │
       │           └──────────────┘   └──────────────┘
       ▼
┌──────────────┐
│Add to Fav/Plan│
└──────────────┘
```

## Technology Stack

| Category | Technology | Purpose |
|----------|-----------|---------|
| **Language** | Kotlin | Primary development language |
| **UI Framework** | Jetpack Compose | Modern declarative UI |
| **Design System** | Material 3 | UI components and theming |
| **Architecture** | Clean Architecture | Layered separation of concerns |
| **DI** | Hilt | Dependency injection |
| **Local Database** | Room | Offline data persistence |
| **Remote API** | Retrofit + Moshi | Network requests and JSON parsing |
| **Preferences** | DataStore | Key-value storage |
| **Background Tasks** | WorkManager | Scheduled tasks and sync |
| **Navigation** | Navigation Compose | Screen navigation |
| **Image Loading** | Coil | Async image loading |
| **Concurrency** | Coroutines + Flow | Async operations |
| **Generative AI** | Gemini AI | AI-powered features |
| **Testing** | JUnit, MockK, Turbine | Unit testing |

## Package Hierarchy

```
com.mercel.mealmate/
├── core/
│   └── util/
│       ├── Constants.kt
│       ├── NetworkMonitor.kt
│       └── UiState.kt
├── data/
│   ├── local/
│   │   ├── converter/
│   │   │   └── Converters.kt
│   │   ├── dao/
│   │   │   ├── MealPlanDao.kt
│   │   │   ├── RecipeDao.kt
│   │   │   └── ShoppingDao.kt
│   │   ├── entity/
│   │   │   ├── MealEntryEntity.kt
│   │   │   ├── RecipeEntity.kt
│   │   │   └── ShoppingItemEntity.kt
│   │   └── MealMateDatabase.kt
│   ├── mapper/
│   │   ├── MealEntryMapper.kt
│   │   ├── RecipeMapper.kt
│   │   └── ShoppingItemMapper.kt
│   ├── remote/
│   │   ├── api/
│   │   │   └── SpoonacularApi.kt
│   │   └── dto/
│   │       └── RecipeDto.kt
│   └── repository/
│       ├── AiRepositoryImpl.kt
│       ├── MealPlanRepositoryImpl.kt
│       ├── PreferencesRepositoryImpl.kt
│       ├── RecipeRepositoryImpl.kt
│       └── ShoppingRepositoryImpl.kt
├── di/
│   ├── AiModule.kt
│   ├── AppModule.kt
│   ├── DatabaseModule.kt
│   ├── DataStoreModule.kt
│   ├── NetworkModule.kt
│   └── RepositoryModule.kt
├── domain/
│   ├── model/
│   │   ├── MealEntry.kt
│   │   ├── Recipe.kt
│   │   ├── ShoppingItem.kt
│   │   └── UserPrefs.kt
│   ├── repository/
│   │   ├── AiRepository.kt
│   │   ├── MealPlanRepository.kt
│   │   ├── PreferencesRepository.kt
│   │   ├── RecipeRepository.kt
│   │   └── ShoppingRepository.kt
│   └── usecase/
│       ├── GenerateWeeklyPlanUseCase.kt
│       ├── GetRecipeUseCase.kt
│       ├── SearchRecipesUseCase.kt
│       ├── ToggleFavoriteUseCase.kt
│       └── ToggleShoppingItemUseCase.kt
├── presentation/
│   ├── detail/
│   │   ├── screen/
│   │   │   └── DetailScreen.kt
│   │   ├── uistate/
│   │   │   └── DetailUiState.kt
│   │   └── viewmodel/
│   │       └── DetailViewModel.kt
│   ├── discover/
│   │   ├── screen/
│   │   │   └── DiscoverScreen.kt
│   │   ├── uistate/
│   │   │   └── DiscoverUiState.kt
│   │   └── viewmodel/
│   │       └── DiscoverViewModel.kt
│   ├── navigation/
│   │   └── NavGraph.kt
│   ├── plan/
│   │   ├── screen/
│   │   │   └── PlanScreen.kt
│   │   ├── uistate/
│   │   │   └── PlanUiState.kt
│   │   └── viewmodel/
│   │       └── PlanViewModel.kt
│   ├── settings/
│   │   ├── screen/
│   │   │   └── SettingsScreen.kt
│   │   ├── uistate/
│   │   │   └── SettingsUiState.kt
│   │   └── viewmodel/
│   │       └── SettingsViewModel.kt
│   └── shopping/
│       ├── screen/
│       │   └── ShoppingScreen.kt
│       ├── uistate/
│       │   └── ShoppingUiState.kt
│       └── viewmodel/
│           └── ShoppingViewModel.kt
├── worker/
│   ├── SyncWorker.kt
│   └── WeeklyReminderWorker.kt
├── MainActivity.kt
└── MealMateApplication.kt
```

## Screenshots / Screen Flow

### Main Screens
1. **Discover Screen**: Browse and search recipes with filters
2. **Recipe Detail Screen**: View full recipe with AI-generated summary
3. **Weekly Plan Screen**: Organize meals across 7 days
4. **Shopping List Screen**: Check off items as you shop
5. **Settings Screen**: Configure preferences and dietary restrictions

### Navigation Flow
```
Discover → Detail → Add to Plan → Plan Screen → Generate Shopping List → Shopping Screen
   ↓                                    ↓
Settings                          AI Generate Plan
```

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 21
- Android SDK API 26+
- Spoonacular API Key (get from https://spoonacular.com/food-api)
- Google Gemini API Key (get from https://ai.google.dev/)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd mealmate
   ```

2. **Configure API Keys**
   
   Create or edit `local.properties` in the project root:
   ```properties
   SPOONACULAR_API_KEY=your_spoonacular_api_key_here
   GEMINI_API_KEY=your_gemini_api_key_here
   ```

3. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the mealmate directory
   - Wait for Gradle sync to complete

4. **Build and Run**
   - Connect an Android device or start an emulator (API 26+)
   - Click "Run" or press Shift+F10
   - Select your device and click OK

### Troubleshooting
- If Gradle sync fails, try "File → Invalidate Caches / Restart"
- Ensure you have the latest Android SDK tools installed
- Check that API keys are properly set in `local.properties`

## API Reference

### Spoonacular API
- **Base URL**: `https://api.spoonacular.com/`
- **Endpoints Used**:
  - `GET /recipes/complexSearch` - Search recipes with filters
  - `GET /recipes/{id}/information` - Get detailed recipe information

### Gemini AI API
- **Model**: `gemini-pro`
- **Use Cases**:
  - Weekly meal plan generation
  - Recipe summaries
  - Shopping list optimization
  - Ingredient substitutions

## Data Models

### Recipe
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
    val isFavorite: Boolean = false
)
```

### MealEntry
```kotlin
data class MealEntry(
    val id: String,
    val recipeId: String,
    val dayOfWeek: Int, // 1-7
    val slot: MealSlot, // BREAKFAST, LUNCH, DINNER
    val recipeName: String,
    val recipeImageUrl: String?
)
```

### ShoppingItem
```kotlin
data class ShoppingItem(
    val id: String,
    val ingredientName: String,
    val quantity: String,
    val section: String?,
    val checked: Boolean = false
)
```

## Testing

### Unit Tests
The project includes comprehensive ViewModel unit tests using JUnit, MockK, and Turbine.

**Example Test**: `DiscoverViewModelTest.kt`
- Tests initial state
- Tests successful recipe search
- Tests error handling
- Tests search query updates
- Tests diet filter changes
- Tests favorite toggling

### Running Tests
```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests DiscoverViewModelTest

# Generate test coverage report
./gradlew testDebugUnitTestCoverage
```

### Test Coverage
- ViewModel logic: Comprehensive coverage
- State management: Verified with Turbine
- Use case invocations: Mocked with MockK

## Generative AI Integration

### AI Features Implemented

#### 1. Weekly Meal Plan Generation
**Prompt Example**:
```
Generate a weekly meal plan with the following preferences:
- diet: vegetarian
- caloriesPerDay: 2000
- budgetPerWeek: 150

Provide a balanced meal plan for breakfast, lunch, and dinner for 7 days.
```

**Code Snippet**:
```kotlin
suspend fun generateWeeklyPlan(preferences: Map<String, Any>): Result<String> {
    val prompt = buildString {
        append("Generate a weekly meal plan with the following preferences:\n")
        preferences.forEach { (key, value) ->
            append("- $key: $value\n")
        }
        append("\nProvide a balanced meal plan for breakfast, lunch, and dinner for 7 days.")
    }
    
    val response = generativeModel.generateContent(prompt)
    return Result.success(response.text ?: "No plan generated")
}
```

#### 2. Recipe Summary Generation
**Prompt Example**:
```
Create a brief, engaging summary for this recipe:
Title: Spaghetti Carbonara
Ingredients: pasta, eggs, bacon, parmesan, black pepper

Provide a 2-3 sentence description highlighting key flavors and appeal.
```

#### 3. Shopping List Optimization
**Prompt Example**:
```
Optimize this shopping list by grouping items by store section:
tomatoes
milk
chicken breast
bread
cheese

Group items into: Produce, Dairy, Meat, Pantry, Frozen, Bakery, Other
```

#### 4. Ingredient Substitution
**Prompt Example**:
```
Suggest a vegan-friendly substitution for: butter
```

### AI Integration Screenshots
*(Screenshots would be included showing AI-generated content in the app)*

## Future Enhancements

### Planned Features
- **Recipe Rating & Reviews**: Allow users to rate and review recipes
- **Social Sharing**: Share meal plans and recipes with friends
- **Barcode Scanner**: Scan products to add to shopping list
- **Meal Prep Mode**: Batch cooking suggestions and prep instructions
- **Nutritional Tracking**: Daily calorie and macro tracking
- **Recipe Import**: Import recipes from URLs
- **Voice Commands**: Voice-activated recipe search and navigation
- **Grocery Delivery Integration**: Connect with delivery services
- **Multi-language Support**: Expand beyond English and Spanish
- **Wear OS Companion**: Shopping list on smartwatch

### Known Limitations
- Requires internet for initial recipe search
- API rate limits on free tier
- Limited recipe database (Spoonacular free tier)
- AI responses may vary in quality

## Wholeness / SCI Connection (MIU Context)

### Science of Creative Intelligence Principle: **Natural Law**

MealMate embodies the principle of Natural Law by creating a system that works in harmony with the natural rhythms of life. Just as nature operates through cycles and patterns, MealMate helps users establish healthy eating patterns through:

1. **Weekly Cycles**: The app organizes meals in weekly cycles, mirroring the natural rhythm of the week
2. **Balance**: AI-generated meal plans ensure nutritional balance, reflecting nature's principle of equilibrium
3. **Efficiency**: Automated shopping lists eliminate waste, aligning with nature's efficiency
4. **Adaptation**: The app adapts to individual preferences, like nature adapts to different environments

By following these natural principles, MealMate helps users develop sustainable, healthy eating habits that support their overall wellbeing and contribute to a more organized, stress-free lifestyle.

### Reflection
Creating MealMate has been an exercise in applying systematic thinking to solve real-world problems. The Clean Architecture approach mirrors the orderly structure found in nature, where each layer has a specific purpose and interacts harmoniously with others. The integration of AI represents the creative intelligence aspect, where technology enhances human capability in making better decisions about nutrition and health.

## Author / Contributors

**Mercel Vubangsi**  
MIU ID: [Your MIU ID]  
Course: CS473 - Mobile Application Development  
Block: October 2025  
Maharishi International University

---

## License

This project is created for educational purposes as part of the CS473 course at Maharishi International University.

## Acknowledgments

- Spoonacular API for recipe data
- Google Gemini AI for generative AI capabilities
- Android Jetpack libraries
- Material Design 3 guidelines
- MIU CS473 course instructors and peers
