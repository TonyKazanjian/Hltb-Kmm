package co.touchlab.kampkit.remove.ktor

import co.touchlab.kampkit.remove.response.BreedResult
import io.ktor.client.engine.HttpClientEngine
import co.touchlab.kermit.Logger as KermitLogger

class DogApiImpl(private val log: KermitLogger, engine: HttpClientEngine) : DogApi {
    override suspend fun getJsonFromApi(): BreedResult {
        return BreedResult(emptyMap(), "ok")
    }
}
