package com.example.nimbus.ui.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nimbus.R
import com.example.nimbus.ui.theme.NimbusTheme
import com.example.nimbus.utils.SharedPreferencesManager

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            window.statusBarColor = getColor(R.color.gray_900)
            window.navigationBarColor = getColor(R.color.gray_900)

            val sharedPrefManager = SharedPreferencesManager(this)
            val isUserLoggedIn = !sharedPrefManager.getAuthToken().isNullOrEmpty()

            val destinationClass = if (isUserLoggedIn) MyTeamsScreen::class.java else OnboardingScreen::class.java

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, destinationClass)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }, 1000)

            NimbusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SplashScreen(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SplashScreen(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF131313))
            .padding(30.dp, 200.dp, 30.dp, 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.mipmap.logo),
            contentDescription = stringResource(id = R.string.logo_desc),
            modifier = modifier.size(130.dp)
        )

        Spacer(modifier = modifier.height(250.dp))

        Image(
            painter = painterResource(id = R.mipmap.dev_by_logo),
            contentDescription = stringResource(id = R.string.text_logo_desc),
            modifier = modifier.size(180.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    NimbusTheme {
        SplashScreen("Android")
    }
}