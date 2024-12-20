package com.example.nimbus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nimbus.domain.Injury
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.nimbus.R
import com.example.nimbus.dto.Injury.InjuryGetDTO
import com.example.nimbus.ui.theme.catamaranFontFamily
import java.time.Duration
import java.util.UUID

@Composable
fun InjuryCard(
    injury: InjuryGetDTO
) {
    val initialDateUnformatted = LocalDate.parse(injury.initialDate)
    val finalDateUnformatted = LocalDate.parse(injury.finalDate)

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val initialDate = initialDateUnformatted.format(formatter)
    val finalDate = finalDateUnformatted.format(formatter)
    val dayDiff = Duration.between(initialDateUnformatted.atStartOfDay(), finalDateUnformatted.atStartOfDay()).toDays()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(id = R.color.gray_700))
            .padding(16.dp, 8.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(.5f),
            //horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = injury.type,
                color = colorResource(id = R.color.orange_500),
                fontWeight = FontWeight.Bold,
                fontFamily = catamaranFontFamily,
                fontSize = 16.sp
            )
        }

        Column(
            //horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //label
            Text(
                text = initialDate,
                color = colorResource(id = R.color.orange_100),
                fontWeight = FontWeight.Bold,
            )

            //value
            Text(
                text = "$dayDiff dia(s) fora",
                color = colorResource(id = R.color.orange_100),
                fontWeight = FontWeight.Bold,
                fontFamily = catamaranFontFamily,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InjuryCardPreview() {
    val example = InjuryGetDTO(UUID.randomUUID(), "Lesão muscular", "2024-10-01", "2024-12-17")

    InjuryCard(injury = example)
}