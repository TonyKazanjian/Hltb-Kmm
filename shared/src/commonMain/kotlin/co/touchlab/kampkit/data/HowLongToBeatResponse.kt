package co.touchlab.kampkit.data

internal const val BASE_URL = "https://www.howlongtobeat.com"
data class HowLongToBeatResponse(
    var statusCode: Int = 0,
    var entryList: List<HowLongToBeatEntry> = mutableListOf(),
    var totalResults: Int = 0
)

data class HowLongToBeatEntry(
    val title: String?,
    val id: Int?,
    val imageUrl: String?,
    val timeLabels: Map<String, String>?

) {
    data class Builder(
        private var title: String? = null,
        private var id: Int? = null,
        private var imageUrl: String? = null,
        private var timeLabels: Map<String, String>? = null
    ){
        fun setTitle(title: String) = apply { this.title = title }
        fun setId(id: Int) = apply { this.id = id }
        fun setImageUrl(imageUrl: String) = apply { this.imageUrl = "$BASE_URL$imageUrl" }
        fun setTimeLabels(timeLabels: Map<String, String>) = apply { this.timeLabels = timeLabels }
        fun build() = HowLongToBeatEntry(title, id, imageUrl, timeLabels)
    }
}
