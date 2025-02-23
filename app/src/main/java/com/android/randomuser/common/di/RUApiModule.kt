package com.android.randomuser.common.di

import com.android.randomuser.common.api.RUApiCallHandler
import com.android.randomuser.common.api.RUApiCallHandlerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RUApiModule {

    @Binds
    abstract fun bindApiCallHandler(impl: RUApiCallHandlerImpl): RUApiCallHandler
}