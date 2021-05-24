// Note no package, must be on root
import dev.petuska.azure.functions.kotlin.handler.myFunctionHandler
import dev.petuska.azure.functions.kotlin.handler.myGreeterFunctionHandler

@JsExport
val MyFunction: AzureFunction = myFunctionHandler

@JsExport
val MyGreeterFunction: AzureFunction = myGreeterFunctionHandler