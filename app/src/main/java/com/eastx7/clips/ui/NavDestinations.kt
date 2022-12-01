package com.eastx7.clips.ui

sealed class NavDestinations(val route: String) {

    object ClipList : NavDestinations("clipslist")
    object ClipItem : NavDestinations("clipsitem") {
        const val itemWithArgument: String = "clipistem/{id}"
        const val argument0: String = "id"
    }
}
