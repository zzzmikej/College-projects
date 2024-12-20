package com.example.nimbus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nimbus.R
import com.example.nimbus.ui.theme.catamaranFontFamily

@Composable
fun Container(
    title: String? = null,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    color: Color = colorResource(id = R.color.gray_700),
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp)),
        color = color
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            if(title != null) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.orange_100),
                    fontWeight = FontWeight.Bold,
                    fontFamily = catamaranFontFamily
                )

                Spacer(modifier = Modifier.height(5.dp))

                if (subtitle != null) {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        color = Color(0xFFBDBDBD),
                        fontWeight = FontWeight.Medium,
                        fontFamily = catamaranFontFamily
                    )

                    Spacer(modifier = Modifier.height(5.dp))
                }
            }

            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContainerPreview() {
    Column {
        Container(
            title = "Titulo",
            subtitle = "Subtitulo"
        ) {
            Text(text = "alo")
        }
    }
}