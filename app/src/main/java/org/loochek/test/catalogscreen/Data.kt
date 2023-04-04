package org.loochek.test.catalogscreen

import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    val id: Int,
    val name: String,
    val deliveryTime: String,
    val image: String
)

@Serializable
data class Commercial(
    val picture: String,
    val url: String
)

@Serializable
data class RestaurantCatalogResponse(
    val nearest: List<Restaurant>,
    val popular: List<Restaurant>,
    val commercial: Commercial
)

