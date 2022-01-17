package co.touchlab.kampkit.ktor

import co.touchlab.kampkit.response.BreedResult
import io.ktor.client.engine.HttpClientEngine
import co.touchlab.kermit.Logger as KermitLogger

class DogApiImpl(private val log: KermitLogger, engine: HttpClientEngine) : DogApi {
    override suspend fun getJsonFromApi(): BreedResult {
        return BreedResult(emptyMap(), "ok")
    }
}
