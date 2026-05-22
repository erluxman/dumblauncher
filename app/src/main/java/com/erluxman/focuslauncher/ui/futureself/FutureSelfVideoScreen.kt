package com.erluxman.focuslauncher.ui.futureself

import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.camera.core.Preview as CamPreview
import java.io.File

private const val VIDEO_FILE_NAME = "future_self.mp4"

fun futureSelfVideoFile(context: Context): File =
    File(context.filesDir, VIDEO_FILE_NAME)

fun hasFutureSelfVideo(context: Context): Boolean =
    futureSelfVideoFile(context).exists() && futureSelfVideoFile(context).length() > 0

@Composable
fun FutureSelfVideoScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val hasCam = remember {
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
    }
    val hasMic = remember {
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) ==
            PackageManager.PERMISSION_GRANTED
    }
    var isRecording by remember { mutableStateOf(false) }
    var elapsedSec by remember { mutableStateOf(0) }
    val recording = remember { mutableStateOf<Recording?>(null) }
    val videoCapture = remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    val outFile = remember { futureSelfVideoFile(context) }
    val previewView = remember { PreviewView(context) }

    Surface(
        modifier = Modifier.fillMaxSize().testTag("future-self-video"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp)
        ) {
            Spacer(Modifier.height(40.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    "FUTURE SELF",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    letterSpacing = 2.sp
                )
            }
            Spacer(Modifier.height(16.dp))
            if (!hasCam || !hasMic) {
                Text(
                    "Grant CAMERA and RECORD_AUDIO permissions in system settings to record a future-self video.",
                    style = MaterialTheme.typography.bodyMedium
                )
                return@Column
            }
            Text(
                "Record a 30-60 second video to your future self. It plays full-length during any uninstall attempt.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth().height(360.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize()
                )
            }

            DisposableEffect(lifecycleOwner) {
                val providerFuture = ProcessCameraProvider.getInstance(context)
                providerFuture.addListener({
                    runCatching {
                        val provider = providerFuture.get()
                        val preview = CamPreview.Builder().build()
                        preview.setSurfaceProvider(previewView.surfaceProvider)
                        val recorder = Recorder.Builder()
                            .setQualitySelector(QualitySelector.from(Quality.HD))
                            .build()
                        val capture = VideoCapture.withOutput(recorder)
                        videoCapture.value = capture
                        provider.unbindAll()
                        provider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_FRONT_CAMERA,
                            preview, capture
                        )
                    }
                }, ContextCompat.getMainExecutor(context))
                onDispose { /* lifecycle aware */ }
            }

            LaunchedEffect(isRecording) {
                while (isRecording) {
                    kotlinx.coroutines.delay(1000)
                    elapsedSec++
                    if (elapsedSec >= 60) {
                        recording.value?.stop()
                        isRecording = false
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = if (isRecording) "Recording: ${elapsedSec}s" else
                    if (outFile.exists()) "Saved (${outFile.length() / 1024} KB)" else "Ready",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            if (!isRecording) {
                Button(
                    onClick = {
                        val capture = videoCapture.value ?: return@Button
                        if (outFile.exists()) outFile.delete()
                        val output = FileOutputOptions.Builder(outFile).build()
                        val pending = capture.output
                            .prepareRecording(context, output)
                        val rec = if (hasMic) pending.withAudioEnabled() else pending
                        elapsedSec = 0
                        recording.value = rec.start(
                            ContextCompat.getMainExecutor(context)
                        ) { event ->
                            if (event is VideoRecordEvent.Finalize) {
                                isRecording = false
                            }
                        }
                        isRecording = true
                    },
                    modifier = Modifier.fillMaxWidth().testTag("future-self-record")
                ) { Text(if (outFile.exists()) "Re-record" else "Start recording") }
            } else {
                Button(
                    onClick = {
                        recording.value?.stop()
                        isRecording = false
                    },
                    modifier = Modifier.fillMaxWidth().testTag("future-self-stop")
                ) { Text("Stop") }
            }
        }
    }
}

@Composable
fun FutureSelfVideoPlayer(
    file: File,
    onCompleted: () -> Unit
) {
    val context = LocalContext.current
    var completed by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxSize().testTag("future-self-video-player"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Spacer(Modifier.height(40.dp))
            Text(
                "FUTURE YOU IS TALKING",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(16.dp))
            AndroidView(
                factory = { ctx ->
                    val sv = SurfaceView(ctx)
                    sv.holder.addCallback(object : SurfaceHolder.Callback {
                        var mp: MediaPlayer? = null
                        override fun surfaceCreated(holder: SurfaceHolder) {
                            runCatching {
                                mp = MediaPlayer().apply {
                                    setDataSource(file.absolutePath)
                                    setDisplay(holder)
                                    setOnCompletionListener {
                                        completed = true
                                        onCompleted()
                                    }
                                    prepare()
                                    start()
                                }
                            }
                        }
                        override fun surfaceChanged(h: SurfaceHolder, f: Int, w: Int, ht: Int) = Unit
                        override fun surfaceDestroyed(holder: SurfaceHolder) {
                            mp?.release()
                            mp = null
                        }
                    })
                    sv
                },
                modifier = Modifier.fillMaxWidth().weight(1f).testTag("future-self-video-surface")
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "You can't skip this — that was the deal you made.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
