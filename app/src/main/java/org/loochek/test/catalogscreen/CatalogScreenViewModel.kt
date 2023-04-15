package org.loochek.test.catalogscreen

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.loochek.test.data.Restaurant
import org.loochek.test.data.RestaurantsRepository

import javax.inject.Inject

sealed class CatalogScreenEvent {
    object UpdateButtonClicked: CatalogScreenEvent()
}

data class CatalogScreenViewState(
    val nearest: List<Restaurant> = listOf(),
    val popular: List<Restaurant> = listOf(),
    val status: String = "Loading...",
)

sealed class CatalogScreenViewAction {
    class ShowToast(val text: String, val duration: Int) : CatalogScreenViewAction()
}

@HiltViewModel
class CatalogScreenViewModel @Inject constructor(private val repository: RestaurantsRepository) : ViewModel() {
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
                updateCatalog(forceUpdate=true)
            }
        }
    }

    private fun updateCatalog(forceUpdate: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCatalogResponse(forceUpdate).collect { response ->
                if (response.updateError == true) {
                    _viewState.emit(_viewState.value.copy(status = "Error!"))
                    _viewAction.tryEmit(
                        CatalogScreenViewAction.ShowToast(
                            "Update error!",
                            Toast.LENGTH_SHORT
                        )
                    )
                    return@collect
                }

                _viewState.emit(_viewState.value.copy(
                    nearest = response.nearest,
                    popular = response.popular
                ))
            }
        }
    }
}