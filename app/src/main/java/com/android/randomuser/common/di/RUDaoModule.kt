package com.android.randomuser.common.di

import com.android.randomuser.common.database.RUDatabase
import com.android.randomuser.common.database.dao.RUUserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RUDaoModule {
    @Provides
    fun provideJobOfferDao(database: RUDatabase): RUUserDao = database.userDao()
}