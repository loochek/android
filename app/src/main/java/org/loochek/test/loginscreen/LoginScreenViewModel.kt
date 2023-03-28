package org.loochek.test.loginscreen

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.loochek.test.MainActivity

sealed class LoginScreenEvent {
    class NameChanged(val name: String) : LoginScreenEvent()
    class EmailChanged(val email: String) : LoginScreenEvent()
    class PasswordChanged(val password: String) : LoginScreenEvent()

    class PasswordVisibilityChanged(val visible: Boolean) : LoginScreenEvent()
    class KeepSignedInChanged(val checked: Boolean) : LoginScreenEvent()
    class NotifySpecialPricingChanged(val checked: Boolean) : LoginScreenEvent()

    object RegisterClicked : LoginScreenEvent()
    object AlreadyHaveAccountClicked : LoginScreenEvent()
}

data class LoginScreenViewState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val keepSignedIn: Boolean = true,
    val notifySpecialPricing: Boolean = true,
)

class LoginScreenViewModel(private val activity: MainActivity?) : ViewModel() {
    private val _viewState = MutableStateFlow(LoginScreenViewState())
    val viewState = _viewState.asStateFlow();

    fun handleEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.NameChanged -> {
                _viewState.value = _viewState.value.copy(name = event.name)
            }

            is LoginScreenEvent.EmailChanged -> {
                _viewState.value = _viewState.value.copy(email = event.email)
            }

            is LoginScreenEvent.PasswordChanged -> {
                _viewState.value = _viewState.value.copy(password = event.password)
            }

            is LoginScreenEvent.PasswordVisibilityChanged -> {
                _viewState.value = _viewState.value.copy(passwordVisible = event.visible)
            }

            is LoginScreenEvent.KeepSignedInChanged -> {
                _viewState.value = _viewState.value.copy(keepSignedIn = event.checked)
            }

            is LoginScreenEvent.NotifySpecialPricingChanged -> {
                _viewState.value = _viewState.value.copy( notifySpecialPricing = event.checked)
            }

            LoginScreenEvent.AlreadyHaveAccountClicked -> {
                Toast.makeText(activity, "500 Internal Server Error", Toast.LENGTH_SHORT).show()
            }

            LoginScreenEvent.RegisterClicked -> {
                Toast.makeText(activity, "501 Not Implemented", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class LoginScreenViewModelFactory(private val activity: MainActivity): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginScreenViewModel(activity) as T
    }
}