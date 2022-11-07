package com.example.jetpackcomposeapplication

import android.content.Context

interface AppContainer {
}

class AppContainerImpl(private val applicationContext: Context) : AppContainer {
}
