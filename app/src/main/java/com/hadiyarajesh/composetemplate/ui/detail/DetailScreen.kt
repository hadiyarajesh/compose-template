package com.hadiyarajesh.composetemplate.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.ui.components.VerticalSpacer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    source: String,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                title = { Text(text = stringResource(R.string.detail_screen)) })
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
        Text(text = stringResource(R.string.you_are_coming_from, source))
        VerticalSpacer(size = 16)
        Button(onClick = { onBackClick() }) {
            Text(text = stringResource(R.string.go_back))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        source = stringResource(R.string.home_screen),
        onBackClick = {}
    )
}
