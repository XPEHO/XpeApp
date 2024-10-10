package com.xpeho.xpeapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.Home
import com.xpeho.xpeapp.ui.theme.XpeAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        checkPermission()

        // This is done to skip to the Home screen faster,
        // thus not forcing the user to wait for authentication.
        val connectedLastTime = runBlocking {
            XpeApp.appModule.datastorePref.getWasConnectedLastTime()
        }
        val startScreenFlow: MutableStateFlow<Screens> =
            MutableStateFlow(if(connectedLastTime) Screens.Home else Screens.Login)

        if (connectedLastTime) {
            CoroutineScope(Dispatchers.IO).launch {
                XpeApp.appModule.authenticationManager.restoreAuthStateFromStorage()
                if (!XpeApp.appModule.authenticationManager.isAuthValid()) {
                    XpeApp.appModule.authenticationManager.logout()
                    withContext(Dispatchers.Main) {
                        startScreenFlow.value = Screens.Login
                    }
                }
            }
        }

        setContent {
            XpeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = XpehoColors.BACKGROUND_COLOR
                ) {
                    val startScreen = startScreenFlow.collectAsStateWithLifecycle()
                    Home(startScreen.value)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        } else {
            Log.d("Permission", "Permission already granted")
        }
    }
}
