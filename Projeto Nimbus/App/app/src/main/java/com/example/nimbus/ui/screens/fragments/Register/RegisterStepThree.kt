package com.example.nimbus.ui.screens.fragments.Register

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nimbus.R
import com.example.nimbus.domain.Coach
import com.example.nimbus.dto.Team.TeamCreateDTO
import com.example.nimbus.ui.components.Button
import com.example.nimbus.ui.components.CustomTextField
import com.example.nimbus.ui.screens.MyTeamsScreen
import com.example.nimbus.ui.viewmodels.RegisterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun RegisterStepThree(
    registerViewModel: RegisterViewModel,
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
            text = stringResource(id = R.string.team_register_text),
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

        RegisterFormThree(registerViewModel, onClickNextStep)
    }
}

@Composable
fun RegisterFormThree(
    registerViewModel: RegisterViewModel,
    onClickNextStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by registerViewModel.uiState.collectAsState()
    val context = LocalContext.current

    var teamName by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomTextField(
            value = teamName,
            onValueChange = { teamName = it },
            label = stringResource(id = R.string.team_name),
            placeholder = stringResource(id = R.string.team_name_placeholder)
        )

        Spacer(modifier = Modifier.height(30.dp))

        CustomTextField(
            value = category,
            onValueChange = { category = it },
            label = stringResource(id = R.string.category),
            placeholder = stringResource(id = R.string.category_placeholder)
        )

        Spacer(modifier = Modifier.height(30.dp))

        CustomTextField(
            value = address,
            onValueChange = { address = it },
            label = stringResource(id = R.string.address),
            placeholder = stringResource(id = R.string.address_placeholder)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            text = stringResource(id = R.string.continue_),
            fontSize = 24,
            onClick = {
                val scope = CoroutineScope(Dispatchers.Main)

                val coach = Coach(id = uiState.user!!.personaId)

                val teamCreate = TeamCreateDTO(
                    name = teamName,
                    category = category,
                    local = address,
                    coach
                )

                scope.launch {
                    val post = async { registerViewModel.postTeam(teamCreate) }
                    post.await()

                    context.startActivity(Intent(context, MyTeamsScreen::class.java))
                }
            }
        )
    }
}