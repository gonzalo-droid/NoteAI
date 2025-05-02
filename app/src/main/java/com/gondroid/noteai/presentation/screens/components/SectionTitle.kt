package com.gondroid.noteai.presentation.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gondroid.noteai.ui.theme.NoteAppTheme

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Box {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier =
            modifier
                .padding(8.dp)
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO
)
fun SectionTitleLight() {
    NoteAppTheme {
        SectionTitle(
            title = "Section Title"
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
fun SectionTitleDark() {
    NoteAppTheme {
        SectionTitle(
            title = "Section Title"
        )
    }
}
