package com.mercel.mealmate.presentation.settings.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mercel.mealmate.core.util.Constants
import com.mercel.mealmate.domain.model.AppTheme
import com.mercel.mealmate.presentation.settings.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onLogout: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSaveSnackbar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 80.dp), // Add extra bottom padding for navigation bar
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Theme Section
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            var showThemeDialog by remember { mutableStateOf(false) }
            Card(
                onClick = { showThemeDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Theme", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = uiState.userPrefs.theme.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(Icons.Default.ChevronRight, null)
                }
            }

            if (showThemeDialog) {
                AlertDialog(
                    onDismissRequest = { showThemeDialog = false },
                    title = { Text("Select Theme") },
                    text = {
                        Column {
                            AppTheme.values().forEach { theme ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    RadioButton(
                                        selected = uiState.userPrefs.theme == theme,
                                        onClick = {
                                            viewModel.updateTheme(theme)
                                            showThemeDialog = false
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = theme.name,
                                        modifier = Modifier.padding(top = 12.dp)
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showThemeDialog = false }) {
                            Text("Close")
                        }
                    }
                )
            }

            Divider()

            // Dietary Preferences
            Text(
                text = "Dietary Preferences",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            var showDietDialog by remember { mutableStateOf(false) }
            Card(
                onClick = { showDietDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Diet", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = uiState.userPrefs.diet ?: "None",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(Icons.Default.ChevronRight, null)
                }
            }

            if (showDietDialog) {
                AlertDialog(
                    onDismissRequest = { showDietDialog = false },
                    title = { Text("Select Diet") },
                    text = {
                        Column {
                            Constants.DIET_OPTIONS.forEach { diet ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    RadioButton(
                                        selected = uiState.userPrefs.diet == diet,
                                        onClick = {
                                            viewModel.updateDiet(diet)
                                            showDietDialog = false
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = diet,
                                        modifier = Modifier.padding(top = 12.dp)
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showDietDialog = false }) {
                            Text("Close")
                        }
                    }
                )
            }

            Divider()

            // Goals
            Text(
                text = "Goals",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            var caloriesText by remember { mutableStateOf(uiState.userPrefs.caloriesPerDay?.toString() ?: "") }
            OutlinedTextField(
                value = caloriesText,
                onValueChange = { 
                    caloriesText = it
                    it.toIntOrNull()?.let { calories ->
                        viewModel.updateCalories(calories)
                    }
                },
                label = { Text("Daily Calorie Target") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.LocalFireDepartment, null) }
            )

            var budgetText by remember { mutableStateOf(uiState.userPrefs.budgetPerWeek?.toString() ?: "") }
            OutlinedTextField(
                value = budgetText,
                onValueChange = {
                    budgetText = it
                    it.toIntOrNull()?.let { budget ->
                        viewModel.updateBudget(budget)
                    }
                },
                label = { Text("Weekly Budget ($)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.AttachMoney, null) }
            )

            HorizontalDivider()

            // Account Section
            Text(
                text = "Account",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Button(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout")
            }

            Divider()

            // Save Settings Button
            Button(
                onClick = {
                    showSaveSnackbar = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Settings")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Save confirmation snackbar
        if (showSaveSnackbar) {
            LaunchedEffect(Unit) {
                showSaveSnackbar = false
            }
        }
    }
}
