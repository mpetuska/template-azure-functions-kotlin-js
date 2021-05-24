package dev.petuska.azure.functions.kotlin

import AzureFunction
import Context
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

fun <T> AzureFunction(action: suspend Context.(args: T) -> dynamic): AzureFunction = { context, args ->
    GlobalScope.promise { action(context, args.unsafeCast<T>()) }
}