package org.loochek.test.catalogscreen

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import org.loochek.test.data.Restaurant
import org.loochek.test.data.RestaurantPlacement

@Composable
fun CatalogScreen(
    bgColor: Color,
    viewModel: CatalogScreenViewModel = viewModel(),
    navController: NavController
) {
    val viewState by viewModel.viewState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.viewAction.collect() {
                viewAction ->
            Toast.makeText(
                context,
                (viewAction as CatalogScreenViewAction.ShowToast).text,
                viewAction.duration,
            ).show()
        }
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
            Button(onClick = { viewModel.handleEvent(CatalogScreenEvent.UpdateButtonClicked)}) {
                Text("Update catalog")
            }

            Spacer(Modifier.size(10.dp))
            Catalog(viewState, navController)
        }
    }
}

@Composable
fun Catalog(viewState: CatalogScreenViewState, navController: NavController) {
    if (viewState.nearest.size + viewState.popular.size == 0) {
        Text(viewState.status)
    } else {
        Text("Nearest restraunts:")
        Spacer(Modifier.size(10.dp))

        for (restaurant in viewState.nearest) {
            RestaurantEntry(restaurant, navController)
        }

        Text("Popular restraunts:")
        Spacer(Modifier.size(10.dp))

        for (restaurant in viewState.popular) {
            RestaurantEntry(restaurant, navController)
        }
    }
}

@Composable
fun RestaurantEntry(restaurant: Restaurant, navController: NavController) {
    Card (modifier = Modifier.clickable(
        interactionSource = MutableInteractionSource(), indication = rememberRipple(
            bounded = true,
            radius = 100.dp,
            color = MaterialTheme.colors.primary
        )) {
            val placement = when (restaurant.placement) {
                RestaurantPlacement.Nearest -> "nearest"
                RestaurantPlacement.Popular -> "popular"
                else -> "dummy"
            }

            navController.navigate("detail/${restaurant.id}/${placement}")
        }
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(restaurant.image)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = restaurant.name,
                modifier = Modifier.size(50.dp, 50.dp),
                alignment = Alignment.Center
            )

            Spacer(Modifier.size(20.dp))

            Text(text = restaurant.name, modifier = Modifier.align(alignment = Alignment.CenterVertically))
            Row(modifier = Modifier.fillMaxWidth().align(alignment = Alignment.CenterVertically),
                horizontalArrangement = Arrangement.End) {
                Text(text = restaurant.deliveryTime,
                     color = MaterialTheme.colors.primary,
                     modifier = Modifier.align(alignment = Alignment.CenterVertically)
                         .padding(horizontal = 5.dp, vertical = 5.dp))
            }
        }
    }

    Spacer(Modifier.size(10.dp))
}