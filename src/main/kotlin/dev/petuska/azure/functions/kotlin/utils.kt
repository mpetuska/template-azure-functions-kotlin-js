package dev.petuska.azure.functions.kotlin

import AzureFunction
import Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.promise
import kotlin.coroutines.EmptyCoroutineContext

fun <T, R> AzureFunction(action: suspend Context.(args: T) -> R): AzureFunction = { context, args ->
  val coroutineScope = CoroutineScope(EmptyCoroutineContext)
  coroutineScope.promise { action(context, args.unsafeCast<T>()) }.then {
    coroutineScope.cancel()
    it
  }.catch {
    coroutineScope.cancel()
    throw it
  }
}
