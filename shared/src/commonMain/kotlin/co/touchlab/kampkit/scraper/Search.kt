package co.touchlab.kampkit.scraper

import co.touchlab.kampkit.data.HowLongToBeatResponse

interface SearchInteractor {
    suspend fun search(query: String, page: Int = 1) : HowLongToBeatResponse
}
