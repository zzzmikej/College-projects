package com.example.nimbus.ui.screens.fragments.Athlete

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nimbus.R
import com.example.nimbus.domain.InjurySeverities
import com.example.nimbus.dto.Injury.InjuryGetDTO
import com.example.nimbus.ui.components.Button
import com.example.nimbus.ui.components.CustomTextField
import com.example.nimbus.ui.components.DatePickerDocked
import com.example.nimbus.ui.components.InjuryCard
import com.example.nimbus.ui.components.StatCard
import com.example.nimbus.ui.screens.fragments.drawFadingEdges
import com.example.nimbus.ui.theme.catamaranFontFamily
import com.example.nimbus.ui.theme.poppinsFontFamily
import com.example.nimbus.ui.viewmodels.AthleteInjuryScreenViewModel
import com.example.nimbus.ui.viewmodels.GlobalViewModel
import com.example.nimbus.utils.getDaysInjuried
import com.example.nimbus.utils.getLast6Months
import com.example.nimbus.utils.getSeverities
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AthleteInjuryScreen(
    viewModel: AthleteInjuryScreenViewModel,
    globalViewModel: GlobalViewModel,
    modifier: Modifier = Modifier
) {
    val globalUiState by globalViewModel.uiState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize(1f)
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val sheetState = rememberModalBottomSheetState()
        var isSheetOpen by rememberSaveable { mutableStateOf(false) }

        if(isSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { isSheetOpen = false },
                sheetState = sheetState,
                containerColor = Color(0xFF212121)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CustomTextField(
                        label = "Tipo da lesão",
                        value = uiState.typeText,
                        onValueChange = { viewModel.onTypeChange(it) },
                        placeholder = "Lesão muscular"
                    )

                    DatePickerDocked(
                        label = "Início",
                        fraction = 1f,
                        onValueChange = { newDate ->
                            viewModel.onStartDateChange(LocalDate.parse(newDate))
                        }
                    )

                    DatePickerDocked(
                        label = "Final",
                        onValueChange = { newDate ->
                            viewModel.onEndDateChange(LocalDate.parse(newDate))
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        text = "Cadastrar lesão",
                        fontSize = 20,
                        onClick = {
                            Log.i("Cadastro injury", "cadstrando")
                            viewModel.postInjury()
                        }
                    )
                }
            }
        }

        //grafico/kpi
        if(globalUiState.selectedAthlete?.injuries?.isNullOrEmpty() != true) {
            val severities = globalUiState.selectedAthlete!!.injuries!!.getSeverities()
            Log.i("Gravidade", "$severities")
            Column(
                Modifier.height(250.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GeneralInfo(globalUiState.selectedAthlete!!.injuries!!)

                Text(
                    text = "Gravidade das lesões",
                    fontFamily = poppinsFontFamily,
                    color = colorResource(id = R.color.gray_400),
                    fontWeight = FontWeight.Medium
                )

                InjurySeverity(severities)
            }
        }

        Log.i("Teste Athlete State", "${globalUiState.selectedAthlete}")
        if(globalUiState.selectedAthlete?.injuries?.isNullOrEmpty() != true) {
            ContentList(content = globalUiState.selectedAthlete?.injuries!!)
        } else {
            NoContent()
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            text = "Adicionar lesão",
            fontSize = 16,
            onClick = { isSheetOpen = true },
            modifier = Modifier.height(50.dp)
        )
    }
}

//arrumar os códigos
@Composable
fun ContentList(content: List<InjuryGetDTO>) {
    val scrollState = rememberLazyListState()
    Column() {

    }
    LazyColumn(
        modifier = Modifier
            .drawFadingEdges(scrollState)
            .height(330.dp)
            .padding(top = 18.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        state = scrollState
    ) {
        items(items = content.toList()) {
            InjuryCard(injury = it)
        }
    }
}

@Composable
fun NoContent() {
    Column(
        modifier = Modifier
            .height(505.dp)
            .padding(top = 18.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Não foram encontradas lesões",
            color = colorResource(id = R.color.gray_400),
            fontFamily = poppinsFontFamily
        )
    }
}

@Composable
fun GeneralInfo(injuries: List<InjuryGetDTO>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        color = colorResource(id = R.color.gray_700)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Lesões totais",
                    fontSize = 12.sp,
                    fontFamily = catamaranFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.orange_100)
                )
                Text(
                    text = injuries.size.toString(),
                    color = colorResource(id = R.color.orange_500),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = poppinsFontFamily
                )
            }

            Column {
                Text(
                    text = "Últimos 6 meses",
                    fontSize = 12.sp,
                    fontFamily = catamaranFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.orange_100)
                )
                Text(
                    text = injuries.getLast6Months().toString(),
                    color = colorResource(id = R.color.orange_500),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = poppinsFontFamily
                )
            }

            Column {
                Text(
                    text = "Total de dias fora",
                    fontSize = 12.sp,
                    fontFamily = catamaranFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.orange_100)
                )
                Text(
                    text = injuries.getDaysInjuried().toString(),
                    color = colorResource(id = R.color.orange_500),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = poppinsFontFamily
                )
            }
        }
    }
}

@Composable
fun InjurySeverity(severities: InjurySeverities) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatCard(
            label = "Leves",
            value = severities.minor.toString(),
            modifier = Modifier.width(100.dp)
        )

        StatCard(
            label = "Médias",
            value = severities.mid.toString(),
            modifier = Modifier.width(100.dp)
        )

        StatCard(
            label = "Graves",
            value = severities.severe.toString(),
            modifier = Modifier.width(100.dp)
        )
    }
}