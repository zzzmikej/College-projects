package com.example.nimbus.ui.components
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nimbus.R
import com.example.nimbus.domain.Game
import com.example.nimbus.domain.Team
import com.example.nimbus.dto.Game.GameWithTeamDTO
import com.example.nimbus.dto.Team.TeamTransferDTO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Event(
    type: String,
    team: TeamTransferDTO,
    game: GameWithTeamDTO
) {
    val icon = if(type == "Partida") R.drawable.match_icon else R.drawable.trainning_icon
    val iconDesc = if(type == "Partida") "Partida" else "Treino"

    val lineColor = if(game.confirmed) colorResource(id = R.color.green) else colorResource(id = R.color.orange_500)

    val homeTeam: String?
    val outTeam: String?

    if(game.challenger == team.id) { homeTeam = team.name; outTeam = game.adversaryName }
    else { homeTeam = game.adversaryName; outTeam = team.name }

    Row(
        modifier = Modifier
            .width(350.dp),
            //.padding(horizontal = 62.dp, vertical = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = iconDesc,
                modifier = Modifier.size(30.dp)
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(47.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(lineColor)
            )
            Column {
                Text(
                    text = homeTeam,
                    color = colorResource(id = R.color.orange_100),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(165.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = outTeam,
                    color = colorResource(id = R.color.gray_300),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(165.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

            val date = LocalDateTime.parse(game.inicialDateTime).format(dateFormatter)
            val time = LocalDateTime.parse(game.inicialDateTime).format(timeFormatter)

            Text(
                text = date,
                color = colorResource(id = R.color.orange_100),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = time,
                color = colorResource(id = R.color.orange_100),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}