package com.hadiyarajesh.composetemplate.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.navigation.TopLevelDestination
import com.hadiyarajesh.composetemplate.ui.components.IconPositionInText
import com.hadiyarajesh.composetemplate.ui.components.TextWithIcon
import com.hadiyarajesh.composetemplate.ui.components.VerticalSpacer

@Composable
internal fun DetailRoute(
    source: String,
    onBackClick: () -> Unit
) {
    DetailScreen(
        source = source,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailScreen(
    source: String,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(
                            R.string.screen_name,
                            TopLevelDestination.Detail.title
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            DetailScreenContent(
                modifier = Modifier.fillMaxSize(),
                source = source,
                onBackClick = onBackClick
            )
        }
    }
}

@Composable
private fun DetailScreenContent(
    modifier: Modifier = Modifier,
    source: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.you_are_coming_from, source),
            style = MaterialTheme.typography.titleSmall
        )
        VerticalSpacer(size = 16)

        OutlinedButton(onClick = { onBackClick() }) {
            TextWithIcon(
                text = stringResource(R.string.go_back),
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                position = IconPositionInText.Leading
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        source = stringResource(R.string.screen_name).format(TopLevelDestination.Home.title),
        onBackClick = {}
    )
}
