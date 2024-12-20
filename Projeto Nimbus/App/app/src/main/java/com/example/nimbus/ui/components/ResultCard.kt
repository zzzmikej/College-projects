package com.example.nimbus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nimbus.R
import com.example.nimbus.domain.GameResult
import com.example.nimbus.ui.theme.catamaranFontFamily

@Composable
fun ResultCard(
    gameResult: GameResult,
    fontSize: Int = 12,
    modifier: Modifier = Modifier
) {
    var result =
        if(gameResult.chalengedPoints > gameResult.challengerPoints) "Vitória" else
            if(gameResult.chalengedPoints == gameResult.challengerPoints) "Empate" else "Derrota"

    var backgroundColor = when(result) {
        "Vitória" -> colorResource(id = R.color.green)
        "Derrota" -> colorResource(id = R.color.red)
        else -> colorResource(id = R.color.orange_500)
    }
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp)),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = result,
                color = colorResource(id = R.color.orange_100),
                fontFamily = catamaranFontFamily,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultCardPreview() {
    val derrota = GameResult("1", 50, 40, true)
    val vitoria = GameResult("1", 40, 50, true)
    val empate = GameResult("1", 50, 50, true)

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ResultCard(derrota)
        ResultCard(vitoria)
        ResultCard(empate)
    }
}

