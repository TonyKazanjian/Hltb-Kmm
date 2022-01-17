import it.skrape.fetcher.ContentType
import it.skrape.fetcher.NonBlockingFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.Result

class TestFetcher: NonBlockingFetcher<Request> {

    private val htmlResponse = this.javaClass.getResource("search_test.html")?.readText()
    override val requestBuilder: Request get() = Request()

    override suspend fun fetch(request: Request): Result {
        return Result(
            responseBody = htmlResponse!!,
            responseStatus = Result.Status(200, "OK"),
            contentType = ContentType(),
            headers = mapOf("Content-type" to "application/x-www-form-urlencoded"),
            baseUri = request.url,
            cookies = emptyList()
        )
    }
}