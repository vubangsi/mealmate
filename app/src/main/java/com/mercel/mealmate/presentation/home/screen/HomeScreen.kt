package com.mercel.mealmate.presentation.home.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mercel.mealmate.presentation.home.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDiscover: () -> Unit,
    onNavigateToInstantMealMaker: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Welcome to MealMate")
                        Text(
                            text = "What would you like to do today?",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Quick Stats Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    QuickStat(
                        icon = Icons.Default.Restaurant,
                        title = "Recipes Found",
                        value = uiState.totalRecipes.toString()
                    )
                    QuickStat(
                        icon = Icons.Default.CalendarToday,
                        title = "Days Planned",
                        value = uiState.plannedDays.toString()
                    )
                    QuickStat(
                        icon = Icons.Default.ShoppingCart,
                        title = "Shopping Items",
                        value = uiState.shoppingItems.toString()
                    )
                }
            }

            // Messages and Loading States
            uiState.message?.let { message ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { viewModel.clearMessage() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                }
            }

            uiState.error?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { viewModel.clearMessage() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                }
            }

            if (uiState.isLoading) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Processing your request...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Grid Tiles - Only functional ones
            val homeTiles = listOf(
                HomeTileData(
                    id = "discover",
                    title = "Discover",
                    subtitle = "Find new recipes",
                    icon = Icons.Default.Search,
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    iconColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                HomeTileData(
                    id = "instant_meal",
                    title = "Instant Meal",
                    subtitle = "From your fridge",
                    icon = Icons.Default.CameraAlt,
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    iconColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(homeTiles) { tile ->
                    HomeTile(
                        tile = tile,
                        onClick = {
                            when (tile.id) {
                                "discover" -> onNavigateToDiscover()
                                "instant_meal" -> onNavigateToInstantMealMaker()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun QuickStat(
    icon: ImageVector,
    title: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun HomeTile(
    tile: HomeTileData,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = tile.backgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                tile.icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = tile.iconColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = tile.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = tile.textColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tile.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = tile.textColor.copy(alpha = 0.8f)
            )
        }
    }
}

data class HomeTileData(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val backgroundColor: androidx.compose.ui.graphics.Color,
    val iconColor: androidx.compose.ui.graphics.Color,
    val textColor: androidx.compose.ui.graphics.Color
)

