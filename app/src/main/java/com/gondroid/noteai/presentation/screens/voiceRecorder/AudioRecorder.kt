package com.gondroid.noteai.presentation.screens.voiceRecorder

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.absoluteValue

class AudioRecorder {
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: String = ""

    private val sampleRate = 44100
    private val bufferSize =
        AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

    private var audioRecord: AudioRecord? = null

    private val _amplitudes = MutableStateFlow<List<Float>>(emptyList())
    val amplitudes: StateFlow<List<Float>> = _amplitudes.asStateFlow()

    private var recordingJob: Job? = null
    private var isPaused = false

    fun startRecording(): String? {
        try {
            if (mediaRecorder != null) return null // Already recording

            val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
            if (!directory.exists()) directory.mkdirs()
            outputFile = "${directory.absolutePath}/voice_note_${System.currentTimeMillis()}.mp3"

            mediaRecorder =
                MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setOutputFile(outputFile)

                    prepare()
                    start()
                }
            startAmplitudeTracking()
            isPaused = false
            return outputFile
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error startRecording", e)
            return null
        }
    }

    fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null

            recordingJob?.cancel()
            audioRecord?.apply {
                if (recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    stop()
                }
                release()
            }
            audioRecord = null
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error in stopRecording", e)
        }
    }

    fun pauseRecording() {
        try {
            if (mediaRecorder != null && !isPaused) {
                mediaRecorder?.pause()
                isPaused = true
            }
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error in pauseRecording", e)
        }
    }

    fun resumeRecording() {
        try {
            if (mediaRecorder != null && isPaused) {
                mediaRecorder?.resume()
                isPaused = false
            }
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error resumeRecording", e)
        }
    }

    fun cancelRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                reset()
                release()
            }
            mediaRecorder = null

            val file = File(outputFile)
            if (file.exists()) {
                if (file.delete()) {
                    Log.d("AudioRecorder", "File deleted: $outputFile")
                } else {
                    Log.e("AudioRecorder", "Failed to delete file: $outputFile")
                }
            } else {
                Log.d("AudioRecorder", "File not found: $outputFile")
            }
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error in cancelRecording", e)
        }
    }

    fun getAmplitudeFlow(): StateFlow<List<Float>> = amplitudes

    @SuppressLint("MissingPermission")
    private fun startAmplitudeTracking() {
        audioRecord =
            AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

        audioRecord?.startRecording()

        recordingJob =
            CoroutineScope(Dispatchers.IO).launch {
                val buffer = ShortArray(bufferSize)
                while (audioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    if (!isPaused) {
                        val readSize = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                        if (readSize > 0) {
                            val amplitude =
                                buffer
                                    .take(readSize)
                                    .map { it.toFloat().absoluteValue / Short.MAX_VALUE }
                                    .average()
                                    .toFloat()

                            withContext(Dispatchers.Main) {
                                _amplitudes.value = (_amplitudes.value + amplitude).takeLast(50)
                            }
                        }
                    }
                }
            }
    }
}
