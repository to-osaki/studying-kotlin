package com.example.jetpackcomposeapplication

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

class MainAppNavigation(navHostController: NavHostController){
    val toMain: () -> Unit = {
        navHostController.navigate("Main") {
            android.util.Log.d("Navigation","Main")
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val toSub: () -> Unit = {
        android.util.Log.d("Navigation","Sub")
        navHostController.navigate("Sub"){
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
