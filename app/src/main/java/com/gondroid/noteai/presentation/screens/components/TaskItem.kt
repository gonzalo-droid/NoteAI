package com.gondroid.noteai.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gondroid.noteai.domain.Category
import com.gondroid.noteai.domain.Task
import com.gondroid.noteai.presentation.screens.task.ActionOnSelected

@Composable
@Preview(showBackground = true)
fun TaskItemPreview() {
    MaterialTheme {
        TaskItem(
            modifier = Modifier,
            onItemSelected = { action, id ->
                when (action) {
                    ActionOnSelected.DONE -> {
                    }

                    ActionOnSelected.DELETE -> {
                    }

                    else -> {
                    }
                }
            },
            onToggleCompletion = {},
            task =
            Task(
                id = "",
                noteId = "",
                title = "Task 1",
                description = "Description 1",
                isCompleted = false,
                category = Category.SHOPPING
            )
        )
    }
}

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    onItemSelected: (ActionOnSelected, String) -> Unit,
    onToggleCompletion: (Task) -> Unit,
    task: Task
) {
    Row(
        modifier =
        modifier
            .clickable {
                onItemSelected(ActionOnSelected.DONE, task.id)
            }.background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = {
                onToggleCompletion(task)
            }
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier =
            Modifier
                .padding(8.dp)
                .weight(1f)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleSmall,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis, // Hol..
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            if (!task.isCompleted) {
                task.description?.let {
                    Text(
                        text = it,
                        maxLines = 2,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis, // Hol..
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                task.category?.let {
                    Text(
                        text = it.toString(),
                        maxLines = 2,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis, // Hol..
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Box {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier =
                Modifier
                    .padding(8.dp)
                    .clickable {
                        onItemSelected(ActionOnSelected.DELETE, task.id)
                    }
            )
        }
    }
}
