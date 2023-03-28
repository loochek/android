package org.loochek.test

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val bgColor = Color(245, 245, 245)

            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(color = bgColor)

            val colors =
                lightColors(primary = Color(118, 220, 143), secondary = Color(118, 220, 143))
            MaterialTheme(colors = colors) {
                LoginScreen(bgColor)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(applicationContext, getString(R.string.welcome_back), Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(applicationContext, getString(R.string.goodbye), Toast.LENGTH_SHORT).show()
    }
}

@Composable
private fun LoginScreen(bgColor: Color) {
    val focusManager = LocalFocusManager.current
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) { focusManager.clearFocus() }, color = bgColor
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Logo()
            LoginSection()
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
private fun Logo() {
    Column(modifier = Modifier.size(200.dp, 200.dp)) {
        Image(
            painterResource(id = R.drawable.logo), null, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(150.dp, 150.dp)
        )
        Image(
            painterResource(id = R.drawable.food_ninja), null, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(100.dp, 20.dp)
        )
        Image(
            painterResource(id = R.drawable.motto), null, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(100.dp, 20.dp)
        )
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
private fun LoginSection() {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var keepSignIn by remember { mutableStateOf(false) }
    var subscribeToSpecDeals by remember { mutableStateOf(false) }

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

        TextField(value = name,
            modifier = textFieldModifier,
            label = { Text(stringResource(R.string.your_name)) },
            onValueChange = { name = it },
            singleLine = true,
            colors = textFieldColors,
            leadingIcon = { Icon(imageVector = Icons.Filled.AccountCircle, null) }
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextField(
            value = email,
            modifier = textFieldModifier,
            label = { Text(stringResource(R.string.email)) },
            onValueChange = { email = it },
            singleLine = true,
            colors = textFieldColors,
            leadingIcon = { Icon(imageVector = Icons.Filled.Email, null) }
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextField(
            value = password,
            modifier = textFieldModifier,
            label = { Text(stringResource(R.string.password)) },
            onValueChange = { password = it },
            singleLine = true,
            colors = textFieldColors,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image =
                    if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                // Please provide localized description for accessibility services
                val description = if (passwordVisible) stringResource(R.string.hide_password) else stringResource(
                                    R.string.show_password)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            leadingIcon = { Icon(imageVector = Icons.Filled.Lock, null) }
        )

        Spacer(modifier = Modifier.height(5.dp))

        LabelledCheckBox(
            checked = keepSignIn,
            onCheckedChange = { keepSignIn = it },
            stringResource(R.string.keep_sign_in),
            modifier = Modifier.padding(horizontal = 6.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))
        LabelledCheckBox(
            checked = subscribeToSpecDeals,
            onCheckedChange = { subscribeToSpecDeals = it },
            stringResource(R.string.email_special_pricing),
            modifier = Modifier.padding(horizontal = 6.dp)
        )

        Spacer(Modifier.size(30.dp))

        Button(
            onClick = { /*TODO*/ },
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
            ) {}, text = stringResource(R.string.already_have_account)
        )

    }
}

@Composable
fun LabelledCheckBox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(
                indication = rememberRipple(color = MaterialTheme.colors.primary),
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onCheckedChange(!checked) }
            )
            .requiredHeight(ButtonDefaults.MinHeight)
            .padding(4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null
        )

        Spacer(Modifier.size(6.dp))

        Text(
            text = label,
        )
    }
}

fun View.isKeyboardOpen(): Boolean {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect);
    val screenHeight = rootView.height
    val keypadHeight = screenHeight - rect.bottom;
    return keypadHeight > screenHeight * 0.15
}

@Composable
fun rememberIsKeyboardOpen(): State<Boolean> {
    val view = LocalView.current

    return produceState(initialValue = view.isKeyboardOpen()) {
        val viewTreeObserver = view.viewTreeObserver
        val listener = ViewTreeObserver.OnGlobalLayoutListener { value = view.isKeyboardOpen() }
        viewTreeObserver.addOnGlobalLayoutListener(listener)

        awaitDispose { viewTreeObserver.removeOnGlobalLayoutListener(listener) }
    }
}

fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {

    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }

    if (isFocused) {
        val isKeyboardOpen by rememberIsKeyboardOpen()

        val focusManager = LocalFocusManager.current
        LaunchedEffect(isKeyboardOpen) {
            if (isKeyboardOpen) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}