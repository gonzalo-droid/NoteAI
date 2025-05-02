package com.gondroid.noteai.presentation.screens.voiceRecorder

import android.util.Log
import com.gondroid.noteai.BuildConfig
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class WhisperTranscriber {
    private val apiKey = BuildConfig.API_KEY_OPENAI
    private val client = OkHttpClient()

    fun transcribeAudio(
        filePath: String,
        callback: (String?) -> Unit
    ) {
        val file = File(filePath)
        val requestBody =
            MultipartBody
                .Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.name, file.asRequestBody("audio/mpeg".toMediaTypeOrNull()))
                .addFormDataPart("model", "whisper-1")
                .build()

        val request =
            Request
                .Builder()
                .url("https://api.openai.com/v1/audio/transcriptions")
                .addHeader("Authorization", "Bearer $apiKey")
                .post(requestBody)
                .build()
        Log.d("WhisperTranscriber", request.toString())

        client.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(
                    call: Call,
                    e: IOException
                ) {
                    Log.e("WhisperTranscriber", "Error: ${e.message}")
                    callback(null)
                }

                override fun onResponse(
                    call: Call,
                    response: Response
                ) {
                    response.body?.string()?.let { responseBody ->
                        val jsonObject = JSONObject(responseBody)
                        Log.e("WhisperTranscriber", jsonObject.toString())

                        val text = jsonObject.optString("text", "Error en la transcripción")
                        callback(text)
                    } ?: callback(null)
                }
            }
        )
    }
}
