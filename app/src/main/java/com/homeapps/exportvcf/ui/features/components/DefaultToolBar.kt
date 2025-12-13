package com.homeapps.exportvcf.ui.features.components

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.VerticalFloatingToolbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.homeapps.exportvcf.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DefaultToolBar(
    modifier: Modifier = Modifier,
    onExport: () -> Unit = { println("onExport") },
    onShare: () -> Unit = { println("onShare") },
    onDelete: () -> Unit = { println("onDelete") },
    onSettings: () -> Unit = { println("onSettings") },
) {
    val expanded = remember { mutableStateOf(false) }
    VerticalFloatingToolbar(
        expanded = expanded.value,
        colors = FloatingToolbarDefaults.standardFloatingToolbarColors(),
        floatingActionButton = {
            FloatingActionButton(onClick = { expanded.value = !expanded.value }) {
                Icon(
                    imageVector = if(expanded.value) {
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
            Pair(R.drawable.ic_save, onExport),
            Pair(R.drawable.ic_share, onShare),
            Pair(R.drawable.ic_delete, onDelete),
            Pair(R.drawable.ic_settings, onSettings)
        ).forEach { item ->
            IconButton(onClick = item.second) {
                Icon(
                    imageVector = ImageVector.vectorResource(item.first),
                    contentDescription = null
                )
            }
        }
    }
}