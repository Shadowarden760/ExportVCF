package com.homeapps.exportvcf.ui.features.main

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.homeapps.exportvcf.domain.vcard.VCardManager
import com.homeapps.exportvcf.utils.PermissionManager

@Composable
fun MainScreen(modifier: Modifier) {
    val contentResolver = LocalContext.current.contentResolver
    val permissionManager = PermissionManager(appContext = LocalContext.current)
    val VCardManager = VCardManager()
    val contacts = remember { mutableStateOf(listOf<String>()) }
    val hasContactPermission = remember { mutableStateOf(permissionManager.checkPermission(Manifest.permission.READ_CONTACTS)) }
    val contactPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasContactPermission.value = isGranted
    }

    LaunchedEffect(null) {
        if (!hasContactPermission.value) {
            contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        } else {
            contacts.value = VCardManager.loadContacts(contentResolver = contentResolver)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = "Contact Permission: (${hasContactPermission.value})",
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = contacts.value.toString(),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
    }
}