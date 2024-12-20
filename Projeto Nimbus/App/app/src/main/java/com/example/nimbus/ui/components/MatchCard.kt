package com.example.nimbus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nimbus.R
import com.example.nimbus.dto.Game.NextGameDTO
import com.example.nimbus.ui.theme.catamaranFontFamily
import com.example.nimbus.ui.theme.poppinsFontFamily
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MatchCard(
    lastGame: NextGameDTO
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")
    val date = LocalDateTime.parse(lastGame.date).format(formatter)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        color = colorResource(id = R.color.gray_700)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp, 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = lastGame.adversaryPicture,
                contentDescription = stringResource(id = R.string.challenger_logo, lastGame.adversaryName),
                modifier = Modifier.size(70.dp),
                contentScale = ContentScale.Fit
            )
            Column {


                Column {
                    Text(
                        text = "Próxima partida contra...",
                        color = colorResource(id = R.color.orange_100),
                        fontSize = 13.sp,
                        fontFamily = poppinsFontFamily
                    )

                    Text(
                        text = lastGame.adversaryName,
                        color = colorResource(id = R.color.orange_500),
                        fontFamily = catamaranFontFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 20.sp
                    )

                    Text(
                        text = date,
                        color = colorResource(id = R.color.gray_placeholder),
                        lineHeight = 8.sp,
                        fontFamily = poppinsFontFamily,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchCardPrewiew() {
    val lastGame = NextGameDTO(
        "Corinthians",
        "https://logodownload.org/wp-content/uploads/2016/11/Corinthians-logo-escudo.png",
        "10/09/2024 - 20:30"
    )

    Column {
        MatchCard(lastGame)
    }
}