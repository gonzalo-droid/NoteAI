package com.gondroid.noteai.presentation.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateUtil {
    companion object {
        fun formatDateTime(date: Long): String {
            val localDateTime =
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(date),
                    ZoneId.systemDefault()
                )

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yy hh:mm a")
            return localDateTime.format(formatter)
        }

        fun formatDateTime(localDateTime: LocalDateTime): String {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yy hh:mm a")
            return localDateTime.format(formatter)
        }
    }
}
