package com.xpeho.xpeapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.Home
import com.xpeho.xpeapp.ui.notifications.AlarmScheduler
import com.xpeho.xpeapp.ui.theme.XpeAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Schedule the alarm if the permission is granted
            scheduleNotificationAlarm()
        } else {
            Log.d("Permission", "Permission denied")
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        // Check if the app has the necessary notifications permissions.
        checkPermissions()

        // This is done to skip to the Home screen faster,
        // thus not forcing the user to wait for authentication.
        val connectedLastTime = runBlocking {
            XpeApp.appModule.datastorePref.getWasConnectedLastTime()
        }
        val startScreenFlow: MutableStateFlow<Screens> =
            MutableStateFlow(if (connectedLastTime) Screens.Home else Screens.Login)

        // If the user was connected last time, try to restore the authentication state.
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
    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this@MainActivity,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("Permission", "Permission already granted")
                scheduleNotificationAlarm()
            }

            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun scheduleNotificationAlarm() {
        val alarmScheduler = AlarmScheduler()
        alarmScheduler.scheduleAlarm(this)
    }
}
