package dev.petuska.azure.functions.kotlin.handler

import HttpRequest
import NodeJS.Dict
import NodeJS.get
import dev.petuska.azure.functions.kotlin.AzureFunction
import util.isNullOrUndefined
import kotlin.js.json

val myFunctionHandler = AzureFunction<HttpRequest> { req ->
    res = if (!isNullOrUndefined(req.query["name"].unsafeCast<Any>()) ||
        (!isNullOrUndefined(req.body.unsafeCast<Any>()) && !isNullOrUndefined(req.body.unsafeCast<Dict<String>>()["name"].unsafeCast<Any>()))
    ) {
        json(
            "status" to 200,
            "body" to "Hello ${req.query["name"] ?: req.body.unsafeCast<Dict<String>>()["name"]}",
        )
    } else {
        json(
            "status" to 400,
            "body" to "Please pass a name on the query string or in the request body",
        )
    }
    log("JavaScript HTTP trigger function processed a request.")
}