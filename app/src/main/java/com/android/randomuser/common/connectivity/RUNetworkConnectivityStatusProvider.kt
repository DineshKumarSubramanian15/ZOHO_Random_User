package com.android.randomuser.common.connectivity

import com.android.goodanime.common.GAInternetConnectivityStatus
import com.android.randomuser.common.di.RUApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * The class that observers the internet status
 */
class RUNetworkConnectivityStatusProviderImpl @Inject constructor(
    networkObserver: RUNetworkConnectivityObserver,
    @RUApplicationScope private val externalScope: CoroutineScope
) : RUNetworkConnectivityStatusProvider {
    override var internetStatus: StateFlow<Boolean> =
        networkObserver.observe().map { it == GAInternetConnectivityStatus.AVAILABLE }
            .stateIn(externalScope, SharingStarted.Eagerly, true)
}

/**
 * The interface that communicates with internet connectivity status provider
 */
interface RUNetworkConnectivityStatusProvider {
    /**
     * The property that provides the internet connectivity status as a flow
     *
     * @return The connectivity status of the internet
     */
    var internetStatus: StateFlow<Boolean>
}