package com.gondroid.noteai.presentation.screens.notes.providers

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.gondroid.noteai.domain.Category
import com.gondroid.noteai.domain.Note

class NoteItemPreviewProvider : PreviewParameterProvider<Note> {
    override val values: Sequence<Note>
        get() =
            sequenceOf(
                Note(
                    id = "1",
                    title = "Project Deadline",
                    content = "Submit project report by Friday. Submit project report by Friday. Submit project report by Friday. Submit project report by Friday.",
                    category = Category.WORK.toString()
                ),
                Note(
                    id = "2",
                    title = "Grocery List",
                    content = "Milk, eggs, bread, eggs, bread, and coffee.",
                    category = Category.PERSONAL.toString()
                ),
                Note(
                    id = "3",
                    title = "Meeting Notes",
                    content = "Discuss app redesign with the team.",
                    category = Category.WORK.toString()
                ),
                Note(
                    id = "4",
                    title = "Book Recommendation",
                    content = "Read 'Atomic Habits' by James Clear.",
                    category = Category.LEARNING.toString()
                ),
                Note(
                    id = "5",
                    title = "Workout Plan",
                    content = "Cardio on Monday, Strength on Tuesday.",
                    category = Category.HEALTH.toString()
                ),
                Note(
                    id = "6",
                    title = "Birthday Reminder",
                    content = "Buy a gift for Sarah's birthday.",
                    category = Category.PERSONAL.toString()
                ),
                Note(
                    id = "7",
                    title = "App Idea",
                    content = "A habit tracker with AI recommendations.",
                    category = Category.WORK.toString()
                ),
                Note(
                    id = "8",
                    title = "Travel Checklist",
                    content = "Passport, tickets, charger, and toiletries.",
                    category = Category.PERSONAL.toString()
                ),
                Note(
                    id = "9",
                    title = "Course Notes",
                    content = "Key takeaways from the Kotlin coroutines course.",
                    category = Category.LEARNING.toString()
                ),
                Note(
                    id = "10",
                    title = "Budget Plan",
                    content = "Track monthly expenses and savings.",
                    category = Category.FINANCE.toString()
                ),
                Note(
                    id = "11",
                    title = "Travel Checklist",
                    content = "Passport, tickets, charger, and toiletries.",
                    category = Category.PERSONAL.toString()
                ),
                Note(
                    id = "12",
                    title = "Course Notes",
                    content = "Key takeaways from the Kotlin coroutines course.",
                    category = Category.LEARNING.toString()
                ),
                Note(
                    id = "13",
                    title = "Budget Plan",
                    content = "Track monthly expenses and savings.",
                    category = Category.FINANCE.toString()
                )
            )
}
