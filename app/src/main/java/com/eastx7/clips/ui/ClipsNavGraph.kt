package com.eastx7.clips.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eastx7.clips.viewmodels.ClipsListViewModel

@Composable
fun ClipsNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String,
) {
//TODO: Remove in production
//    navController.addOnDestinationChangedListener { controller, destination, arguments ->
//        destination.route?.let { navigatedRoute ->
//            Log.d("DestinationChanged", "Route: $navigatedRoute")
//        }
//    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(
            route = NavDestinations.ClipList.route
        ) { _ ->

            val listViewModel: ClipsListViewModel = hiltViewModel()
            listViewModel.populateListClips()
            val showCommentField: Boolean by listViewModel.showCommentField.collectAsState()
            val curVideo by listViewModel.curVideo.collectAsState()
            val thumbnailsList by listViewModel.liveListThumbnails.observeAsState(initial = listOf())
            val queue by listViewModel.liveQueue.observeAsState(initial = ArrayList())

            ClipListScreen(
                curVideo = curVideo,
                queue = queue,
                thumbnailsList = thumbnailsList,
                onItemClick = {
                    listViewModel.setCurVideo(it.id)
                },
                showCommentField = showCommentField,
                onCommentBtnClick = {
                    if (showCommentField) {
                        listViewModel.closeCommentField()
                    } else {
                        listViewModel.openCommentField()
                    }
                },
                onDismiss = {}
            )
        }
    }
}