package com.example.jetpackcomposeapplication

import android.content.res.Configuration
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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
        var clickedFloatingButton by remember { mutableStateOf(false) }
        var clickedViewButton by remember { mutableStateOf(false) }

        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = Color(0), // transparent background
            topBar =  {
                TopAppBar(title = { Text("Title") }, backgroundColor = MaterialTheme.colors.background)
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = { FloatingActionButton(onClick = {
                clickedFloatingButton = true
            }){ Text("OK") } },
            drawerContent =
            {
                Column()
                {
                    Text(text = "drawerContent")
                    // Animation
                    // https://developer.android.com/jetpack/compose/animation
                    Crossfade(targetState = clickedViewButton) { state ->
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
                        android.widget.Button(ctx).also {
                            it.layoutParams = android.widget.LinearLayout.LayoutParams(
                                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            it.text = "HOGEHOGE"
                            it.setOnClickListener({ clickedViewButton = !clickedViewButton });
                        }
                    }, modifier = Modifier.padding(top = 20.dp))
                }
            },
            drawerShape = MaterialTheme.shapes.small,
            content = {
                Column()
                {
                    ComposeList(
                        messages = listOf(
                            ListItem("Name", "aaaaaa"),
                            ListItem("Name", "cccc\nbbbb\ncccc")
                        )
                    )
                }
            },
            bottomBar = { BottomAppBar(backgroundColor = MaterialTheme.colors.surface) { Text("BottomAppBar") } }
        )

        // wake dialog
        if(clickedFloatingButton)
        {
            AlertDialog(
                title = {Text("Title")}, text = {Text("Text")},
                onDismissRequest = { clickedFloatingButton = false},
                confirmButton = { Button(onClick = {clickedFloatingButton = false}){Text("Confirm")} })
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