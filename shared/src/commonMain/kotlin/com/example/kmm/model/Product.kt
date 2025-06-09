import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val make: String? = null,
    val description: String? = null,
    val currentBid: Double,
    val endDate: String,
    val reservePriceStatus: String,
    val municipalityName: String,
    val mainImage: MainImage? = null,
    val categoryLevel1: Int? = null,
    val categoryLevel2: Int? = null,
    val categoryLevel3: Int? = null
)

@Serializable
data class MainImage(
    val imageUrlThumb: String,
    val imageUrlLarge: String
)