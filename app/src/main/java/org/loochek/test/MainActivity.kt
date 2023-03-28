package org.loochek.test

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.loochek.test.loginscreen.LoginScreen

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
                LoginScreen(bgColor, this)
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