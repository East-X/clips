package com.eastx7.clips.data

data class Clips (
   var id: String? = null,
    var group: String? = null,
    var color: String? = null,
    var file_url: String? = null,
    var thumbnail_url: String? = null,
    var poster_url: String? = null,
    var small_thumbnail_url: String? = null,
    var small_poster_url: String? = null,
    var is_favorite: Boolean = false
)