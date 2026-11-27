package com.example.opencalltracker.ui.main.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.opencalltracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(onHistoryClick: () -> Unit, onSettingsClick: () -> Unit, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name), style = MaterialTheme.typography.titleMedium)
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_tune),
                    contentDescription = stringResource(R.string.main_settings_action)
                )
            }
            IconButton(onClick = onHistoryClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_history),
                    contentDescription = stringResource(R.string.main_history_action)
                )
            }
        },
        modifier = modifier
    )
}
