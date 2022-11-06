package com.example.jetpackcomposeapplication

import android.content.res.Configuration
import android.webkit.WebView
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.jetpackcomposeapplication.ui.theme.JetpackComposeApplicationTheme

@Composable
fun MainScreen () {
    ComposeMainScreen()
}

data class ListItem(val author: String, val body: String)

@Composable
fun ComposeList(messages: List<ListItem>) {
    LazyColumn() {
        items(messages) { ComposeListItem(it) }
    }
}

@Composable
fun ComposeListItem(msg: ListItem) {
    // https://developer.android.com/jetpack/compose/tutorial?hl=ja
    Row(modifier = Modifier.padding(all = Dp(8.0f))) {
        var isExpanded by remember {
            // https://developer.android.com/jetpack/compose/state
            // mutableStateOf makes observable variable
            mutableStateOf(false)
        }

        val surfaceColor: Color by animateColorAsState(
            targetValue = if(isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface)

        val checkedState = remember { mutableStateOf(true) }

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "picture",
            modifier = Modifier
                .size(Dp(40f))
                .clip(CircleShape)
                .border(Dp(1.5f), MaterialTheme.colors.secondary, CircleShape)
        )

        Checkbox(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it }
        )

        Spacer(modifier = Modifier.width(Dp(8f)))

        // click -> change isExpanded -> schedule recompose
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = "Hello ${msg.author}!",
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            Spacer(modifier = Modifier.width(Dp(8f)))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 10.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = "${msg.body}!",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(4.dp),
                    maxLines = if(isExpanded) Int.MAX_VALUE else 1
                )
            }
        }
    }
}

@Composable
fun ComposeMainScreen() {
    JetpackComposeApplicationTheme {
        var scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        var showDialogState by remember { mutableStateOf(false) }
        var toggleState by remember { mutableStateOf(false) }

        // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary
        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = Color(0), // transparent background to see through UnityPlayer
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = { FloatingActionButton(onClick = {
                showDialogState = true
            }){ Text("OK") } },
            topBar =  {
                TopAppBar(title = { Text("Title") }, backgroundColor = MaterialTheme.colors.background)
            },
            bottomBar = {
                BottomAppBar(
                    backgroundColor = MaterialTheme.colors.surface
                ) { Text("BottomAppBar") }
            },
            drawerShape = MaterialTheme.shapes.small,
            drawerContent = {
                Column {
                    Text(text = "drawerContent")
                    // Animation
                    // https://developer.android.com/jetpack/compose/animation
                    Crossfade(targetState = toggleState) { state ->
                        when (state) {
                            true -> {
                                Button(onClick = { }) { Text("MyComposeButton") }
                            }
                            false -> {
                                Text("MyComposeButton")
                            }
                        }
                    }
                    // interop AndroidView
                    // https://developer.android.com/jetpack/compose/interop/interop-apis
                    AndroidView(factory = { ctx ->
                        android.widget.Button(ctx).apply {
                            layoutParams = android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            text = "HOGEHOGE"
                            setOnClickListener({ toggleState = !toggleState });
                        }
                    }, modifier = Modifier.padding(top = 20.dp))
                }
            },
            content = {
                Column {
                    ComposeList(
                        messages = listOf(
                            ListItem("Name", "aaaaaa"),
                            ListItem("Name", "cccc\nbbbb\ncccc")
                        )
                    )
                }
            }
        )

        // wake dialog
        if(showDialogState)
        {
            Dialog(
                onDismissRequest = { showDialogState = false },
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.primary
                ) {
                    Box(Modifier.padding(24.dp)){
                        Column {
                            // WebView
                            // https://kaleidot.net/jetpack-compose-%E3%81%AE-androidview-%E3%81%A7-webview-%E3%82%92%E5%88%A9%E7%94%A8%E3%81%99%E3%82%8B-b3cdb3efa69e
                            AndroidView(
                                factory = { ctx ->
                                    WebView(ctx).apply {
                                        layoutParams =
                                            android.widget.LinearLayout.LayoutParams(800, 600)
                                    }
                                },
                                update = {
                                    it.webViewClient = android.webkit.WebViewClient()
                                    it.loadUrl("https://www.google.com")
                                })
                            Button(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                onClick = { showDialogState = false }
                            ) {
                                Text("Confirm")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun DefaultPreview() {
    ComposeMainScreen()
}