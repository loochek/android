package org.loochek.test

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.loochek.test.catalogscreen.CatalogScreen
import org.loochek.test.catalogscreen.CatalogScreenViewModel
import org.loochek.test.detailscreen.DetailScreen
import org.loochek.test.detailscreen.DetailScreenViewModel
import org.loochek.test.loginscreen.LoginScreen
import org.loochek.test.loginscreen.LoginScreenViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val bgColor = Color(245, 245, 245)

            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(color = bgColor)

            val navController = rememberNavController()

            val colors =
                lightColors(primary = Color(118, 220, 143), secondary = Color(118, 220, 143))
            MaterialTheme(colors = colors) {
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        val viewModel = hiltViewModel<LoginScreenViewModel>()
                        LoginScreen(bgColor, viewModel, navController)
                    }

                    composable("catalog") {
                        val viewModel = hiltViewModel<CatalogScreenViewModel>()
                        CatalogScreen(bgColor, viewModel, navController)
                    }

                    composable("detail/{id}/{placement}") { backStackEntry ->
                        val viewModel = hiltViewModel<DetailScreenViewModel>()
                        DetailScreen(
                            bgColor,
                            viewModel,
                            backStackEntry.arguments?.getString("id")!!.toInt(),
                            backStackEntry.arguments?.getString("placement")!!
                        )
                    }
                }
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