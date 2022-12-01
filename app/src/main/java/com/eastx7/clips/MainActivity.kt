package com.eastx7.clips

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import com.eastx7.clips.theme.ClipTheme
import com.eastx7.clips.ui.NavDestinations
import com.eastx7.clips.ui.ClipsNavGraph

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var arguments: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments = intent.extras
        setContent {
            ClipTheme {
                Row(
                    Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .windowInsetsPadding(
                            WindowInsets
                                .navigationBars
                                .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                        )
                ) {
                    ClipsNavGraph(
                        startDestination = NavDestinations.ClipList.route,
                    )
                }
            }
        }
    }
}
