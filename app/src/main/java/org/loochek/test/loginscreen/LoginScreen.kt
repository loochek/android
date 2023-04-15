package org.loochek.test.loginscreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

import org.loochek.test.MainActivity
import org.loochek.test.utils.LabelledCheckBox
import org.loochek.test.utils.clearFocusOnKeyboardDismiss
import org.loochek.test.R

@Composable
fun LoginScreen(
    bgColor: Color,
    viewModel: LoginScreenViewModel = viewModel(),
    navController: NavController
) {
    val viewState by viewModel.viewState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.viewAction.collect() {
                viewAction ->
            when (viewAction) {
                is LoginScreenViewAction.Navigate -> {
                    navController.navigate(viewAction.location)
                }

                is LoginScreenViewAction.ShowToast -> {
                    Toast.makeText(
                        context,
                        viewAction.text,
                        viewAction.duration,
                    ).show()
                }
            }
        }
    }

    val focusManager = LocalFocusManager.current
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
            verticalArrangement = Arrangement.Center
        ) {
            Logo()
            LoginSection(viewModel, viewState)
        }
    }
}

@Composable
fun LoginSection(viewModel: LoginScreenViewModel,
                 viewState: LoginScreenViewState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        val textFieldModifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20))
            .clearFocusOnKeyboardDismiss()
        val textFieldColors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            disabledTextColor = Color.Transparent,
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )

        TextField(value = viewState.name,
            modifier = textFieldModifier,
            label = { Text(stringResource(R.string.your_name)) },
            onValueChange = { viewModel.handleEvent(LoginScreenEvent.NameChanged(it)) },
            singleLine = true,
            colors = textFieldColors,
            leadingIcon = { Icon(imageVector = Icons.Filled.AccountCircle, stringResource(R.string.your_name)) }
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextField(
            value = viewState.email,
            modifier = textFieldModifier,
            label = { Text(stringResource(R.string.email)) },
            onValueChange = { viewModel.handleEvent(LoginScreenEvent.EmailChanged(it)) },
            singleLine = true,
            colors = textFieldColors,
            leadingIcon = { Icon(imageVector = Icons.Filled.Email, stringResource(R.string.email)) }
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextField(
            value = viewState.password,
            modifier = textFieldModifier,
            label = { Text(stringResource(R.string.password)) },
            onValueChange = { viewModel.handleEvent(LoginScreenEvent.PasswordChanged(it)) },
            singleLine = true,
            colors = textFieldColors,
            visualTransformation = if (viewState.passwordVisible)
                VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (viewState.passwordVisible)
                    Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (viewState.passwordVisible)
                    stringResource(R.string.hide_password) else stringResource(R.string.show_password)

                IconButton(onClick = {
                    viewModel.handleEvent(
                        LoginScreenEvent.PasswordVisibilityChanged(!viewState.passwordVisible)
                    )
                }) {
                    Icon(imageVector = image, description)
                }
            },

            leadingIcon = { Icon(imageVector = Icons.Filled.Lock, stringResource(R.string.password)) }
        )

        Spacer(modifier = Modifier.height(5.dp))

        LabelledCheckBox(
            checked = viewState.keepSignedIn,
            onCheckedChange = { viewModel.handleEvent(LoginScreenEvent.KeepSignedInChanged(it)) },
            stringResource(R.string.keep_sign_in),
            modifier = Modifier.padding(horizontal = 6.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))
        LabelledCheckBox(
            checked = viewState.notifySpecialPricing,
            onCheckedChange = {
                viewModel.handleEvent(LoginScreenEvent.NotifySpecialPricingChanged(it))
            },
            stringResource(R.string.email_special_pricing),
            modifier = Modifier.padding(horizontal = 6.dp)
        )

        Spacer(Modifier.size(30.dp))

        Button(
            onClick = { viewModel.handleEvent(LoginScreenEvent.RegisterClicked) },
            modifier = Modifier
                .size(150.dp, 50.dp)
                .align(Alignment.CenterHorizontally)
                .clip(shape = RoundedCornerShape(30))
        ) {
            Text(stringResource(R.string.register))
        }

        Spacer(Modifier.size(30.dp))

        Text(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .clickable(
                interactionSource = MutableInteractionSource(), indication = rememberRipple(
                    bounded = true,
                    radius = 100.dp,
                    color = MaterialTheme.colors.primary
                )
            ) {
                viewModel.handleEvent(LoginScreenEvent.AlreadyHaveAccountClicked)
            }, text = stringResource(R.string.already_have_account)
        )

    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun Logo() {
    Column(modifier = Modifier.size(200.dp, 200.dp)) {
        Image(
            painterResource(id = R.drawable.logo), stringResource(R.string.foodninja_logo), modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(150.dp, 150.dp)
        )
        Image(
            painterResource(id = R.drawable.food_ninja), stringResource(R.string.foodninja), modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(100.dp, 20.dp)
        )
        Image(
            painterResource(id = R.drawable.motto), stringResource(R.string.foodninja_motto), modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(100.dp, 20.dp)
        )
    }
}
