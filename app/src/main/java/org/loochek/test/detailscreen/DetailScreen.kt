package org.loochek.test.detailscreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import org.loochek.test.data.Restaurant
import org.loochek.test.data.RestaurantPlacement

@Composable
fun DetailScreen(
    bgColor: Color,
    viewModel: DetailScreenViewModel = viewModel(),
    id: Int,
    placement: String
) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(CatalogScreenEvent.DetailOpened(
            id,
            when (placement) {
                "popular" -> RestaurantPlacement.Popular
                "nearest" -> RestaurantPlacement.Nearest
                else -> RestaurantPlacement.Dummy
            }
        ))
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = bgColor
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            val restaurant = viewState.restaurant

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(viewState.restaurant.image)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = restaurant.name,
                modifier = Modifier.size(200.dp, 200.dp),
                alignment = Alignment.Center
            )

            Spacer(Modifier.size(10.dp))

            Text(text = restaurant.name, modifier = Modifier.align(alignment = Alignment.CenterHorizontally))

            Spacer(Modifier.size(20.dp))

            Card (modifier = Modifier.height(50.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Delivery time", modifier = Modifier.align(alignment = Alignment.CenterVertically))
                    Row(modifier = Modifier.fillMaxWidth().align(alignment = Alignment.CenterVertically),
                        horizontalArrangement = Arrangement.End) {
                        Text(text = restaurant.deliveryTime,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                                .padding(horizontal = 5.dp, vertical = 5.dp))
                    }
                }
            }
        }
    }
}