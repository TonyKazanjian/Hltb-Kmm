package co.touchlab.kampkit.scraper
import androidx.annotation.VisibleForTesting
import co.touchlab.kampkit.data.HowLongToBeatEntry
import co.touchlab.kampkit.data.HowLongToBeatResponse
import it.skrape.core.htmlDocument
import it.skrape.fetcher.Method
import it.skrape.fetcher.NonBlockingFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.Scraper
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.request.UrlBuilder
import it.skrape.fetcher.skrape
import it.skrape.selects.CssSelector
import it.skrape.selects.ElementNotFoundException
import it.skrape.selects.html5.*
import it.skrape.selects.text

class AndroidSearchInteractor(private val fetcher: NonBlockingFetcher<Request> = AndroidAsyncFetcher): SearchInteractor {
    override suspend fun search(query: String, page: Int): HowLongToBeatResponse {
        return skrape(fetcher) {
            request {
                method = Method.POST
                headers = mapOf("Content-type" to "application/x-www-form-urlencoded")
                url {
                    protocol = UrlBuilder.Protocol.HTTPS
                    host = "howlongtobeat.com"
                    path = "search_results.php"
                    port = -1
                    queryParam {
                        "page" to page
                    }
                }
                body {
                    form {
                        "queryString" to query
                        "t" to "games"
                        "sorthead" to "popular"
                        "sortd" to "0"
                        "plat" to ""
                        "length_type" to "main"
                        "length_min" to ""
                        "length_max" to ""
                        "detail" to "0"
                    }
                }
                timeout = 5000
                followRedirects = false
            }
            parseHtmlResponse(this)
        }
    }

    @VisibleForTesting
    suspend fun parseHtmlResponse(request: Any) = (request as Scraper<*>).extractIt<HowLongToBeatResponse> { hltbResponse ->
        val entryBuilder = HowLongToBeatEntry.Builder()
        val entryList = mutableListOf<HowLongToBeatEntry>()
        hltbResponse.statusCode = status { code }
        htmlDocument {
            div {
                findFirst {
                    h3 {
                        findFirst {
                            val totalResults = text.filter { it.isDigit() }.toInt()
                            hltbResponse.totalResults = totalResults
                        }
                    }
                }
            }
            li {
                findAll {
                    forEach { liElement ->
                        liElement.a {
                            findFirst {
                                val entryName = attributes["title"]
                                val entryId = attributes["href"]?.filter { idString -> idString.isDigit() }?.toInt()
                                entryBuilder
                                    .setTitle(entryName ?: "")
                                    .setId(entryId ?: 0)

                                img {
                                    findFirst {
                                        val imageSrc = attributes["src"]
                                        entryBuilder.setImageUrl(imageSrc ?: "")
                                    }
                                }
                            }
                        }
                        liElement.div {
                            findThird {
                                findFirst {
                                    findSecond {
                                        this.div {
                                            parseTimeLabelDetails(this, entryBuilder)
                                        }
                                    }
                                }
                            }
                        }
                        entryList.add(entryBuilder.build())
                    }
                }
            }
        }
        hltbResponse.entryList = entryList
    }

    private fun parseTimeLabelDetails(cssSelector: CssSelector, entryBuilder: HowLongToBeatEntry.Builder): HowLongToBeatEntry.Builder {
        val storyKey = "shadow_text"
        val timeKey = "center time"
        var storyChunk = ""
        var timeLength = ""
        val timeMap = mutableMapOf<String, String>()
        try {
            cssSelector.findAll {
                forEach { element ->
                    if (element.attributes["class"]!!.contains(storyKey)) {
                        storyChunk = element.findAll(element.attribute(storyKey)).text
                    }
                    if (element.attributes["class"]!!.contains(timeKey)) {
                        timeLength = element.findAll(element.attribute(timeKey)).text
                    }
                    timeMap[storyChunk] = timeLength
                }
            }
        } catch (e: ElementNotFoundException) {
            //TODO - kermit logger
        }
        return entryBuilder.setTimeLabels(timeMap)
    }
}