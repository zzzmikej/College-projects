package com.example.nimbus.ui.screens.fragments.Athlete

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nimbus.R
import com.example.nimbus.ui.components.DeleteDialog
import com.example.nimbus.ui.theme.catamaranFontFamily
import com.example.nimbus.ui.viewmodels.AthleteInfoViewModel
import com.example.nimbus.ui.viewmodels.GlobalViewModel

@Composable
fun DottedLine() {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)) {
        val canvasWidth = size.width
        val dotWidth = 2.dp.toPx() // Largura do ponto
        val spaceWidth = 5.dp.toPx() // Espaçamento entre pontos

        var startX = 0f
        while (startX < canvasWidth) {
            drawCircle(
                color = Color(0xFFFFEAE0), // Cor dos pontos
                radius = dotWidth / 2,
                center = Offset(startX + dotWidth / 2, size.height / 2)
            )
            startX += dotWidth + spaceWidth
        }
    }
}

@Composable
fun InfoLine(
    label: String? = "N/A",
    info: String? = "N/A"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (label != null) {
            Text(
                text = label.toUpperCase(),
                fontWeight = FontWeight.Black,
                color = Color(0xFFFFEAE0),
                fontFamily = catamaranFontFamily,
                fontSize = 16.sp
            )
        }

        if (info != null) {
            Text(
                text = info,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFFEAE0),
                fontFamily = catamaranFontFamily,
                fontSize = 16.sp
            )
        }
    }
    DottedLine()
}

@Composable
fun PlayerInformation(
    viewModel: AthleteInfoViewModel,
    globalViewModel: GlobalViewModel,
    modifier: Modifier = Modifier,
    onPageClick: (athletePage: Int) -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val globalUiState by globalViewModel.uiState.collectAsState()
    val athlete = globalUiState.selectedAthlete
    val uiState by viewModel.uiState.collectAsState()
    var isDialogOpen by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize(1f)
            .padding(20.dp, 0.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val athleteName =
            "${athlete?.firstName} ${athlete?.lastName}"

        if (isDialogOpen) {
            DeleteDialog(
                athleteName = athleteName,
                onDismissButton = {
                    isDialogOpen = false
                },
                onConfirmButton = {
                    isDialogOpen = false
                    onPageClick(0)
                }
            )
        }

        Text(
            text = athleteName.toUpperCase(),
            fontWeight = FontWeight.Black,
            color = Color(0xFFFFEAE0),
            fontFamily = catamaranFontFamily,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Altura".toUpperCase(),
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFFEAE0),
                    fontFamily = catamaranFontFamily,
                    fontSize = 16.sp
                )
                Text(
                    text = "${athlete?.athleteDesc?.height}m".toUpperCase(),
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFFEAE0),
                    fontFamily = catamaranFontFamily,
                    fontSize = 16.sp
                )
            }
            Box(
                modifier = Modifier
                    .border(width = 3.dp, color = Color(0xFFff7425), shape = CircleShape)
                    .size(155.dp)
                    .padding(5.dp)
            ) {
                AsyncImage(
                    model = athlete?.picture,
                    contentDescription = "jogador",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(200.dp))
                        .border(width = 3.dp, color = Color(0xFF131313), shape = CircleShape)
                        .size(150.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Peso".toUpperCase(),
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFFEAE0),
                    fontFamily = catamaranFontFamily,
                    fontSize = 16.sp
                )
                Text(
                    text = "${athlete?.athleteDesc?.weight}kg".toUpperCase(),
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFFEAE0),
                    fontFamily = catamaranFontFamily,
                    fontSize = 16.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            val isStarting = if (athlete?.isStarting == true) "Sim" else "Não"

            InfoLine(label = stringResource(id = R.string.starting), info = isStarting)
            InfoLine(
                label = stringResource(id = R.string.position),
                info = athlete?.athleteDesc?.position
            )
            InfoLine(label = stringResource(id = R.string.age), info = athlete?.getIdade().toString())
            InfoLine(
                label = stringResource(id = R.string.category),
                info = athlete?.category
            )
            InfoLine(label = stringResource(id = R.string.birth_date_short), info = athlete?.getFormattedBirthDate())
            InfoLine(label = stringResource(id = R.string.phone) + " 1", info = athlete?.phone)
            InfoLine(
                label = stringResource(id = R.string.email),
                info = stringResource(id = R.string.email_placeholder)
            )
            InfoLine(
                label = stringResource(id = R.string.address),
                info = athlete?.athleteDesc?.address ?: "Indefinido"
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val interactionSource = remember { MutableInteractionSource() }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        isDialogOpen = true
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.thrash),
                        contentDescription = null,
                        Modifier.size(30.dp)
                    )
                    Text(
                        text = "Excluir",
                        color = colorResource(id = R.color.orange_100),
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onPageClick(3)
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bandaids_orange),
                        contentDescription = null,
                        Modifier.size(30.dp)
                    )
                    Text(
                        text = "Lesões",
                        color = colorResource(id = R.color.orange_100),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

    }
}