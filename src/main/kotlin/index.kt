// Note no package, must be on root
import dev.petuska.azure.functions.kotlin.myHandler

@JsExport
val exportedHandler: AzureFunction = myHandler