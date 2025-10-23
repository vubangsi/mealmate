package com.mercel.mealmate.data.repository

import android.util.Log
import com.mercel.mealmate.data.local.dao.MealPlanDao
import com.mercel.mealmate.data.local.dao.RecipeDao
import com.mercel.mealmate.data.local.dao.ShoppingDao
import com.mercel.mealmate.data.local.entity.ShoppingItemEntity
import com.mercel.mealmate.data.mapper.toDomain
import com.mercel.mealmate.domain.model.ShoppingItem
import com.mercel.mealmate.domain.repository.AiRepository
import com.mercel.mealmate.domain.repository.ShoppingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val mealPlanDao: MealPlanDao,
    private val recipeDao: RecipeDao,
    private val aiRepository: AiRepository
) : ShoppingRepository {
    
    companion object {
        private const val TAG = "ShoppingRepositoryImpl"
    }

    override fun getShoppingList(): Flow<List<ShoppingItem>> {
        return shoppingDao.getAllItems().map { items ->
            items.map { it.toDomain() }
        }
    }

    override suspend fun generateShoppingListFromPlan() {
        Log.d(TAG, "Starting shopping list generation from meal plan...")

        val mealEntries = mealPlanDao.getAllMealEntries().first()
        if (mealEntries.isEmpty()) {
            throw Exception("No meal plan found. Please generate a meal plan first by going to the Plan tab.")
        }

        val ingredientsMap = mutableMapOf<String, MutableList<String>>()

        // Collect all ingredients from meal plan
        mealEntries.forEach { entry ->
            Log.d(TAG, "Processing meal entry: ${entry.recipeName} (ID: ${entry.recipeId})")
            val recipe = recipeDao.getRecipeById(entry.recipeId).first()
            
            if (recipe == null) {
                Log.w(TAG, "Recipe with ID ${entry.recipeId} not found in database")
            } else {
                Log.d(TAG, "Recipe ${recipe.title} has ${recipe.ingredients.size} ingredients")
                recipe.ingredients.forEach { ingredient ->
                    Log.d(TAG, "Ingredient: ${ingredient.name} - ${ingredient.quantity}")
                    val key = ingredient.name.lowercase().trim()
                    if (key.isNotBlank()) {
                        if (ingredientsMap.containsKey(key)) {
                            ingredientsMap[key]?.add(ingredient.quantity)
                        } else {
                            ingredientsMap[key] = mutableListOf(ingredient.quantity)
                        }
                    }
                }
            }
        }

        if (ingredientsMap.isEmpty()) {
            throw Exception("No ingredients found in meal plan. Please search for recipes in the Discover tab and add them to your meal plan.")
        }

        Log.d(TAG, "Collected ${ingredientsMap.size} unique ingredients")

        // Prepare ingredients list for AI optimization
        val ingredientsList = ingredientsMap.map { (name, quantities) ->
            "$name (${quantities.joinToString(", ")})"
        }

        // Get AI optimization for store sections
        Log.d(TAG, "Requesting AI optimization for shopping list with ${ingredientsList.size} items...")
        val optimizationResult = aiRepository.optimizeShoppingList(ingredientsList)

        // Handle result properly
        val sectionMap = optimizationResult.fold(
            onSuccess = { response ->
                Log.d(TAG, "AI optimization successful, parsing response...")
                parseAiOptimization(response)
            },
            onFailure = { exception ->
                Log.e(TAG, "AI optimization failed: ${exception.message}", exception)
                throw Exception("Failed to optimize shopping list: ${exception.message}", exception)
            }
        )

        // Clear existing list and add new items with sections
        shoppingDao.deleteAll()
        val items = ingredientsMap.map { (name, quantities) ->
            ShoppingItemEntity(
                id = UUID.randomUUID().toString(),
                ingredientName = name,
                quantity = quantities.joinToString(", "),
                section = sectionMap[name.lowercase()] ?: "Other",
                checked = false
            )
        }

        Log.d(TAG, "Generated ${items.size} shopping items with AI optimization")
        shoppingDao.insertItems(items)
    }
    
    private fun parseAiOptimization(optimizedResponse: String?): Map<String, String> {
        if (optimizedResponse.isNullOrBlank()) {
            Log.w(TAG, "AI optimization failed, using default sections")
            return emptyMap()
        }
        
        val sectionMap = mutableMapOf<String, String>()
        val lines = optimizedResponse.lines()
        var currentSection = "Other"
        
        lines.forEach { line ->
            val trimmedLine = line.trim()
            when {
                trimmedLine.contains("Produce", ignoreCase = true) && trimmedLine.contains(":") -> {
                    currentSection = "Produce"
                }
                trimmedLine.contains("Dairy", ignoreCase = true) && trimmedLine.contains(":") -> {
                    currentSection = "Dairy"
                }
                trimmedLine.contains("Meat", ignoreCase = true) && trimmedLine.contains(":") -> {
                    currentSection = "Meat"
                }
                trimmedLine.contains("Pantry", ignoreCase = true) && trimmedLine.contains(":") -> {
                    currentSection = "Pantry"
                }
                trimmedLine.contains("Frozen", ignoreCase = true) && trimmedLine.contains(":") -> {
                    currentSection = "Frozen"
                }
                trimmedLine.contains("Bakery", ignoreCase = true) && trimmedLine.contains(":") -> {
                    currentSection = "Bakery"
                }
                trimmedLine.startsWith("-") || trimmedLine.matches("\\d+\\..*".toRegex()) -> {
                    // Extract ingredient name from list item
                    val ingredient = trimmedLine
                        .removePrefix("-")
                        .replace("\\d+\\.".toRegex(), "")
                        .trim()
                        .split("(")[0] // Remove quantity part in parentheses
                        .lowercase()
                    
                    if (ingredient.isNotBlank()) {
                        sectionMap[ingredient] = currentSection
                    }
                }
            }
        }
        
        Log.d(TAG, "Parsed ${sectionMap.size} ingredient sections from AI response")
        return sectionMap
    }

    override suspend fun toggleItemChecked(itemId: String) {
        val item = shoppingDao.getAllItems().first().find { it.id == itemId }
        item?.let {
            shoppingDao.updateCheckedStatus(itemId, !it.checked)
        }
    }

    override suspend fun addItem(name: String, quantity: String, section: String?, estimatedPrice: Double?) {
        val item = ShoppingItemEntity(
            id = UUID.randomUUID().toString(),
            ingredientName = name,
            quantity = quantity,
            section = section,
            estimatedPrice = estimatedPrice,
            checked = false
        )
        shoppingDao.insertItem(item)
    }

    override suspend fun removeItem(itemId: String) {
        shoppingDao.deleteItem(itemId)
    }

    override suspend fun clearCheckedItems() {
        shoppingDao.deleteCheckedItems()
    }

    override suspend fun scanAvailableIngredients() {
        Log.d(TAG, "Starting fridge/pantry scan for available ingredients...")
        
        // In a real implementation, this would use camera to capture images
        // For now, simulate AI analysis with placeholder logic
        val prompt = """
        Analyze the uploaded fridge/pantry images and identify all visible food ingredients and items.
        
        For each item identified:
        1. Extract the ingredient name
        2. Estimate the quantity available 
        3. Determine if it's sufficient for cooking
        4. Consider expiration dates if visible
        
        Return a structured list of available ingredients that can be used for meal planning.
        Focus on common cooking ingredients like:
        - Proteins (meat, eggs, dairy)
        - Vegetables and fruits
        - Grains and starches
        - Condiments and spices
        - Canned/packaged goods
        
        Format as: ingredient_name | estimated_quantity | freshness_status
        """
        
        val aiResult = aiRepository.analyzeAvailableIngredients(prompt)
        
        aiResult.fold(
            onSuccess = { analysis ->
                Log.d(TAG, "AI analysis successful: $analysis")
                // Parse AI response and update shopping list items as available at home
                updateItemsBasedOnAvailability(analysis)
            },
            onFailure = { exception ->
                Log.e(TAG, "Failed to analyze available ingredients: ${exception.message}", exception)
                throw Exception("Failed to scan ingredients: ${exception.message}", exception)
            }
        )
    }

    override suspend fun optimizeShoppingListWithAI() {
        Log.d(TAG, "Starting AI-powered shopping list optimization...")
        
        val currentItems = shoppingDao.getAllItems().first()
        if (currentItems.isEmpty()) {
            throw Exception("No items in shopping list to optimize")
        }
        
        val itemsList = currentItems.map { "${it.ingredientName} (${it.quantity})" }
        
        val optimizationPrompt = """
        You are an expert grocery shopping optimizer. Optimize this shopping list for maximum efficiency and cost savings.
        
        CURRENT SHOPPING LIST:
        ${itemsList.joinToString("\n")}
        
        OPTIMIZATION GOALS:
        1. CONSOLIDATION: Combine similar items to reduce redundancy
        2. SUBSTITUTIONS: Suggest cheaper or more versatile alternatives  
        3. BULK BUYING: Identify items better bought in larger quantities
        4. SEASONAL PRICING: Consider seasonal availability and pricing
        5. STORE LAYOUT: Group items by typical grocery store sections
        6. MULTI-USE INGREDIENTS: Prioritize ingredients that work across multiple recipes
        
        OPTIMIZATION RULES:
        - Minimize total number of unique items while maintaining recipe requirements
        - Suggest quantity adjustments for better value (family packs, bulk items)
        - Estimate realistic grocery store prices in USD
        - Group by store sections: Produce, Dairy, Meat, Pantry, Frozen, Bakery
        - Flag items that can often be substituted or are optional
        
        RESPONSE FORMAT:
        For each optimized item, provide:
        Item: [name] | Quantity: [amount] | Price: $[X.XX] | Section: [store section] | Notes: [optimization reason]
        
        Then provide a summary with:
        - Total estimated cost
        - Money saved through optimizations  
        - Number of items reduced from original list
        - Key optimization insights
        """
        
        val optimizationResult = aiRepository.optimizeShoppingList(itemsList, optimizationPrompt)
        
        optimizationResult.fold(
            onSuccess = { optimizedResponse ->
                Log.d(TAG, "AI optimization successful, updating shopping list...")
                parseAndUpdateOptimizedList(optimizedResponse)
            },
            onFailure = { exception ->
                Log.e(TAG, "Failed to optimize shopping list: ${exception.message}", exception)
                throw Exception("Failed to optimize shopping list: ${exception.message}", exception)
            }
        )
    }

    override suspend fun placeDeliveryOrder() {
        Log.d(TAG, "Placing delivery order for shopping list...")
        
        val uncheckedItems = shoppingDao.getAllItems().first()
            .filterNot { it.checked || it.isAvailableAtHome }
        
        if (uncheckedItems.isEmpty()) {
            throw Exception("No items to order - all items are checked or available at home")
        }
        
        val totalCost = uncheckedItems.sumOf { it.estimatedPrice ?: 0.0 }
        
        // In a real implementation, this would integrate with delivery services like:
        // - Instacart API
        // - Amazon Fresh API  
        // - Local grocery store APIs
        // - DoorDash/Uber Eats grocery delivery
        
        Log.d(TAG, "Order summary: ${uncheckedItems.size} items, estimated total: $${String.format("%.2f", totalCost)}")
        
        // Simulate successful order placement
        // In reality, this would handle payment processing, delivery scheduling, etc.
        throw Exception("Delivery integration not yet implemented. This would connect to grocery delivery services.")
    }

    override suspend fun updateItemPrice(itemId: String, price: Double) {
        // Update item price in database
        // This would require adding an update method to ShoppingDao
        val item = shoppingDao.getAllItems().first().find { it.id == itemId }
        item?.let {
            val updatedItem = it.copy(estimatedPrice = price)
            shoppingDao.updateItem(updatedItem)
        }
    }

    override suspend fun markItemAsAvailableAtHome(itemId: String, isAvailable: Boolean) {
        val item = shoppingDao.getAllItems().first().find { it.id == itemId }
        item?.let {
            val updatedItem = it.copy(isAvailableAtHome = isAvailable)
            shoppingDao.updateItem(updatedItem)
        }
    }

    private suspend fun updateItemsBasedOnAvailability(analysis: String) {
        // Parse AI analysis and mark items as available at home
        val availableIngredients = analysis.lines()
            .filter { it.contains("|") }
            .map { line ->
                val parts = line.split("|")
                if (parts.size >= 2) {
                    parts[0].trim().lowercase()
                } else null
            }
            .filterNotNull()

        val currentItems = shoppingDao.getAllItems().first()
        currentItems.forEach { item ->
            val isAvailable = availableIngredients.any { 
                it.contains(item.ingredientName.lowercase()) || 
                item.ingredientName.lowercase().contains(it) 
            }
            if (isAvailable) {
                markItemAsAvailableAtHome(item.id, true)
            }
        }
    }

    private suspend fun parseAndUpdateOptimizedList(optimizedResponse: String) {
        // Parse AI optimization response and update shopping list
        val optimizedItems = mutableListOf<ShoppingItemEntity>()
        val lines = optimizedResponse.lines()
        
        lines.forEach { line ->
            if (line.startsWith("Item:") && line.contains("|")) {
                try {
                    val parts = line.split("|")
                    val itemName = parts[0].substringAfter("Item:").trim()
                    val quantity = parts[1].substringAfter("Quantity:").trim()
                    val priceStr = parts[2].substringAfter("Price: $").trim()
                    val section = parts[3].substringAfter("Section:").trim()
                    val notes = if (parts.size > 4) parts[4].substringAfter("Notes:").trim() else null
                    
                    val price = priceStr.toDoubleOrNull()
                    
                    val optimizedItem = ShoppingItemEntity(
                        id = UUID.randomUUID().toString(),
                        ingredientName = itemName,
                        quantity = quantity,
                        section = section,
                        estimatedPrice = price,
                        notes = notes,
                        checked = false,
                        isAvailableAtHome = false
                    )
                    optimizedItems.add(optimizedItem)
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to parse optimization line: $line", e)
                }
            }
        }
        
        if (optimizedItems.isNotEmpty()) {
            // Replace current list with optimized items
            shoppingDao.deleteAll()
            shoppingDao.insertItems(optimizedItems)
            Log.d(TAG, "Updated shopping list with ${optimizedItems.size} optimized items")
        }
    }
}
