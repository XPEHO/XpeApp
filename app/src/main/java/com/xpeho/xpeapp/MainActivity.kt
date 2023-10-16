package com.xpeho.xpeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.xpeho.xpeapp.ui.presentation.Home
import com.xpeho.xpeapp.ui.theme.XpeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XpeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = colorResource(id = R.color.xpeho_background_color)
                ) {
                    Home()
                }
            }
        }
    }
}
