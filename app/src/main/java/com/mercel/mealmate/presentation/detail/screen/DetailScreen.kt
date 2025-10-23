package com.mercel.mealmate.presentation.detail.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.mercel.mealmate.presentation.detail.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onNavigateBack: () -> Unit,
    onAddToPlan: (String) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            if (uiState.recipe?.isFavorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            "Favorite"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.recipe != null) {
                ExtendedFloatingActionButton(
                    onClick = { onAddToPlan(uiState.recipe!!.id) },
                    icon = { Icon(Icons.Default.Add, null) },
                    text = { Text("Add to Plan") }
                )
            }
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
                uiState.error != null -> {
                    Text(
                        text = uiState.error ?: "Error",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                uiState.recipe != null -> {
                    RecipeDetailContent(
                        recipe = uiState.recipe!!,
                        aiSummary = uiState.aiSummary,
                        isGeneratingSummary = uiState.isGeneratingSummary,
                        summaryError = uiState.summaryError,
                        onGenerateSummary = viewModel::generateSummary
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeDetailContent(
    recipe: com.mercel.mealmate.domain.model.Recipe,
    aiSummary: String?,
    isGeneratingSummary: Boolean,
    summaryError: String? = null,
    onGenerateSummary: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Image
        AsyncImage(
            model = recipe.imageUrl,
            contentDescription = recipe.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Title
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoChip(
                    icon = Icons.Default.Schedule,
                    text = "${recipe.readyInMinutes} min"
                )
                InfoChip(
                    icon = Icons.Default.Restaurant,
                    text = "${recipe.servings} servings"
                )
                InfoChip(
                    icon = Icons.Default.LocalFireDepartment,
                    text = "${recipe.nutrients.calories} cal"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI Summary Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AI Summary",
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (aiSummary == null && summaryError == null) {
                            Button(
                                onClick = onGenerateSummary,
                                enabled = !isGeneratingSummary
                            ) {
                                if (isGeneratingSummary) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Generate")
                                }
                            }
                        }
                    }
                    if (aiSummary != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = aiSummary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    if (summaryError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = summaryError,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onGenerateSummary,
                            enabled = !isGeneratingSummary
                        ) {
                            Text("Try Again")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ingredients
            Text(
                text = "Ingredients",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            recipe.ingredients.forEach { ingredient ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("• ", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "${ingredient.quantity} ${ingredient.name}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Instructions
            Text(
                text = "Instructions",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            recipe.steps.forEachIndexed { index, step ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "${index + 1}. ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Card {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
