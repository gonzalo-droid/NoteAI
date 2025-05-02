package com.gondroid.noteai.presentation.screens.components

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gondroid.noteai.R
import com.gondroid.noteai.ui.theme.NoteAppTheme

@Composable
fun SummaryInfo(
    modifier: Modifier = Modifier,
    date: String = "March 9, 2024",
    tasksSummary: String = "5 incomplete, 5 completed",
    completedTasks: Int = 5,
    totalTask: Int = 10
) {
    val angleRatio =
        remember {
            Animatable(0f)
        }

    LaunchedEffect(completedTasks, totalTask) {
        if (totalTask == 0) {
            angleRatio.animateTo(
                targetValue = 0f
            )
            return@LaunchedEffect
        }
        angleRatio.animateTo(
            targetValue = (completedTasks.toFloat() / totalTask.toFloat()),
            animationSpec =
            tween( // animation type
                durationMillis = 300
            )
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier =
            Modifier
                .weight(1.5f)
                .padding(16.dp)
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(R.string.summary, tasksSummary),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier =
            Modifier
                .padding(16.dp)
                .aspectRatio(1f)
                .weight(1f)
        ) {
            val colorBase = MaterialTheme.colorScheme.inversePrimary
            val progress = MaterialTheme.colorScheme.primary
            val strokeWidth = 16.dp

            Canvas(
                modifier = Modifier.aspectRatio(1f)
            ) {
                drawArc(
                    color = colorBase,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    size = size,
                    style =
                    Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )

                if (completedTasks <= totalTask) {
                    drawArc(
                        color = progress,
                        startAngle = 90f,
                        sweepAngle = 360f * angleRatio.value,
                        useCenter = false,
                        size = size,
                        style =
                        Stroke(
                            width = strokeWidth.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }
            }

            Text(
                text = "${(completedTasks / totalTask.toFloat()).times(100).toInt()}%",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun SummaryInfoPreviewLight() {
    NoteAppTheme {
        SummaryInfo(modifier = Modifier)
    }
}

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun SummaryInfoPreviewDark() {
    NoteAppTheme {
        SummaryInfo(modifier = Modifier)
    }
}
