package co.touchlab.kampkit.scraper

import TestFetcher
import co.touchlab.kampkit.data.HowLongToBeatResponse
import it.skrape.fetcher.skrape

class TestSearchInteractor(private val fetcher: TestFetcher) : SearchInteractor {

    private val htmlParser = SearchInteractorFactory.createHtmlParser()
    override suspend fun search(query: String, page: Int): HowLongToBeatResponse {
        return skrape(fetcher) {
            htmlParser.parseHtmlResponse(this)
        }
    }
}
