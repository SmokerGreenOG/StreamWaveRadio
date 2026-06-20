package com.streamwave.radio.di

import android.content.Context
import androidx.room.Room
import com.streamwave.radio.data.database.AppDatabase
import com.streamwave.radio.data.database.dao.CategoryDao
import com.streamwave.radio.data.database.dao.FavoriteDao
import com.streamwave.radio.data.database.dao.OfficialStationDao
import com.streamwave.radio.data.database.dao.PersonalStationDao
import com.streamwave.radio.data.database.dao.RecentStationDao
import com.streamwave.radio.data.database.dao.StationSubmissionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideOfficialStationDao(db: AppDatabase): OfficialStationDao =
        db.officialStationDao()

    @Provides
    fun providePersonalStationDao(db: AppDatabase): PersonalStationDao =
        db.personalStationDao()

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao =
        db.categoryDao()

    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao =
        db.favoriteDao()

    @Provides
    fun provideRecentStationDao(db: AppDatabase): RecentStationDao =
        db.recentStationDao()

    @Provides
    fun provideStationSubmissionDao(db: AppDatabase): StationSubmissionDao =
        db.stationSubmissionDao()
}
