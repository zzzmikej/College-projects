package com.example.nimbus.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nimbus.ui.screens.fragments.Register.RegisterStepThree
import com.example.nimbus.ui.screens.ui.theme.NimbusTheme
import com.example.nimbus.ui.viewmodels.RegisterModelFactory
import com.example.nimbus.ui.viewmodels.RegisterViewModel
import com.example.nimbus.utils.SharedPreferencesManager

class TeamRegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NimbusTheme {
                val context = LocalContext.current
                val sharedPrefManager = SharedPreferencesManager(context)

                val registerViewModel: RegisterViewModel = viewModel(
                    factory = RegisterModelFactory(sharedPrefManager)
                )
                RegisterStepThree(registerViewModel, { /*TODO*/ })
            }
        }
    }
}