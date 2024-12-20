package com.example.nimbus.ui.screens

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nimbus.R
import com.example.nimbus.ui.screens.fragments.Register.RegisterStepOne
import com.example.nimbus.ui.screens.fragments.Register.RegisterStepThree
import com.example.nimbus.ui.screens.fragments.Register.RegisterStepTwo
import com.example.nimbus.ui.screens.ui.theme.NimbusTheme
import com.example.nimbus.ui.viewmodels.LoginModelFactory
import com.example.nimbus.ui.viewmodels.LoginViewModel
import com.example.nimbus.ui.viewmodels.RegisterModelFactory
import com.example.nimbus.ui.viewmodels.RegisterViewModel
import com.example.nimbus.utils.SharedPreferencesManager

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {

            window.statusBarColor = getColor(R.color.gray_900)
            window.navigationBarColor = getColor(R.color.gray_900)

            NimbusTheme {
                val context = LocalContext.current
                val sharedPrefManager = SharedPreferencesManager(context)

                val registerViewModel: RegisterViewModel = viewModel(
                    factory = RegisterModelFactory(sharedPrefManager)
                )

                Register(registerViewModel, context)
            }
        }
    }
}

@Composable
fun Register(registerViewModel: RegisterViewModel, context: Context): Unit {
    val uiState = registerViewModel.uiState.collectAsState()
    val step = uiState.value.step

    return when (step) {
        0 -> RegisterStepOne(registerViewModel, { registerViewModel.setStep(1) })
        1 -> RegisterStepTwo(registerViewModel, context, { registerViewModel.setStep(2) })
        2 -> RegisterStepThree(registerViewModel, {})
        else -> {}
    }
}