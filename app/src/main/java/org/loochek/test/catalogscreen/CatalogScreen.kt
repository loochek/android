package org.loochek.test.catalogscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun CatalogScreen(bgColor: Color, viewModel: CatalogScreenViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current
    val viewState by viewModel.viewState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { focusManager.clearFocus() },
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
            Catalog(viewModel, viewState)
        }
    }
}

@Composable
fun Catalog(viewModel: CatalogScreenViewModel, viewState: CatalogScreenViewState) {
    if (!viewState.loaded) {
        Text(viewState.status)
    } else {
        Text("Nearest restraunts:")
        Spacer(Modifier.size(10.dp))

        for (restaurant in viewState.nearest) {
            RestaurantEntry(restaurant)
        }

        Text("Popular restraunts:")
        Spacer(Modifier.size(10.dp))

        for (restaurant in viewState.popular) {
            RestaurantEntry(restaurant)
        }
    }
}

@Composable
fun RestaurantEntry(restaurant: Restaurant) {
    Card {
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