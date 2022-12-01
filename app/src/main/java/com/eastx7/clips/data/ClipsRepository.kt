package com.eastx7.clips.data

import com.eastx7.clips.api.GsonService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClipsRepository @Inject constructor(
    private var serviceGson: GsonService
) {

    suspend fun listClips(): List<Clips>? {
        return serviceGson.getClips("video", "1")
    }
}
