package com.example.opencalltracker.ui.history

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.opencalltracker.R
import com.example.opencalltracker.domain.model.HandledCall
import com.example.opencalltracker.domain.model.HandledCallOutcome
import com.example.opencalltracker.ui.history.components.EmptyHistory
import com.example.opencalltracker.ui.history.components.HandledCallRow
import com.example.opencalltracker.ui.theme.OpenCallTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenContent(uiState: HistoryUiState, onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.history_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.history_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.handledCalls.isEmpty()) {
            if (!uiState.isLoading) {
                EmptyHistory(modifier = Modifier.padding(innerPadding))
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = ListHorizontalPadding)
        ) {
            items(items = uiState.handledCalls, key = { handledCall -> handledCall.id }) { handledCall ->
                HandledCallRow(handledCall = handledCall)
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = DIVIDER_ALPHA))
            }
        }
    }
}

private val ListHorizontalPadding = 24.dp
private const val DIVIDER_ALPHA = 0.3f

@Preview(name = "With calls")
@Composable
private fun HistoryScreenContentPreview() {
    OpenCallTrackerTheme {
        HistoryScreenContent(
            uiState = HistoryUiState(
                handledCalls = listOf(
                    HandledCall(id = 1L, phoneNumber = "0612345678", handledAt = 1_795_418_000_000L, outcome = HandledCallOutcome.COMPLETED),
                    HandledCall(id = 2L, phoneNumber = null, handledAt = 1_795_410_000_000L, outcome = HandledCallOutcome.FAILED)
                ),
                isLoading = false
            ),
            onBackClick = {}
        )
    }
}

@Preview(name = "Empty")
@Composable
private fun HistoryScreenContentEmptyPreview() {
    OpenCallTrackerTheme(darkTheme = true) {
        HistoryScreenContent(uiState = HistoryUiState(isLoading = false), onBackClick = {})
    }
}
