package com.example.jetpackcomposeapplication

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainApp(
    appContainer: AppContainer
) {
    val navController = rememberNavController()
    val navigator = remember(navController) {
        MainAppNavigation(navController)
    }
    
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)

    // https://developer.android.com/jetpack/compose/navigation
    // 各 NavController は 1 つの NavHost コンポーザブルに関連付ける必要があります。NavHost は NavController にナビゲーション グラフを関連付けます。
    // ナビゲーション グラフではコンポーザブルのデスティネーション（目的地）が指定されており、それらのデスティネーション間を移動できるようになります。
    BottomDrawer(
        drawerContent = {
            Row(modifier = Modifier.padding(60.dp)) {
                Button(onClick = { navigator.toMain() }) {
                    Text("Main")
                }
                Button(onClick = { navigator.toSub() }) {
                    Text("Sub")
                }
            }
        },
        drawerState = drawerState,
        gesturesEnabled = true,
    ) {
        NavHost(navController, "Main") {
            composable("Main") { MainScreen(appContainer) }
            composable("Sub") { SubScreen(appContainer) }
        }
    }
}