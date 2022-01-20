package co.touchlab.kampkit.remove.response

import kotlinx.serialization.Serializable

@Serializable
data class BreedResult(
    val message: Map<String, List<String>>,
    var status: String
)
