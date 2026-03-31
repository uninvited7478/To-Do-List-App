@file:Suppress("FunctionName")
package com.example.todolistapp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todolistapp.data.DataSource
import com.example.todolistapp.data.Task
import com.example.todolistapp.R
import com.example.todolistapp.data.ToDoRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    task: Task,
    onEditClick: () -> Unit,
    onDelete: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle = if (expanded) 180f else 0f

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = { expanded = !expanded },
                onLongClick = onDelete
            ),
        color = if (task.isCompleted) Color.LightGray else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onCheckedChange(it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = task.name,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )
                if (task.isImportant) {
                    Text("⚠️", modifier = Modifier.padding(horizontal = 8.dp))
                }
                IconButton(onClick = onEditClick) {
                    Icon(Icons.TwoTone.Edit, contentDescription = "Edit Task")
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotationAngle)
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 48.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToDoListScreen(
    repository: ToDoRepository,
    modifier: Modifier = Modifier,
    onEdit: (Task) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val tasks by repository.tasksFlow.collectAsState(initial = emptyList())

    var showDeleteDialog by remember { mutableStateOf(false) }
    var taskToDelete: Task? by remember { mutableStateOf(null) }

    Surface(modifier = modifier.padding(8.dp)) {
        Column {
            LazyColumn(modifier = Modifier.padding(bottom = 8.dp)) {
                items(items = tasks) { task ->
                    TaskItem(
                        task = task,
                        onEditClick = { onEdit(task) },
                        onDelete = {
                            taskToDelete = task
                            showDeleteDialog = true
                        },
                        onCheckedChange = { isChecked ->
                            coroutineScope.launch {
                                repository.updateTask(task.copy(isCompleted = isChecked))
                            }
                        }
                    )
                }
            }

            if (showDeleteDialog && taskToDelete != null) {
                DeleteConfirmationDialog(
                    task = taskToDelete!!,
                    onConfirm = {
                        coroutineScope.launch {
                            repository.deleteTask(taskToDelete!!)
                            taskToDelete = null
                            showDeleteDialog = false
                        }
                    },
                    onDismiss = {
                        taskToDelete = null
                        showDeleteDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    task: Task,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_task_title)) },
        text = { Text(stringResource(R.string.delete_msg, task.name)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.delete), color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}