package com.homeapps.exportvcf.ui.features.main

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bilalazzam.contacts_provider.rememberContactsProvider
import com.homeapps.exportvcf.BuildConfig
import com.homeapps.exportvcf.R
import com.homeapps.exportvcf.ui.MainActivity
import com.homeapps.exportvcf.ui.features.components.DefaultToolBar
import com.homeapps.exportvcf.ui.features.main.components.ContactCards
import com.homeapps.exportvcf.ui.features.main.components.ContactsPermissionDialog
import com.homeapps.exportvcf.utils.PermissionManager
import io.github.themeanimator.ThemeAnimationState
import io.github.themeanimator.button.ThemeSwitchButton
import io.github.themeanimator.button.ThemeSwitchIcon
import io.github.themeanimator.rememberThemeAnimationState
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@Composable
fun MainScreen(
    animationState: ThemeAnimationState,
    modifier: Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val permissionManager = PermissionManager(appContext = LocalContext.current)
    val viewModel = remember { MainViewModel() }
    var expanded by remember { mutableStateOf(false) }
    val contactsProvider = rememberContactsProvider()
    val contacts = viewModel.contacts.collectAsStateWithLifecycle()
    var openPermissionDialog by remember { mutableStateOf(false) }
    var hasContactPermission by remember { mutableStateOf(permissionManager.checkPermission(permission = Manifest.permission.READ_CONTACTS)) }
    val fileSaverLauncher = rememberFileSaverLauncher(dialogSettings = FileKitDialogSettings()) { file ->
        if (file != null) {
            val result = runCatching {
                scope.launch {
                    val cardBytes = viewModel.createVCFCards()
                    file.write(cardBytes)
                }
            }
            when(result.isSuccess) {
                true -> Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                false -> Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Operation was canceled", Toast.LENGTH_SHORT).show()
        }
    }
    val contactPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasContactPermission = isGranted
        if (!isGranted) {
            openPermissionDialog = true
        }
    }

    LaunchedEffect(null) {
        if (!hasContactPermission) {
            contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(end = 16.dp)
            ) {
                Spacer(modifier = Modifier.weight(1.5F))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1F))
                ThemeSwitchButton(
                    buttonIcon = ThemeSwitchIcon.DuoVector(
                        darkVector = ImageVector.vectorResource(R.drawable.ic_dark_mode),
                        lightVector = ImageVector.vectorResource(R.drawable.ic_light_mode)
                    ),
                    animationState = animationState,
                    modifier = Modifier.weight(0.5f)
                )
            }
            if (contacts.value.isEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                OutlinedIconButton(
                    enabled = hasContactPermission,
                    shape = CircleShape,
                    colors = IconButtonDefaults.outlinedIconButtonVibrantColors(),
                    border = BorderStroke(width = 4.dp, color = MaterialTheme.colorScheme.outline),
                    onClick = { viewModel.getContacts(contactsProvider = contactsProvider) },
                    modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_import_contacts),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(fraction = 0.5F)
                    )
                }
            } else {
                Card(modifier = Modifier.align(Alignment.End).padding(end = 16.dp)) {
                    Text(
                        text = "Found ${contacts.value.size} accounts",
                        modifier = Modifier.padding(8.dp)
                    )
                }
                ContactCards(
                    contacts = contacts.value,
                    modifier = Modifier.weight(20F).padding(16.dp)
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
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )
        }
        DefaultToolBar(
            expanded = expanded,
            onExpandClick = { expanded = !expanded },
            exportEnabled = contacts.value.isNotEmpty(),
            onExport = {
                fileSaverLauncher.launch(
                    suggestedName = "saved_contacts_${System.currentTimeMillis()}",
                    extension = "vcf"
                )
                expanded = false
            },
            shareEnabled = contacts.value.isNotEmpty(),
            resetEnabled = contacts.value.isNotEmpty(),
            onReset = {
                viewModel.resetContacts()
                expanded = false
            },
            modifier = modifier.align(Alignment.BottomEnd).padding(16.dp)
        )
    }

    if (openPermissionDialog) {
        ContactsPermissionDialog(
            onDismiss = {
                openPermissionDialog = false
                MainActivity().finishAffinity()
                exitProcess(0)
            },
            onConfirm = {
                openPermissionDialog = false
                contactPermissionLauncher.launch(input = Manifest.permission.READ_CONTACTS)
            }
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen(
        animationState = rememberThemeAnimationState(),
        modifier = Modifier
    )
}