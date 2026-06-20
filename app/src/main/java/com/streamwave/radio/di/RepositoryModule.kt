package com.streamwave.radio.di

import com.streamwave.radio.data.repository.CategoryRepository
import com.streamwave.radio.data.repository.CategoryRepositoryImpl
import com.streamwave.radio.data.repository.FavoriteRepository
import com.streamwave.radio.data.repository.FavoriteRepositoryImpl
import com.streamwave.radio.data.repository.PersonalStationRepository
import com.streamwave.radio.data.repository.PersonalStationRepositoryImpl
import com.streamwave.radio.data.repository.RecentStationRepository
import com.streamwave.radio.data.repository.RecentStationRepositoryImpl
import com.streamwave.radio.data.repository.StationRepository
import com.streamwave.radio.data.repository.StationRepositoryImpl
import com.streamwave.radio.data.repository.SubmissionRepository
import com.streamwave.radio.data.repository.SubmissionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStationRepository(impl: StationRepositoryImpl): StationRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindRecentStationRepository(impl: RecentStationRepositoryImpl): RecentStationRepository

    @Binds
    @Singleton
    abstract fun bindPersonalStationRepository(impl: PersonalStationRepositoryImpl): PersonalStationRepository

    @Binds
    @Singleton
    abstract fun bindSubmissionRepository(impl: SubmissionRepositoryImpl): SubmissionRepository
}
