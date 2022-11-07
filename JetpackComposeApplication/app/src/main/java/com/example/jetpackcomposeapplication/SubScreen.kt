package com.example.jetpackcomposeapplication

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposeapplication.ui.theme.JetpackComposeApplicationTheme

@Composable
fun SubScreen (appContainer: AppContainer) {
    ComposeSubScreen()
}

@Composable
fun ComposeSubScreen() {
    JetpackComposeApplicationTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable{ },
            elevation = 10.dp
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text("SubScreen")
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun SubScreenPreview() {
    ComposeSubScreen()
}