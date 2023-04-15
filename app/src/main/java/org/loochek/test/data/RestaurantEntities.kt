package org.loochek.test.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

enum class RestaurantPlacement(val placement: Int){
    Dummy(0),
    Popular(1),
    Nearest(2)
}

@Serializable
@Entity(tableName = "restaurants")
data class Restaurant(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "deliveryTime") val deliveryTime: String,
    @ColumnInfo(name = "image") val image: String,
    // Not present in API response - DB only
    @ColumnInfo(name = "placement") val placement: RestaurantPlacement? = null
)

@Serializable
@Entity(tableName = "commercials")
data class Commercial(
    @ColumnInfo(name = "picture") val picture: String,
    @ColumnInfo(name = "url") val url: String
)

@Serializable
data class RestaurantCatalogResponse(
    val nearest: List<Restaurant>,
    val popular: List<Restaurant>,
    val commercial: Commercial?,
    val actual: Boolean?,
    val updateError: Boolean?,
)
