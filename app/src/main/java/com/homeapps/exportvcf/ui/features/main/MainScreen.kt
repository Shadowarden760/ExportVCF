package com.homeapps.exportvcf.ui.features.main

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.homeapps.exportvcf.BuildConfig
import com.homeapps.exportvcf.R
import com.homeapps.exportvcf.domain.vcard.VCardManager
import com.homeapps.exportvcf.ui.MainActivity
import com.homeapps.exportvcf.ui.features.components.DefaultDialog
import com.homeapps.exportvcf.ui.features.components.DefaultToolBar
import com.homeapps.exportvcf.utils.PermissionManager
import kotlin.system.exitProcess

@Composable
fun MainScreen(modifier: Modifier) {
    val contentResolver = LocalContext.current.contentResolver
    val permissionManager = PermissionManager(appContext = LocalContext.current)
    val VCardManager = VCardManager()
    val permissionTries = remember { mutableStateOf(0) }
    val openPermissionDialog = remember { mutableStateOf(false) }
    val contacts = remember { mutableStateOf(listOf<String>()) }
    val hasContactPermission = remember { mutableStateOf(permissionManager.checkPermission(permission = Manifest.permission.READ_CONTACTS)) }
    val contactPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasContactPermission.value = isGranted
        permissionTries.value = permissionTries.value.inc()
        if (!isGranted) {
            openPermissionDialog.value = true
        }
    }

    LaunchedEffect(null) {
        if (!hasContactPermission.value) {
            contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        DefaultToolBar(modifier = modifier.align(Alignment.BottomEnd).padding(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Contact Permission: (${hasContactPermission.value})",
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            OutlinedIconButton(
                enabled = hasContactPermission.value,
                shape = CircleShape,
                colors = IconButtonDefaults.outlinedIconButtonVibrantColors(),
                border = BorderStroke(width = 4.dp, color = MaterialTheme.colorScheme.outline),
                onClick = {
                    println(VCardManager.loadContacts(contentResolver = contentResolver))
                },
                modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_save),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(fraction = 0.5F)
                )
            }
            Spacer(modifier = Modifier.weight(2f))
            Text(
                text = "${stringResource(R.string.main_text_version)} ${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.main_text_creator),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }
    }

    if (openPermissionDialog.value) {
        DefaultDialog(
            onDismissRequest = {
                openPermissionDialog.value = false
                MainActivity().finishAffinity()
                exitProcess(0)
            },
            onConfirmation = {
                openPermissionDialog.value = false
                if (permissionTries.value < 2) {
                    contactPermissionLauncher.launch(input = Manifest.permission.READ_CONTACTS)
                } else {
                    MainActivity().finishAffinity()
                    exitProcess(0)
                }
            },
            dialogTitle = "Permission not granted",
            dialogText = "Application needs contact permission to compose VCF file.",
            dismissText = if (permissionTries.value < 2) "Close app" else null,
            confirmText = if (permissionTries.value < 2) "Add permission" else "Close app",
            icon = Icons.Default.Info
        )
    }
}

