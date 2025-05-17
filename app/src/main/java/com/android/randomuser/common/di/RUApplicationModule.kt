package com.android.randomuser.common.di

import android.content.Context
import android.net.ConnectivityManager
import com.android.randomuser.common.RUApplication
import com.android.randomuser.common.api.RULog
import com.android.randomuser.common.api.RURetrofit
import com.android.randomuser.common.connectivity.RUNetworkConnectivityStatusProvider
import com.android.randomuser.common.connectivity.RUNetworkConnectivityStatusProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * The module that provides the application level dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object RUApplicationModule {
    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    fun provideRetrofit(): RURetrofit = RURetrofit

    @Provides
    fun provideLog(): RULog = RULog

    @Provides
    @Singleton
    fun provideApplication(): RUApplication = RUApplication.getInstance()

    @RUIODispatcher
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @RUDefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @RUApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @RUDefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)
}

/**
 * The interface that provides the internet connectivity status
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RUInternetConnectivityModule {
    @Singleton
    @Binds
    abstract fun bindInternetConnectivityStatusProvider(impl: RUNetworkConnectivityStatusProviderImpl): RUNetworkConnectivityStatusProvider
}

/**
 * Annotation used to denote a custom scope for objects with application level
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class RUApplicationScope

/**
 * An annotation used to qualify the default dispatcher for application level
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class RUDefaultDispatcher

/**
 * An annotation used to qualify the I/O (Input/Output) dispatcher for application level
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class RUIODispatcher