package com.android.randomuser.common.di

import com.android.randomuser.common.api.localDataSource.RUUsersLocalDataSourceImpl
import com.android.randomuser.common.api.localDataSource.RUUsersLocalDataSource
import com.android.randomuser.common.api.remoteDataSource.RUUsersRemoteDataSourceImpl
import com.android.randomuser.common.api.remoteDataSource.RUUsersRemoteDataSource
import com.android.randomuser.common.api.repository.RUUsersRepoImpl
import com.android.randomuser.common.api.repository.RUUsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class RUJobsModule {

    @Binds
    abstract fun bindJobsRepository(impl: RUUsersRepoImpl): RUUsersRepository

    @Binds
    abstract fun bindJobsRemoteDataSource(impl: RUUsersRemoteDataSourceImpl): RUUsersRemoteDataSource

    @Binds
    abstract fun bindUsersLocalDataSource(impl: RUUsersLocalDataSourceImpl): RUUsersLocalDataSource
}
