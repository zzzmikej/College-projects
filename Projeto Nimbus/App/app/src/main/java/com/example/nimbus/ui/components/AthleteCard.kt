package com.example.nimbus.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nimbus.R
import com.example.nimbus.domain.Athlete
import com.example.nimbus.ui.theme.catamaranFontFamily
import com.example.nimbus.ui.theme.poppinsFontFamily

@Composable
fun AthleteCard(
    athlete: Athlete,
    onAthleteClick: () -> Unit
) {
    val interactionSource= remember { MutableInteractionSource() }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.gray_700),
        ),
        modifier = Modifier
            .size(width = 165.dp, height = 220.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onAthleteClick()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            AsyncImage(
                model = athlete.picture ?: "https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png?20150327203541",
                contentDescription = stringResource(id = R.string.player_image, "${athlete.firstName} ${athlete.lastName}"),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .size(150.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${athlete.firstName} ${athlete.lastName}",
                color = colorResource(id = R.color.orange_100),
                fontWeight = FontWeight.Bold,
                fontFamily = catamaranFontFamily,
                maxLines = 1
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                athlete.athleteDesc?.let {
                    Text(
                        fontSize = 12.sp,
                        text = it.position,
                        color = colorResource(id = R.color.gray_200),
                        fontFamily = poppinsFontFamily
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    var starting: Int = R.drawable.star
                    var startingDesc: Int = R.string.not_starting_icon
                    var injury: Int = R.drawable.injury
                    var injuryDesc: Int = R.string.not_injuried_icon

                    if(athlete.isStarting) {
                        starting = R.drawable.star_filled
                        startingDesc = R.string.is_starting_icon
                    }


                    if(athlete.isInjuried) {
                        injury = R.drawable.injury_filled
                        injuryDesc = R.string.is_injuried_icon
                    }

                    Image(
                        painter = painterResource(id = starting),
                        contentDescription = stringResource(id = startingDesc),
                        modifier = Modifier.size(20.dp)
                    )

                    Image(
                        painter = painterResource(id = injury),
                        contentDescription = stringResource(id = injuryDesc),
                        modifier = Modifier.size(20.dp)
                    )

                }
            }
        }
    }
}
