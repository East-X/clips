package com.eastx7.clips.ui

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.GlideLazyListPreloader
import com.eastx7.clips.R
import com.eastx7.clips.data.Constants.THUMBNAIL_DIMENSION
import com.eastx7.clips.data.Thumbnails
import com.eastx7.clips.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClipListScreen(
    curVideo: Int,
    queue: ArrayList<MediaItem>,
    thumbnailsList: List<Thumbnails>,
    onItemClick: (Thumbnails) -> Unit,
    showCommentField: Boolean,
    onCommentBtnClick: () -> Unit,
    onDismiss: () -> Unit,
) {

    Scaffold(
        topBar = {
            ClipListTopBar(
                onDismiss = onDismiss,
            )
        },

        content = { innerPadding ->
            BodyList(
                curVideo = curVideo,
                queue = queue,
                thumbnailsList = thumbnailsList,
                showCommentField = showCommentField,
                onItemClick = onItemClick,
                onCommentBtnClick = onCommentBtnClick,
                innerPadding = innerPadding
            )
        }
    )
}

@Composable
fun BodyList(
    curVideo: Int,
    showCommentField: Boolean,
    queue: ArrayList<MediaItem>,
    thumbnailsList: List<Thumbnails>,
    onItemClick: (Thumbnails) -> Unit,
    onCommentBtnClick: () -> Unit,
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
            .statusBarsPadding(),
    ) {
        if (queue.isNotEmpty()) {
            VideoPlayer(
                curVideo = curVideo,
                queue = queue
            )
        }
        BodyListComment(
            showCommentField = showCommentField,
            onCommentBtnClick = onCommentBtnClick
        )
        BodyListThumbnails(
            thumbnailsList = thumbnailsList,
            onItemClick = onItemClick
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BodyListThumbnails(
    thumbnailsList: List<Thumbnails>,
    onItemClick: (Thumbnails) -> Unit,
) {
    val state = rememberLazyListState()
    val posterHeight = LocalConfiguration.current.screenHeightDp.dp / 5

    VertFieldSpacer()
    Text(
        text = stringResource(R.string.you_may_also_like).uppercase(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        style = ClipTypography.titleLarge
    )
    LazyRow(
        modifier = Modifier.padding(start = 7.dp, end = 7.dp),
        state = state
    ) {
        items(thumbnailsList)
        { item ->
            BodyListItem(
                item = item,
                posterDimensions = posterHeight,
                onItemClick = onItemClick,
            )
        }
    }
    GlideLazyListPreloader(
        state = state,
        data = thumbnailsList,
        size = Size(THUMBNAIL_DIMENSION.toFloat(), THUMBNAIL_DIMENSION.toFloat()),
        numberOfItemsToPreload = 15,
        fixedVisibleItemCount = 5,
    ) { item, requestBuilder ->
        requestBuilder.load(item.poster)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BodyListItem(
    item: Thumbnails,
    posterDimensions: Dp,
    onItemClick: (Thumbnails) -> Unit,
) {
    GlideImage(
        model = item.poster,
        contentDescription = null,
        modifier = Modifier
            .padding(5.dp)
            .clickable(onClick = {
                onItemClick(item)
            }
            )
            .width(posterDimensions * 1.5f)
            .height(posterDimensions),
    )
}

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayer(
    curVideo: Int,
    queue: ArrayList<MediaItem>
) {
    val context = LocalContext.current
    val sizeInPx =
        with(LocalDensity.current) {
            LocalConfiguration.current.screenHeightDp.dp.toPx()
        }
    val height = (sizeInPx / 2).toInt()

    val trackSelector = DefaultTrackSelector(context).apply {
        setParameters(buildUponParameters().setMaxVideoSizeSd())
    }
    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
    }


    LaunchedEffect(curVideo) {
        exoPlayer.apply {
            setMediaItem(queue[curVideo])
            playWhenReady = true
//        videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            repeatMode = Player.REPEAT_MODE_ALL
            prepare()
        }
    }


    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                hideController()
                useController = false
                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, height)

            }
        }
        )
    ) {
        onDispose { exoPlayer.release() }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyListComment(
    showCommentField: Boolean,
    onCommentBtnClick: () -> Unit
) {
    if (showCommentField) {
        VertFieldSpacer()
        var textComment by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }
        TextField(
            value = textComment,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            onValueChange = {
                textComment = it
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.write_your_comment),
                )
            },
            singleLine = false,
        )
    }
    VertFieldSpacer()
    Button(
        onClick = onCommentBtnClick,
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
    ) {
        Text(
            stringResource(
                if (showCommentField) {
                    R.string.send
                } else {
                    R.string.leave_comment
                }
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClipListTopBar(
    onDismiss: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                stringResource(R.string.movies),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "back"
                )
            }
        }
    )
}