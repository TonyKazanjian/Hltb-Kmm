package co.touchlab.kampkit.remove.ktor

import co.touchlab.kampkit.remove.response.BreedResult

interface DogApi {
    suspend fun getJsonFromApi(): BreedResult
}
