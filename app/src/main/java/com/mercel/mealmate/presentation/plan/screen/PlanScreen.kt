package com.mercel.mealmate.presentation.plan.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mercel.mealmate.domain.model.MealEntry
import com.mercel.mealmate.domain.model.MealSlot
import com.mercel.mealmate.presentation.plan.viewmodel.PlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanScreen(
    onRecipeClick: (String) -> Unit,
    viewModel: PlanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weekly Plan") },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, "Menu")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Generate Plan") },
                            onClick = {
                                viewModel.generateWeeklyPlan()
                                showMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.AutoAwesome, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Clear Plan") },
                            onClick = {
                                viewModel.clearPlan()
                                showMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.Delete, null) }
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.isGenerating -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Generating meal plan...")
                    }
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.ErrorOutline,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Error generating plan",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error ?: "Unknown error",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.generateWeeklyPlan() }) {
                            Text("Try Again")
                        }
                    }
                }
                uiState.mealEntries.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No meals planned yet")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.generateWeeklyPlan() }) {
                            Icon(Icons.Default.AutoAwesome, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate Plan")
                        }
                    }
                }
                else -> {
                    WeeklyPlanContent(
                        mealEntries = uiState.mealEntries,
                        onRecipeClick = onRecipeClick,
                        onRemoveMeal = viewModel::removeMeal
                    )
                }
            }
        }
    }
}

@Composable
fun WeeklyPlanContent(
    mealEntries: List<MealEntry>,
    onRecipeClick: (String) -> Unit,
    onRemoveMeal: (String) -> Unit
) {
    val groupedByDay = mealEntries.groupBy { it.dayOfWeek }
    val dayNames = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        groupedByDay.forEach { (dayOfWeek, meals) ->
            item {
                Text(
                    text = dayNames.getOrNull(dayOfWeek - 1) ?: "Day $dayOfWeek",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(meals.sortedBy { it.slot }) { meal ->
                MealEntryCard(
                    meal = meal,
                    onClick = { onRecipeClick(meal.recipeId) },
                    onRemove = { onRemoveMeal(meal.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealEntryCard(
    meal: MealEntry,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = meal.recipeImageUrl,
                contentDescription = meal.recipeName,
                modifier = Modifier
                    .size(60.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = meal.slot.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = meal.recipeName,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Close, "Remove")
            }
        }
    }
}
