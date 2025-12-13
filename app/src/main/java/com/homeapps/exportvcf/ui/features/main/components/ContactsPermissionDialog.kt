package com.homeapps.exportvcf.ui.features.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import com.homeapps.exportvcf.ui.features.components.DefaultDialog

@Composable
fun ContactsPermissionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    DefaultDialog(
        onDismissRequest = onDismiss,
        onConfirmation = onConfirm,
        dialogTitle = "Permission not granted",
        dialogText = "Application needs contact permission to compose VCF file.",
        dismissText = "Close app",
        confirmText = "Add permission",
        icon = Icons.Default.Info
    )
}