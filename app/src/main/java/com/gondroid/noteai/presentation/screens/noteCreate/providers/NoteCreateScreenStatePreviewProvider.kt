package com.gondroid.noteai.presentation.screens.noteCreate.providers

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.gondroid.noteai.domain.Category
import com.gondroid.noteai.presentation.screens.noteCreate.NoteCreateScreenState

class NoteCreateScreenStatePreviewProvider : PreviewParameterProvider<NoteCreateScreenState> {
    override val values: Sequence<NoteCreateScreenState>
        get() =
            sequenceOf(
                NoteCreateScreenState(
                    title = TextFieldState("Note 1"),
                    content = TextFieldState("Description 1"),
                    category = Category.WORK.name
                ),
                NoteCreateScreenState(
                    title = TextFieldState("Note 2"),
                    content = TextFieldState("Description 2"),
                    category = Category.WORK.name
                )
            )
}
