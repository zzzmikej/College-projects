package com.example.nimbus.ui.screens.fragments

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.nimbus.R
import com.example.nimbus.dto.Team.TeamTransferDTO
import com.example.nimbus.ui.components.AthleteCard
import com.example.nimbus.ui.components.CustomLoading
import com.example.nimbus.ui.components.CustomTextFieldWithIcon
import com.example.nimbus.ui.theme.NimbusTheme
import com.example.nimbus.ui.theme.poppinsFontFamily
import com.example.nimbus.ui.viewmodels.GlobalViewModel
import com.example.nimbus.ui.viewmodels.RosterScreenViewModel

@Composable
fun Roster(
    globalViewModel: GlobalViewModel,
    viewModel: RosterScreenViewModel,
    modifier: Modifier = Modifier,
    onAthleteClick: (athletePage: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val globalUiState by globalViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CustomTextFieldWithIcon(
            value = uiState.searchText,
            onValueChange = { viewModel.onSearchChange(it) },
            label = null,
            placeholder = stringResource(id = R.string.search_player),
            icon = R.drawable.search_icon,
            iconDescription = stringResource(id = R.string.search_icon_desc)
        )

        if(uiState.isLoading) {
            CustomLoading()
        } else {
            val scrollState = rememberLazyListState()
            if(viewModel.filteredAthletes.toList().isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(505.dp)
                        .padding(top = 18.dp)
                        .weight(weight = 1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Você não possui jogadores associados ao seu time",
                        color = colorResource(id = R.color.gray_400),
                        fontFamily = poppinsFontFamily,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(505.dp)
                        .padding(top = 18.dp)
                        .weight(weight = 1f)
                        .drawFadingEdges(scrollState),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    state = scrollState
                ) {
                    Log.i("RosterScreen", "${viewModel.filteredAthletes}")
                    items(items = viewModel.filteredAthletes.toList().chunked(2)) { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            rowItems.forEach {
                                AthleteCard(
                                    it
                                ) {
                                    onAthleteClick(1)
                                    globalViewModel.selectAthlete(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Modifier.drawFadingEdges(
    scrollableState: ScrollableState,
    topEdgeHeight: Dp = 18.dp,
    bottomEdgeHeight: Dp = 18.dp,
) = then(
    Modifier
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()

            val topEdgeHeightPx = topEdgeHeight.toPx()
            val bottomEdgeHeightPx = bottomEdgeHeight.toPx()

            if (scrollableState.canScrollBackward && topEdgeHeightPx >= 1f) {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 0f,
                        endY = topEdgeHeightPx,
                    ),
                    blendMode = BlendMode.DstIn,
                )
            }

            if (scrollableState.canScrollForward && bottomEdgeHeightPx >= 1f) {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black, Color.Transparent),
                        startY = size.height - bottomEdgeHeightPx,
                        endY = size.height,
                    ),
                    blendMode = BlendMode.DstIn,
                )
            }
        }
)

fun Modifier.drawFadingEdgesHorizontal(
    scrollableState: ScrollableState,
    startEdgeWidth: Dp = 18.dp,
    endEdgeWidth: Dp = 18.dp,
) = then(
    Modifier
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()

            val startEdgeWidthPx = startEdgeWidth.toPx()
            val endEdgeWidthPx = endEdgeWidth.toPx()

            if (scrollableState.canScrollBackward && startEdgeWidthPx >= 1f) {
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startX = 0f,
                        endX = startEdgeWidthPx,
                    ),
                    blendMode = BlendMode.DstIn,
                )
            }

            if (scrollableState.canScrollForward && endEdgeWidthPx >= 1f) {
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Black, Color.Transparent),
                        startX = size.width - endEdgeWidthPx,
                        endX = size.width,
                    ),
                    blendMode = BlendMode.DstIn,
                )
            }
        }
)
