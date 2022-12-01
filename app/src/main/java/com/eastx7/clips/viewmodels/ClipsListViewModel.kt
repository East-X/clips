package com.eastx7.clips.viewmodels

import androidx.lifecycle.*
import androidx.media3.common.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.eastx7.clips.R
import com.eastx7.clips.data.ClipsRepository
import com.eastx7.clips.data.Thumbnails
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class ClipsListViewModel @Inject constructor(
    private val clipsRepository: ClipsRepository
) : ViewModel() {

    private val _liveListThumbnails = MutableLiveData<List<Thumbnails>>(listOf())
    val liveListThumbnails: LiveData<List<Thumbnails>> = _liveListThumbnails

    private val _liveQueue = MutableLiveData<ArrayList<MediaItem>>(ArrayList())
    val liveQueue: LiveData<ArrayList<MediaItem>> = _liveQueue

    private val _textEmptyList = MutableStateFlow(R.string.empty_list)
    val textEmptyList: StateFlow<Int> = _textEmptyList.asStateFlow()

    private val _curVideo = MutableStateFlow<Int>(0)
    val curVideo: StateFlow<Int> = _curVideo.asStateFlow()

    private val _showCommentField = MutableStateFlow(false)
    val showCommentField: StateFlow<Boolean> = _showCommentField.asStateFlow()

    fun openCommentField() {
        _showCommentField.value = true
    }

    fun closeCommentField() {
        _showCommentField.value = false
    }

    fun populateListClips() {
        viewModelScope.launch {
            try {
                val listClips = clipsRepository.listClips()
                listClips?.let { list ->
                    val mediaQueue = ArrayList<MediaItem>()
                    val listThumbnails = mutableListOf<Thumbnails>()
                    list.forEachIndexed { index, item ->
                        listThumbnails.add(
                            Thumbnails(
                                id = index,
                                poster = item.small_thumbnail_url ?: ""
                            )
                        )
                        mediaQueue.add(
                            index,
                            MediaItem.Builder()
                                .setUri(item.file_url)
                                .build()
                        )
                    }
                    _liveQueue.value = mediaQueue
                    _liveListThumbnails.value = listThumbnails
                }
            } catch (e: UnknownHostException) {
                _textEmptyList.value = R.string.unknown_host
            } catch (e: ConnectException) {
                _textEmptyList.value = R.string.no_internet
            } catch (e: Exception) {
                _textEmptyList.value = R.string.unknown_exception
            }
        }
    }

    fun setCurVideo(indexVideo: Int) {
        _curVideo.value = indexVideo
    }
}