package co.touchlab.kampkit.scraper

import co.touchlab.kampkit.readResource
import it.skrape.fetcher.ContentType
import it.skrape.fetcher.NonBlockingFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.Result

class TestFetcher: NonBlockingFetcher<Request> {

    // private val htmlResponse = this.javaClass.getResource("search_test.html")?.readText()
    override val requestBuilder: Request get() = Request()

    private val htmlResponse: String = "<div class=\"global_padding shadow_box back_blue center\">\n" +
        "\t<h3> We Found 2 Games for \"like a dragon\" </h3> \t\t\t</div>\n" +
        "\n" +
        "<ul>\n" +
        "\t<div class=\"clear\"></div>\n" +
        "\t<li class=\"back_darkish\" \t\t\tstyle=\"background-image:linear-gradient(rgb(31, 31, 31), rgba(31, 31, 31, 0.9)), url('/games/71931_Yakuza_Like_a_Dragon.jpg')\"> \t\t\t\t<div class=\"search_list_image\">\n" +
        "\t\t<a aria-label=\"Yakuza Like a Dragon\" title=\"Yakuza Like a Dragon\" href=\"game?id=71931\">\n" +
        "\t\t\t<img alt=\"Box Art\" src=\"/games/71931_Yakuza_Like_a_Dragon.jpg\" />\n" +
        "\t\t</a>\n" +
        "\t</div> \t\t\t<div class=\"search_list_details\">\t\t\t\t\t<h3 class=\"shadow_text\">\n" +
        "\t\t<a class=\"text_white\" title=\"Yakuza Like a Dragon\" href=\"game?id=71931\">Yakuza: Like a Dragon</a>\n" +
        "\t</h3> \t\t\t\t\t<div class=\"search_list_details_block\"> \t\t\t\t\t\t\t\t<div>\n" +
        "\t\t<div class=\"search_list_tidbit text_white shadow_text\">Main Story</div>\n" +
        "\t\t<div class=\"search_list_tidbit center time_100\">44&#189; Hours </div>\n" +
        "\t\t<div class=\"search_list_tidbit text_white shadow_text\">Main + Extra</div>\n" +
        "\t\t<div class=\"search_list_tidbit center time_100\">66 Hours </div>\n" +
        "\t\t<div class=\"search_list_tidbit text_white shadow_text\">Completionist</div>\n" +
        "\t\t<div class=\"search_list_tidbit center time_100\">99&#189; Hours </div>\n" +
        "\t</div>\t\t\t\t\t</div> \t\t\t</div>\t</li>\n" +
        "\t<li class=\"back_darkish\" \t\t\tstyle=\"background-image:linear-gradient(rgb(31, 31, 31), rgba(31, 31, 31, 0.9)), url('/games/42407_Ryu_ga_Gotoku_6.jpg')\"> \t\t\t\t<div class=\"search_list_image\">\n" +
        "\t\t<a aria-label=\"Yakuza 6 The Song of Life\" title=\"Yakuza 6 The Song of Life\" href=\"game?id=42407\">\n" +
        "\t\t\t<img alt=\"Box Art\" src=\"/games/42407_Ryu_ga_Gotoku_6.jpg\" />\n" +
        "\t\t</a>\n" +
        "\t</div> \t\t\t<div class=\"search_list_details\">\t\t\t\t\t<h3 class=\"shadow_text\">\n" +
        "\t\t<a class=\"text_white\" title=\"Yakuza 6 The Song of Life\" href=\"game?id=42407\">Yakuza 6: The Song of Life</a>\n" +
        "\t</h3> \t\t\t\t\t<div class=\"search_list_details_block\"> \t\t\t\t\t\t\t\t<div>\n" +
        "\t\t<div class=\"search_list_tidbit text_white shadow_text\">Main Story</div>\n" +
        "\t\t<div class=\"search_list_tidbit center time_100\">18&#189; Hours </div>\n" +
        "\t\t<div class=\"search_list_tidbit text_white shadow_text\">Main + Extra</div>\n" +
        "\t\t<div class=\"search_list_tidbit center time_100\">31&#189; Hours </div>\n" +
        "\t\t<div class=\"search_list_tidbit text_white shadow_text\">Completionist</div>\n" +
        "\t\t<div class=\"search_list_tidbit center time_100\">51 Hours </div>\n" +
        "\t</div>\t\t\t\t\t</div> \t\t\t</div>\t</li> \t\t\t\t<div class=\"clear\"></div>\n" +
        "</ul>"
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