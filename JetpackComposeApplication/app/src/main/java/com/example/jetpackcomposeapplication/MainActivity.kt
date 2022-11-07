package com.example.jetpackcomposeapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.viewinterop.AndroidView
import com.unity3d.player.UnityPlayer

class MainActivity : ComponentActivity() {
    var unityPlayer: UnityPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // https://qiita.com/cha84rakanal/items/4a1e4b242ec40e3e8121
        unityPlayer = UnityPlayer(this) // add String.xml https://forum.unity.com/threads/unity-2018-3-6-upgrade-to-unity-2018-4-has-bug.681997/
        unityPlayer?.requestFocus()

        setContent {
            // TODO: https://developer.android.com/jetpack/compose/interop/interop-apis?hl=ja#composition-strategy
            // setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            AndroidView(factory = { ctx ->
                unityPlayer?.view!!
            })
            val appContainer = AppContainerImpl(this)
            MainApp(appContainer)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        unityPlayer?.windowFocusChanged(hasFocus)
    }
    override fun onResume() {
        super.onResume()
        unityPlayer?.resume()
    }
}