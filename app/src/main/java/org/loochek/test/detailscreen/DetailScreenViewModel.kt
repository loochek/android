package org.loochek.test.detailscreen

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.loochek.test.data.Restaurant
import org.loochek.test.data.RestaurantPlacement
import org.loochek.test.data.RestaurantsRepository

import javax.inject.Inject

sealed class CatalogScreenEvent {
    class DetailOpened(val id: Int, val placement: RestaurantPlacement): CatalogScreenEvent()
}

data class CatalogScreenViewState(
    val restaurant: Restaurant
)

sealed class CatalogScreenViewAction {
    class ShowToast(val text: String, val duration: Int) : CatalogScreenViewAction()
}

@HiltViewModel
class DetailScreenViewModel @Inject constructor(private val repository: RestaurantsRepository) : ViewModel() {
    private val _viewState = MutableStateFlow(CatalogScreenViewState(Restaurant()))
    val viewState = _viewState.asStateFlow();

    private val _viewAction = MutableSharedFlow<CatalogScreenViewAction>(extraBufferCapacity = 64)
    val viewAction = _viewAction.asSharedFlow();

    fun handleEvent(event: CatalogScreenEvent) {
        when (event) {
            is CatalogScreenEvent.DetailOpened -> {
                update(event)
            }
        }
    }

    private fun update(event: CatalogScreenEvent.DetailOpened) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCatalogResponse().collect { response ->
                if (response.updateError == true) {
                    return@collect
                }

                val restaurants = when (event.placement) {
                    RestaurantPlacement.Popular -> response.popular
                    RestaurantPlacement.Nearest -> response.nearest
                    else -> return@collect
                }

                val restaurant = restaurants.find {it.id == event.id} ?: return@collect
                _viewState.emit(_viewState.value.copy(restaurant = restaurant))
            }
        }
    }
}