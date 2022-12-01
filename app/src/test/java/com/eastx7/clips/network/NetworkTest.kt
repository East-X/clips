package com.eastx7.clips.network

import com.eastx7.clips.api.GsonService
import com.eastx7.clips.data.ClipsRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.net.CookieManager

class NetworkTest {

    private lateinit var repository: ClipsRepository

    @Before
    fun setUp() {
        repository = ClipsRepository(GsonService.create(CookieManager()))
    }

    @After
    fun tearDown() {

    }

    @Test
    fun filmListAsExpected() {
        runBlocking {
            val listClips = repository.listClips()
            assertTrue(listClips!!.size != 0)
        }
    }
}