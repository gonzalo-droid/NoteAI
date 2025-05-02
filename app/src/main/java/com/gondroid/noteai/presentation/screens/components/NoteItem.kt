package com.gondroid.noteai.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gondroid.noteai.domain.Category
import com.gondroid.noteai.domain.Note
import com.gondroid.noteai.ui.theme.NoteAppTheme
import kotlin.random.Random

@Composable
@Preview(showBackground = true)
fun NoteItemPreview() {
    NoteAppTheme {
        NoteItem(
            modifier = Modifier,
            onItemSelected = {
            },
            note =
            Note(
                id = "1",
                title = "Project Deadline",
                content = "Submit project report by Friday. Submit project report by Friday. Submit project report by Friday. Submit project report by Friday. Submit project report by Friday. Submit project report by Friday. Submit project report by Friday. Submit project report by Friday. Submit project report by Friday.",
                category = Category.WORK.toString()
            )
        )
    }
}

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
    note: Note
) {
    val randomHeight =
        remember {
            Random.nextInt(100, 300)
        }

    val maxLines =
        when {
            randomHeight <= 120 -> 1
            randomHeight <= 150 -> 2
            randomHeight <= 200 -> 4
            else -> 7
        }

    val randomColor = remember { randomColor() }
    val darkenedColor = remember { darkenColor(randomColor, 0.1f) } // Reduce brillo en 30%

    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(randomHeight.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(randomColor)
            .padding(16.dp)
            .clickable {
                onItemSelected(note.id)
            }
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier =
            Modifier
                .fillMaxWidth()
                .height(randomHeight.dp)
                .clickable {
                    onItemSelected(note.id)
                }
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis, // Hol..
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            note.content?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(Modifier.weight(1f))
        }

        note.category?.let { it ->

            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                color = darkenedColor,
                textAlign = TextAlign.Center,
                modifier =
                Modifier
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

fun darkenColor(
    color: Color,
    factor: Float
): Color =
    Color(
        red = (color.red * factor).coerceIn(0f, 1f),
        green = (color.green * factor).coerceIn(0f, 1f),
        blue = (color.blue * factor).coerceIn(0f, 1f),
        alpha = color.alpha
    )

fun randomColor(): Color =
    Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 0.2f
    )
