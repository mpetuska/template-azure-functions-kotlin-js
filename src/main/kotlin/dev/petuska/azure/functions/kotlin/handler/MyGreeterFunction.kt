package dev.petuska.azure.functions.kotlin.handler

import HttpRequest
import NodeJS.get
import dev.petuska.azure.functions.kotlin.AzureFunction
import kotlin.js.Date
import kotlin.js.json

val myGreeterFunctionHandler = AzureFunction<HttpRequest, Unit> {
  res = json(
    "status" to 200,
    "body" to "${Date()}Greetings, Stranger!",
  )
  log("JavaScript HTTP trigger function processed a greeting request.")
}
