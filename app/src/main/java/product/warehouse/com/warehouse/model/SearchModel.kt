package product.warehouse.com.warehouse.model


data class SearchModel(
    val HitCount: String,
    val Results: List<Result>
)

data class Result(
    val Description: String,
    val Products: List<Product>
)

