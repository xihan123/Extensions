package cn.xihan.lib.pufferdb.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal suspend fun <T> runSuspend(
    context: CoroutineContext,
    body: suspend CoroutineScope.() -> T
) =
    withContext(context) { body() }

internal fun <T> runAsync(
    scope: CoroutineScope,
    dispatcher: CoroutineContext,
    body: suspend CoroutineScope.() -> T
) =
    scope.async(dispatcher) { body() }
