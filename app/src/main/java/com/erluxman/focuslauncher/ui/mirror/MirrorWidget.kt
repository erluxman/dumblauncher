package com.erluxman.focuslauncher.ui.mirror

import android.content.pm.PackageManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

/**
 * PSYCH-005 Mirror Widget.
 *
 * Renders the front camera as a small live tile so the user sees their face
 * every time they open the launcher. Gracefully renders a placeholder if
 * camera permission is missing or no front camera exists.
 */
@Composable
fun MirrorWidget(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val hasPermission = remember {
        ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    Column(modifier = modifier.testTag("mirror-widget")) {
        Text(
            "MIRROR",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().height(180.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            if (!hasPermission) {
                Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "Grant CAMERA in system settings to see yourself here.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                val previewView = remember {
                    PreviewView(context).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }
                }
                DisposableEffect(lifecycleOwner) {
                    val providerFuture = ProcessCameraProvider.getInstance(context)
                    var bound: ProcessCameraProvider? = null
                    providerFuture.addListener({
                        runCatching {
                            val provider = providerFuture.get()
                            bound = provider
                            val preview = Preview.Builder().build()
                            preview.setSurfaceProvider(previewView.surfaceProvider)
                            provider.unbindAll()
                            provider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_FRONT_CAMERA,
                                preview
                            )
                        }
                    }, ContextCompat.getMainExecutor(context))

                    onDispose {
                        bound?.unbindAll()
                    }
                }
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }
}
