package com.example.nimbus.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nimbus.R
import com.example.nimbus.ui.components.Button
import com.example.nimbus.ui.theme.NimbusTheme
import com.example.nimbus.ui.theme.catamaranFontFamily

class OnboardingScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        setContent {

            window.statusBarColor = getColor(R.color.gray_900)
            window.navigationBarColor = getColor(R.color.gray_900)

            NimbusTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OnboardingScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

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

        Spacer(modifier = modifier.height(80.dp))

        Text(
            text = stringResource(id = R.string.slogan_half1).toUpperCase(),
            color = Color.White,
            fontSize = 24.sp,
            fontFamily = catamaranFontFamily,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = stringResource(id = R.string.slogan_half2).toUpperCase(),
            color = Color.White,
            fontSize = 24.sp,
            fontFamily = catamaranFontFamily,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = modifier.height(40.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp, 0.dp, 25.dp, 0.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            Button(
                text = stringResource(id = R.string.create_account),
                fontSize = 24,
                onClick = { context.startActivity(Intent(context, RegisterActivity::class.java)) }
            )
        }

        Spacer(modifier = modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.login),
            color = Color.White,
            fontSize = 24.sp,
            fontFamily = catamaranFontFamily,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.clickable { context.startActivity(Intent(context, LoginScreen::class.java)) }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnboardingScreenPreview2() {
    NimbusTheme {
        OnboardingScreen()
    }
}