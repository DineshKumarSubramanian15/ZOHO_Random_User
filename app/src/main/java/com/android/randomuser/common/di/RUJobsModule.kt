package com.android.randomuser.common.di

import com.android.randomuser.common.api.remoteDataSource.RUTodoRemoteDataSourceImpl
import com.android.randomuser.common.api.remoteDataSource.RUTodoRemoteDataSource
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
    abstract fun bindJobsRemoteDataSource(impl: RUTodoRemoteDataSourceImpl): RUTodoRemoteDataSource
}
