package co.touchlab.kampkit.scraper

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.timeout
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.Cookie
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.setCookie
import io.ktor.http.toHttpDate
import io.ktor.util.InternalAPI
import io.ktor.util.flattenEntries
import it.skrape.fetcher.Domain
import it.skrape.fetcher.Expires
import it.skrape.fetcher.Method
import it.skrape.fetcher.NonBlockingFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.Result
import it.skrape.fetcher.SameSite
import it.skrape.fetcher.urlOrigin
import java.util.Locale

object AndroidAsyncFetcher: NonBlockingFetcher<Request> {

    override val requestBuilder: Request get() = Request()

    override suspend fun fetch(request: Request): Result = configuredClient(request).toResult()


    @Suppress("MagicNumber")
    private suspend fun configuredClient(request: Request): HttpResponse =
        HttpClient(OkHttp) {
            expectSuccess = false
            followRedirects = request.followRedirects
            install(HttpTimeout)
            install(Logging) {
                level = LogLevel.NONE
            }
            HttpResponseValidator {
                handleResponseException { cause: Throwable ->
                    when (cause) {
                        is SocketTimeoutException -> {
                            throw cause
                        }
                    }
                }
            }
        }.use {
            it.request(request.toHttpRequest())
        }
}

@OptIn(InternalAPI::class)
internal fun Request.toHttpRequest(): HttpRequestBuilder {
    val request = this
    return HttpRequestBuilder().apply {
        method = request.method.toHttpMethod()
        url(request.url)
        headers {
            request.headers.forEach { (k, v) ->
                append(k, v)
            }
            append("User-Agent", request.userAgent)
            cookies = request.cookies
            request.authentication?.run {
                append("Authorization", toHeaderValue())
            }
        }
        request.body?.run {
            body = this
        }
        timeout {
            socketTimeoutMillis = request.timeout.toLong()
        }
    }
}

private fun Method.toHttpMethod(): HttpMethod = when (this) {
    Method.GET -> HttpMethod.Get
    Method.POST -> HttpMethod.Post
    Method.HEAD -> HttpMethod.Head
    Method.DELETE -> HttpMethod.Delete
    Method.PATCH -> HttpMethod.Patch
    Method.PUT -> HttpMethod.Put
}

private suspend fun HttpResponse.toResult(): Result = Result(
    responseBody = this.bodyAsText(),
    responseStatus = this.toStatus(),
    contentType = this.contentType()?.toString()?.replace(" ", ""),
    headers = this.headers.flattenEntries()
        .associateBy({ item -> item.first }, { item -> this.headers[item.first]!! }),
    cookies = this.setCookie().map { cookie -> cookie.toDomainCookie(this.request.url.toString().urlOrigin) },
    baseUri = this.request.url.toString()
)

private fun Cookie.toDomainCookie(origin: String): it.skrape.fetcher.Cookie {
    val path = this.path ?: "/"
    val expires = this.expires?.toHttpDate().toExpires()
    val domain = when (val domainUrl = this.domain) {
        null -> Domain(origin, false)
        else -> Domain(domainUrl.urlOrigin, true)
    }
    val sameSite = this.extensions["SameSite"].toSameSite()
    val maxAge = this.maxAge.toMaxAge()

    return it.skrape.fetcher.Cookie(this.name, this.value, expires, maxAge, domain, path, sameSite, this.secure, this.httpOnly)
}

private fun Int.toMaxAge(): Int? = when (this) {
    0 -> null
    else -> this
}

private fun String?.toExpires(): Expires {
    return when (this) {
        null -> Expires.Session
        else -> Expires.Date(this)
    }
}

internal fun String?.toSameSite(): SameSite = when (this?.toLowerCase(Locale.getDefault())) {
    "strict" -> SameSite.STRICT
    "lax" -> SameSite.LAX
    "none" -> SameSite.NONE
    else -> SameSite.LAX
}

private fun HttpResponse.toStatus() = Result.Status(this.status.value, this.status.description)
