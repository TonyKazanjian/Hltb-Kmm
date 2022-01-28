package co.touchlab.kampkit.scraper

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.touchlab.kampkit.MR
import it.skrape.fetcher.ContentType
import it.skrape.fetcher.NonBlockingFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.Result

class TestFetcher: NonBlockingFetcher<Request> {

    private val htmlResponse = MR.files.search_test.readText(context = ApplicationProvider.getApplicationContext<Application>())
    override val requestBuilder: Request get() = Request()

    override suspend fun fetch(request: Request): Result {
        return Result(
            responseBody = htmlResponse,
            responseStatus = Result.Status(200, "OK"),
            contentType = ContentType(),
            headers = mapOf("Content-type" to "application/x-www-form-urlencoded"),
            baseUri = request.url,
            cookies = emptyList()
        )
    }
}