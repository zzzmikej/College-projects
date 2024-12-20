package com.example.nimbus.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nimbus.R
import com.example.nimbus.ui.theme.catamaranFontFamily

@Composable
fun Button(
    text: String,
    fontSize: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange_500)),
        shape = RoundedCornerShape(11.dp)
    ) {
        Text(
            text = text,
            color = colorResource(id = R.color.orange_100),
            fontSize = fontSize.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = catamaranFontFamily,
        )
    }
}