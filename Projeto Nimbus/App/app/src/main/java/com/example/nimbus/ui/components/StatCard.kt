package com.example.nimbus.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nimbus.ui.theme.catamaranFontFamily
import com.example.nimbus.R
import com.example.nimbus.ui.theme.poppinsFontFamily
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatCard(
    label: String,
    value: String,
    subvalue: String? = "",
    modifier: Modifier = Modifier
) {
    Container(
        modifier = modifier.width(150.dp)
    ) {
        Column {
            Text(
                text = label,
                color = colorResource(id = R.color.orange_100),
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
            )

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = value,
                    fontSize = 50.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Black,
                    color = colorResource(id = R.color.orange_500),
                )
                if (subvalue != null) {
                    Text(
                        text = subvalue,
                        fontFamily = catamaranFontFamily,
                        color = colorResource(id = R.color.orange_500),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatCardPreview() {
    StatCard(
    "Vitórias",
    "32",
    )
}