package com.gondroid.noteai.presentation.screens.voiceRecorder

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun RequestAudioPermission(onPermissionGranted: (Boolean) -> Unit) {
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                if (granted) {
                    onPermissionGranted(true)
                } else {
                    onPermissionGranted(false)
                    Log.d("AudioRecorder", "Permiso de audio denegado")
                }
            }
        )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
}
