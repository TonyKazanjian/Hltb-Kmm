package co.touchlab.kampkit

actual fun readResource(
    resourceName: String
): String {
    return ClassLoader
        .getSystemResource(resourceName)
        .readText()
}