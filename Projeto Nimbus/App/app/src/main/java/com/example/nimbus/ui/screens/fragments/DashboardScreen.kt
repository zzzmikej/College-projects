package com.example.nimbus.ui.screens.fragments

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nimbus.R
import com.example.nimbus.api.game1
import com.example.nimbus.domain.StatItem
import com.example.nimbus.ui.components.getBadge
import com.example.nimbus.ui.components.Container
import com.example.nimbus.ui.components.MatchCard
import com.example.nimbus.ui.components.PendingResult
import com.example.nimbus.ui.components.StatCard
import com.example.nimbus.ui.screens.PredictionAIActivity
import com.example.nimbus.ui.theme.catamaranFontFamily
import com.example.nimbus.ui.theme.poppinsFontFamily
import com.example.nimbus.ui.viewmodels.DashboardViewModel
import com.example.nimbus.ui.viewmodels.GlobalViewModel

@Composable
fun Dashboard(
    viewModel: DashboardViewModel,
    globalViewModel: GlobalViewModel,
    context: Context,
    modifier: Modifier = Modifier,
) {
    val uiState = viewModel.uiState.collectAsState()
    val team = globalViewModel.getSelectedTeam()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (team != null) {
                AsyncImage(
                    model = team.picture,
                    contentDescription = stringResource(id = R.string.challenger_logo, team.name),
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )
            }
            
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                if (team != null) {
                    Text(
                        text = team.name,
                        fontSize = 22.sp,
                        color = colorResource(id = R.color.orange_100),
                        fontWeight = FontWeight.Bold,
                        fontFamily = catamaranFontFamily
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val badge = team?.let { getBadge(it.level) }
                    if (badge != null) {
                        Text(
                            text = badge.text,
                            color = colorResource(id = R.color.orange_100),
                            fontFamily = catamaranFontFamily
                        )

                        Image(
                            painter = painterResource(id = badge.icon),
                            contentDescription = badge.text,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
        
        if(uiState.value.isLoading) {
            Text(text = "Carregando...", color = colorResource(id = R.color.orange_500))
        }
        else {
            val scrollState = rememberLazyListState()
            Row(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "Estatísticas",
                        fontFamily = poppinsFontFamily,
                        color = colorResource(id = R.color.gray_400),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        state = scrollState,
                        modifier = Modifier.drawFadingEdgesHorizontal(scrollState)
                    ) {
                        items(items = uiState.value.statsItem.toList()) {
                            StatCard(label = it.label, value = it.value)
                        }
                    }
                }
            }

            if(uiState.value.nextGame != null) {
                MatchCard(uiState.value.nextGame!!)
            }

            uiState.value.lastGame?.let {
                PendingResult(
                    lastGame = it,
                    onConfirmClick = { /*TODO*/ },
                    onDismissClick = { /*TODO*/ },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //Container(
                //    modifier = Modifier
                //        .width(160.dp)
                //        .height(80.dp)
                //        .clip(RoundedCornerShape(20.dp)),
                //    color = colorResource(id = R.color.orange_500)
                //) {
                //    Column(
                //        modifier = Modifier.fillMaxSize(),
                //        verticalArrangement = Arrangement.Center,
                //        horizontalAlignment = Alignment.CenterHorizontally
                //    ) {
                //        Icon(
                //            painter = painterResource(id = R.drawable.basketball),
                //            contentDescription = null,
                //            tint = colorResource(id = R.color.orange_100)
                //        )
                //        Text(
                //            text = "Jogar partida",
                //            color = colorResource(id = R.color.orange_100),
                //            fontFamily = catamaranFontFamily,
                //            fontWeight = FontWeight.Bold
                //        )
                //    }
                //}

                Container(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    color = colorResource(id = R.color.orange_500)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.robot),
                            contentDescription = null,
                            tint = colorResource(id = R.color.orange_100)
                        )

                        val interactionSource = remember { MutableInteractionSource() }
                        Text(
                            text = "Obter previsão de jogo feita por IA",
                            color = colorResource(id = R.color.orange_100),
                            fontFamily = catamaranFontFamily,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null
                            )  {
                                context.startActivity(Intent(context, PredictionAIActivity::class.java))
                            }
                        )
                    }
                }
        }
        }
    }
}