package dev.petuska.azure.functions.kotlin

import AzureFunction
import HttpRequest
import NodeJS.Dict
import NodeJS.get
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import util.isNullOrUndefined
import kotlin.js.json

val myHandler: AzureFunction = { context, args ->
    GlobalScope.promise {
        val req = args.unsafeCast<HttpRequest>()
        context.log("JavaScript HTTP trigger function processed a request.");
        if (!isNullOrUndefined(req.query["name"].unsafeCast<Any>()) || (!isNullOrUndefined(req.body.unsafeCast<Any>()) && !isNullOrUndefined(
                req.body.unsafeCast<Dict<String>>()["name"].unsafeCast<Any>()
            ))
        ) {
            context.res = json(
                "status" to 200,
                "body" to "Hello ${req.query["name"] ?: req.body.unsafeCast<Dict<String>>()["name"]}",
            )
        } else {
            context.res = json(
                "status" to 400,
                "body" to "Please pass a name on the query string or in the request body",
            )
        }
    }
}