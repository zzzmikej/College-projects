package com.example.nimbus.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nimbus.R
import com.example.nimbus.ui.components.Button
import com.example.nimbus.ui.components.CustomTextFieldPassword
import com.example.nimbus.ui.components.CustomTextFieldWithIcon
import com.example.nimbus.ui.theme.NimbusTheme
import com.example.nimbus.ui.viewmodels.LoginModelFactory
import com.example.nimbus.ui.viewmodels.LoginViewModel
import com.example.nimbus.utils.SharedPreferencesManager

class LoginScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {

            window.statusBarColor = getColor(R.color.gray_900)
            window.navigationBarColor = getColor(R.color.gray_900)

            NimbusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Login(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Login(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPrefManager = SharedPreferencesManager(context)

    val viewModel: LoginViewModel = viewModel(
        factory = LoginModelFactory(sharedPrefManager)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF131313))
            .padding(30.dp, 70.dp, 30.dp, 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = painterResource(id = R.mipmap.logo),
                contentDescription = stringResource(id = R.string.logo_desc),
                modifier = Modifier.size(110.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Continue para seu",
                color = Color.White,
                fontSize = 20.sp
            )

            Text(
                text = stringResource(id = R.string.login).toUpperCase(),
                color = Color(0xFFFF7425),
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.height(31.dp))

        LoginForm(context, viewModel)

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = stringResource(id = R.string.forget_password),
            color = Color.White,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(45.dp))

        Text(
            text = stringResource(id = R.string.no_account),
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.no_account_create),
            color = Color(0xFFFF7425),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable { context.startActivity(Intent(context, RegisterActivity::class.java)) }
        )
    }
}

@Composable
fun LoginForm(context: Context, viewModel: LoginViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomTextFieldWithIcon(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = stringResource(id = R.string.add_email),
            icon = R.drawable.envelope_icon,
            iconDescription = stringResource(id = R.string.email_icon_desc),
            placeholder = stringResource(id = R.string.email_placeholder)
        )

        Spacer(modifier = Modifier.height(30.dp))

        CustomTextFieldPassword(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = stringResource(id = R.string.add_email),
            placeholder = stringResource(id = R.string.password_placeholder)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            text = stringResource(id = R.string.login),
            fontSize = 24,
            onClick = { viewModel.login(context, null) }
        )
    }
}