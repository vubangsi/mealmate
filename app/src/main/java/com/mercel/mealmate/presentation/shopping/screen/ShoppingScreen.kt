package com.mercel.mealmate.presentation.shopping.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mercel.mealmate.domain.model.ShoppingItem
import com.mercel.mealmate.presentation.shopping.viewmodel.ShoppingViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(
    viewModel: ShoppingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping List") },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, "Menu")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Generate from Plan") },
                            onClick = {
                                viewModel.generateFromPlan()
                                showMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.AutoAwesome, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Scan Fridge/Pantry") },
                            onClick = {
                                viewModel.scanAvailableIngredients()
                                showMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.CameraAlt, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Optimize List") },
                            onClick = {
                                viewModel.optimizeShoppingList()
                                showMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.TrendingUp, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Clear Checked") },
                            onClick = {
                                viewModel.clearCheckedItems()
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
                            text = "Error generating list",
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
                        Button(onClick = { viewModel.generateFromPlan() }) {
                            Text("Try Again")
                        }
                    }
                }
                uiState.items.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No items in shopping list")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.generateFromPlan() }) {
                            Icon(Icons.Default.AutoAwesome, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate from Plan")
                        }
                    }
                }
                else -> {
                    Column {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.items) { item ->
                                ShoppingItemCard(
                                    item = item,
                                    onCheckedChange = { viewModel.toggleItemChecked(item.id) },
                                    onRemove = { viewModel.removeItem(item.id) },
                                    onEdit = { viewModel.editItem(item.id) }
                                )
                            }
                        }
                        
                        // Total and Order Section
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
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
                                        text = "Estimated Total:",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    val totalPrice = uiState.items
                                        .filterNot { it.checked || it.isAvailableAtHome }
                                        .sumOf { it.estimatedPrice ?: 0.0 }
                                    Text(
                                        text = NumberFormat.getCurrencyInstance(Locale.US).format(totalPrice),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { viewModel.optimizeShoppingList() },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.TrendingUp, null)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Optimize")
                                    }
                                    
                                    Button(
                                        onClick = { viewModel.placeOrder() },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.ShoppingCart, null)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Order")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    onCheckedChange: () -> Unit,
    onRemove: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                item.isAvailableAtHome -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                item.checked -> MaterialTheme.colorScheme.surfaceVariant
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = item.checked,
                    onCheckedChange = { onCheckedChange() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.ingredientName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            textDecoration = if (item.checked) TextDecoration.LineThrough else null
                        )
                        item.estimatedPrice?.let { price ->
                            Text(
                                text = NumberFormat.getCurrencyInstance(Locale.US).format(price),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (item.isAvailableAtHome) Color.Green else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    
                    Text(
                        text = item.quantity,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item.section?.let { section ->
                            AssistChip(
                                onClick = { },
                                label = { Text(section, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                        
                        if (item.isAvailableAtHome) {
                            AssistChip(
                                onClick = { },
                                label = { Text("Available at Home", style = MaterialTheme.typography.labelSmall) },
                                leadingIcon = { Icon(Icons.Default.Home, null, modifier = Modifier.size(12.dp)) }
                            )
                        }
                    }
                    
                    item.notes?.let { notes ->
                        Text(
                            text = notes,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, "Edit")
                    }
                    IconButton(onClick = onRemove) {
                        Icon(Icons.Default.Delete, "Remove")
                    }
                }
            }
        }
    }
}
