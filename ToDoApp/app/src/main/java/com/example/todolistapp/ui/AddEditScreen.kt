@file:Suppress("FunctionName")
package com.example.todolistapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todolistapp.R
import com.example.todolistapp.data.Task

@Composable
fun OutlinedTextFieldWithClearAndError(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = stringResource(R.string.clear_content_description, label)
                    )
                }
            }
        },
        isError = isError,
        supportingText = {
            Row {
                if (isError) Text(errorMessage)
            }
        },
        modifier = modifier
    )
}

@Composable
fun SwitchWithText(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text)
        Spacer(modifier = Modifier.padding(8.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

// Nasza własna funkcja do walidacji (sprawdza czy pola nie są puste)
fun validateInput(name: String, description: String): Boolean {
    return name.isNotBlank() && description.isNotBlank()
}

@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    task: Task? = null,
    onSave: (Task) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    // Zmienne przechowujące stan formularza
    var name by remember { mutableStateOf(task?.name ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var isImportant by remember { mutableStateOf(task?.isImportant == true) }
    var validationError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextFieldWithClearAndError(
            value = name,
            onValueChange = { name = it },
            label = stringResource(R.string.task_name),
            errorMessage = stringResource(R.string.error_empty),
            isError = validationError && name.isBlank(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextFieldWithClearAndError(
            value = description,
            onValueChange = { description = it },
            label = stringResource(R.string.task_desc),
            errorMessage = stringResource(R.string.error_empty),
            isError = validationError && description.isBlank(),
            singleLine = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        SwitchWithText(
            text = stringResource(R.string.important_label),
            checked = isImportant,
            onCheckedChange = { isImportant = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onCancel() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.cancel))
            }

            Button(
                onClick = {
                    validationError = !validateInput(name, description)
                    if (!validationError) {
                        onSave(
                            Task(
                                name = name,
                                description = description,
                                isImportant = isImportant,
                                isCompleted = task?.isCompleted ?: false,
                                taskId = task?.taskId ?: System.currentTimeMillis()
                            )
                        )
                    }
                }
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}
