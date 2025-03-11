package com.gondroid.noteai.presentation.screens.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.gondroid.noteai.presentation.screens.components.NoteItem
import com.gondroid.noteai.presentation.screens.notes.providers.NoteScreenPreviewProvider
import com.gondroid.noteai.ui.theme.NoteAppTheme

@Composable
fun NotesScreenRoot(
    viewModel: NoteScreenViewModel,
    navigateTo: (String?) -> Unit,
) {
    val state = viewModel.state
    val event = viewModel.events

    NotesScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is NoteScreenAction.OnAddNote -> navigateTo(null)
                is NoteScreenAction.OnClickNote -> navigateTo(action.noteId)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    state: NoteDataState,
    onAction: (NoteScreenAction) -> Unit,
) {
    var isMenuExtended by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notes.AI",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAction(NoteScreenAction.OnAddNote)
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                },
            )
        },
    ) { paddingValues ->

        LazyVerticalStaggeredGrid(
            modifier =
            Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(
                items = state.notes,
                key = { note -> note.id }
            ) { note ->
                NoteItem(
                    modifier = Modifier,
                    onItemSelected = {
                        onAction(NoteScreenAction.OnClickNote(noteId = note.id))
                    },
                    note = note,
                )
            }
        }
    }
}

@Preview
@Composable
fun TaskScreenPreviewLight(
    @PreviewParameter(NoteScreenPreviewProvider::class) state: NoteDataState,
) {
    NoteAppTheme {
        NotesScreen(
            state = state,
            onAction = {
            },
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun TaskScreenPreviewDark(
    @PreviewParameter(NoteScreenPreviewProvider::class) state: NoteDataState,
) {
    NoteAppTheme {
        NotesScreen(
            state = state,
            onAction = {
            },
        )
    }
}
