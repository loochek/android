package org.loochek.test

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.loochek.test.catalogscreen.CatalogScreen
import javax.inject.Inject

@AndroidEntryPoint
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
                CatalogScreen(bgColor)
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