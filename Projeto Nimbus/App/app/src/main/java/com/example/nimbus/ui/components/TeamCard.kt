package com.example.nimbus.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nimbus.R
import com.example.nimbus.domain.Team
import com.example.nimbus.ui.theme.catamaranFontFamily
import com.example.nimbus.ui.theme.poppinsFontFamily

@Composable
fun TeamCard(
    team: Team,
    onClick: () -> Unit
) {
    val interactionSource= remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(colorResource(id = R.color.gray_700))
            .padding(16.dp, 20.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = team.picture ?: "https://media.istockphoto.com/id/1147544807/vector/thumbnail-image-vector-graphic.jpg?s=612x612&w=0&k=20&c=rnCKVbdxqkjlcs3xH87-9gocETqpspHFXu5dIGB4wuM=",
            contentDescription = stringResource(id = R.string.team_image_desc, team.name),
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterVertically)
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = team.name,
                    color = colorResource(id = R.color.orange_100),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = catamaranFontFamily,
                )

                Spacer(modifier = Modifier.width(14.dp))

                val badgeValue = when(team.level) {
                    1 -> R.drawable.badge_1
                    2 -> R.drawable.badge_2
                    3 -> R.drawable.badge_3
                    4 -> R.drawable.badge_4
                    else -> null
                }

                if(badgeValue != null) {
                    Image(
                        painter = painterResource(id = badgeValue),
                        contentDescription = "Icon",
                        modifier = Modifier.size(30.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            val athletesCount = team.athletes?.size ?: 0

            //Text(
            //    text = stringResource(id = R.string.players_amount, athletesCount),
            //    color = colorResource(id = R.color.gray_400),
            //    fontSize = 16.sp,
            //    fontWeight = FontWeight.Medium,
            //    fontFamily = poppinsFontFamily,
            //)
        }
    }
}
