package com.example.nimbus.ui.screens.fragments.Register

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nimbus.R
import com.example.nimbus.domain.Coach
import com.example.nimbus.dto.User.UserCreateDTO
import com.example.nimbus.ui.components.Button
import com.example.nimbus.ui.components.CustomTextField
import com.example.nimbus.ui.theme.NimbusTheme
import com.example.nimbus.ui.viewmodels.RegisterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun RegisterStepTwo(
    registerViewModel: RegisterViewModel,
    context: Context,
    onClickNextStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF131313))
            .padding(30.dp, 55.dp, 30.dp, 15.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = painterResource(id = R.mipmap.logo),
            contentDescription = stringResource(id = R.string.logo_desc),
            modifier = modifier.size(90.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.need_info),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )

        Text(
            text = stringResource(id = R.string.register).toUpperCase(),
            color = Color(0xFFFF7425),
            fontSize = 32.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        RegisterFormTwo(registerViewModel, context, onClickNextStep)
    }
}

@Composable
fun RegisterFormTwo(
    registerViewModel: RegisterViewModel,
    context: Context,
    onClickNextStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by registerViewModel.uiState.collectAsState()

    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = stringResource(id = R.string.email),
            placeholder = stringResource(id = R.string.email_placeholder)
        )

        Spacer(modifier = Modifier.height(30.dp))

        CustomTextField(
            value = phone,
            onValueChange = { phone = it },
            label = stringResource(id = R.string.phone),
            placeholder = stringResource(id = R.string.phone_placeholder)
        )

        Spacer(modifier = Modifier.height(30.dp))

        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(id = R.string.password),
            placeholder = stringResource(id = R.string.password_placeholder)
        )

        Spacer(modifier = Modifier.height(30.dp))

        CustomTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = stringResource(id = R.string.confirm_password),
            placeholder = stringResource(id = R.string.password_placeholder)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            text = stringResource(id = R.string.continue_),
            fontSize = 24,
            onClick = {
                val scope = CoroutineScope(Dispatchers.Main)

                val stateCoach = uiState.userData!!.coach

                val coach = Coach(
                    firstName = stateCoach!!.firstName,
                    lastName = stateCoach.lastName,
                    birthDate = stateCoach.birthDate,
                    phone = phone
                )

                val userData = UserCreateDTO(
                    email = email,
                    password = password,
                    coach = coach
                )

                registerViewModel.setUserData(userData)

                scope.launch {
                    val post = async { registerViewModel.postUser(userData) }
                    post.await()

                    val login = async { registerViewModel.login(context) }
                    login.await()

                    onClickNextStep()
                }
            }
        )
    }
}