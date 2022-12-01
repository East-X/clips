package com.eastx7.clips.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.eastx7.clips.api.GsonService
import java.net.CookieManager
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideCookieManager(): CookieManager {
        return CookieManager()
    }

    @Singleton
    @Provides
    fun provideGsonService(cookieManager: CookieManager): GsonService {
        return GsonService.create(cookieManager)
    }
}
