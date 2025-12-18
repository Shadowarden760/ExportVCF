package com.homeapps.exportvcf.ui.features.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.VerticalFloatingToolbar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.homeapps.exportvcf.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DefaultToolBar(
    expanded: Boolean,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier,
    exportEnabled: Boolean = true,
    onExport: () -> Unit = { println("onExport") },
    shareEnabled: Boolean = true,
    onShare: () -> Unit = { println("onShare") },
    resetEnabled: Boolean = true,
    onReset: () -> Unit = { println("onReset") },
    onSettings: () -> Unit = { println("onSettings") },
) {
    VerticalFloatingToolbar(
        expanded = expanded,
        colors = FloatingToolbarDefaults.standardFloatingToolbarColors(),
        floatingActionButton = {
            FloatingActionButton(onClick = onExpandClick) {
                Icon(
                    imageVector = if(expanded) {
                        ImageVector.vectorResource(R.drawable.ic_decrease)
                    } else {
                        ImageVector.vectorResource(R.drawable.ic_expand)
                    },
                    contentDescription = null
                )
            }
        },
        modifier = modifier
    ) {
        listOf(
            MenuItem(icon = R.drawable.ic_save, operation = onExport, enabled = exportEnabled),
            MenuItem(icon = R.drawable.ic_share, operation = onShare, enabled = shareEnabled),
            MenuItem(icon = R.drawable.ic_delete, operation = onReset, enabled = resetEnabled),
            MenuItem(icon = R.drawable.ic_settings, operation = onSettings, enabled = true)
        ).forEach { item ->
            IconButton(
                onClick = item.operation,
                enabled = item.enabled
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(item.icon),
                    contentDescription = null
                )
            }
        }
    }
}

private data class MenuItem(
    @DrawableRes val icon: Int,
    val operation: () -> Unit,
    val enabled: Boolean
)