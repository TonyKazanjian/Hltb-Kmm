package co.touchlab.kampkit.scraper

import co.touchlab.kampkit.AndroidJUnit4
import co.touchlab.kampkit.data.HowLongToBeatEntry
import co.touchlab.kampkit.data.HowLongToBeatResponse
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchTest {
    private val testSearchInteractor = AndroidSearchInteractor(TestFetcher())

    private val expectedResponse = HowLongToBeatResponse(
        statusCode = 200,
        entryList = mutableListOf(
            HowLongToBeatEntry(
                title = "Yakuza Like a Dragon",
                id = 71931,
                imageUrl = "https://www.howlongtobeat.com/games/71931_Yakuza_Like_a_Dragon.jpg",
                timeLabels = mapOf("Main Story" to "44½ Hours", "Main + Extra" to "66 Hours", "Completionist" to "99½ Hours")),
            HowLongToBeatEntry(
                title = "Yakuza 6 The Song of Life",
                id =42407,
                imageUrl = "https://www.howlongtobeat.com/games/42407_Ryu_ga_Gotoku_6.jpg",
                timeLabels = mapOf("Main Story" to "18½ Hours", "Main + Extra" to "31½ Hours", "Completionist" to "51 Hours"))
        ),
        totalResults = 2)
    @Test
    fun `can parse and verify response from htlb`() {
        runTest {
            val response = testSearchInteractor.search("query", 1)
            assertEquals(expectedResponse, response)
        }
    }
}