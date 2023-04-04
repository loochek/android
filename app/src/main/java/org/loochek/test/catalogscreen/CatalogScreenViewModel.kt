package org.loochek.test.catalogscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.request.get
import kotlinx.coroutines.launch

import javax.inject.Inject

sealed class CatalogScreenEvent {
    object UpdateButtonClicked: CatalogScreenEvent()
}

data class CatalogScreenViewState(
    val nearest: List<Restaurant> = listOf(),
    val popular: List<Restaurant> = listOf(),
    val status: String = "Loading...",
    val loaded: Boolean = false
)

sealed class CatalogScreenViewAction {
}

@HiltViewModel
class CatalogScreenViewModel @Inject constructor(private val httpClient: HttpClient) : ViewModel() {
    private val _viewState = MutableStateFlow(CatalogScreenViewState())
    val viewState = _viewState.asStateFlow();

    private val _viewAction = MutableSharedFlow<CatalogScreenViewAction>(extraBufferCapacity = 64)
    val viewAction = _viewAction.asSharedFlow();

    init {
        updateCatalog()
    }

    fun handleEvent(event: CatalogScreenEvent) {
        when (event) {
            is CatalogScreenEvent.UpdateButtonClicked -> {
                updateCatalog()
            }
        }
    }

    private fun updateCatalog() {
        viewModelScope.launch {
            try {
                val catalog = fetchRestaurantCatalog()
                _viewState.value = CatalogScreenViewState(catalog.nearest, catalog.popular, loaded = true)
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(status = "Loading error!", loaded = false)
            }
        }
    }

    private suspend fun fetchRestaurantCatalog(): RestaurantCatalogResponse {
        return httpClient.get("http://195.2.84.138:8081/catalog")
            .body()
    }
}