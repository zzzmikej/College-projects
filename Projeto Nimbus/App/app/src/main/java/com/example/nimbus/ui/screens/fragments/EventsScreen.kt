package com.example.nimbus.ui.screens.fragments

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.nimbus.R
import com.example.nimbus.ui.components.Button
import com.example.nimbus.ui.components.CustomTextField
import com.example.nimbus.ui.components.CustomTextFieldWithIcon
import com.example.nimbus.ui.components.DatePickerDocked
import com.example.nimbus.ui.components.DropdownTeams
import com.example.nimbus.ui.components.Event
import com.example.nimbus.ui.components.InfoDialog
import com.example.nimbus.ui.theme.NimbusTheme
import com.example.nimbus.ui.theme.catamaranFontFamily
import com.example.nimbus.ui.viewmodels.EventsViewModel
import com.example.nimbus.ui.viewmodels.GlobalViewModel
import com.example.nimbus.utils.sortByClosestDate
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Events(
    viewModel: EventsViewModel,
    globalViewModel: GlobalViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()
    val globalUiState = globalViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val sheetState = rememberModalBottomSheetState()
        var isSheetOpen by rememberSaveable { mutableStateOf(false) }
        var isDialogOpen by rememberSaveable { mutableStateOf(false) }

        if(isDialogOpen) {
            InfoDialog(dialogText = stringResource(id = R.string.event_color_info), onConfirmButton = { isDialogOpen = false })
        }

        if(isSheetOpen) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { isSheetOpen = false },
                containerColor = Color(0xFF212121)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Adicionar novo evento",
                        fontFamily = catamaranFontFamily,
                        color = Color(0xFFFFEAE0),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    var challengedTeam by remember { mutableStateOf("") }

                    CustomTextFieldWithIcon(
                        label = stringResource(id = R.string.challenged_team),
                        value = challengedTeam,
                        onValueChange = { challengedTeam = it },
                        placeholder = "Nome do Time FC...",
                        icon = R.drawable.select_team_icon,
                        iconDescription = "Escudo do time selecionado"
                    )

                    //DropdownTeams(
                    //    options = uiState.value.teams,
                    //    onOptionSelected = {  }
                    //)

                    CustomTextField(
                        label = stringResource(id = R.string.place),
                        value = uiState.value.local,
                        onValueChange = { viewModel.onLocalChange(it) },
                        placeholder = "Rua 123..."
                    )

                    DatePickerDocked(
                        label = stringResource(id = R.string.date),
                        onValueChange = { newDate ->
                            viewModel.onDateChange(LocalDate.parse(newDate))
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        CustomTextField(
                            label = stringResource(id = R.string.start),
                            value = uiState.value.startTime,
                            onValueChange = { viewModel.onChangeStartTime(it) },
                            placeholder = "12:00",
                            fraction = .5f
                        )

                        CustomTextField(
                            label = stringResource(id = R.string.end),
                            value = uiState.value.endTime,
                            onValueChange = { viewModel.onChangeEndTime(it) },
                            placeholder = "14:00",
                            fraction = 1f
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(text = "Cadastrar evento", fontSize = 20, onClick = {
                        viewModel.viewModelScope.launch {
                            viewModel.postGame()
                            isSheetOpen = false
                        }
                    })
                }
            }
        }
        
        val interactionSource = remember { MutableInteractionSource() }
        Text(
            text = "Eventos do mês ⓘ",
            fontFamily = catamaranFontFamily,
            color = Color(0xFFFFEAE0),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            )  {
                isDialogOpen = true
            }
        )

        val scrollState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 18.dp)
                .weight(weight = 1f)
                .drawFadingEdges(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            state = scrollState
        ) {
            items(items = uiState.value.games.toList().sortByClosestDate()) {
                Event(type = "Partida", team = globalUiState.value.selectedTeam!!, game = it)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.height(45.dp)
        ) {

            Text(
                text = "Novos jogos podem ser cadastrados apenas pelo nosso site.",
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.gray_400),
                fontFamily = catamaranFontFamily,
                fontWeight = FontWeight.Bold
            )
            //Button(
            //    text = "Adicionar evento",
            //    fontSize = 16,
            //    onClick = { isSheetOpen = true },
            //    modifier = Modifier.height(50.dp)
            //)
        }
    }
}