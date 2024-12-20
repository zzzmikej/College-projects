package com.example.nimbus.ui.components

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nimbus.R
import com.example.nimbus.ui.theme.poppinsFontFamily

@Composable
fun BottomNavigation(
    selectedPage: Int,
    onItemClick: (page: Int) -> Unit
) {
    val screen = when(selectedPage) {
        0 -> "Time"
        1 -> "Home"
        2 -> "Eventos"
        3 -> "Perfil"
        else -> "X"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.gray_700))
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .padding(horizontal = 32.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if(selectedPage == 0) {
            ActiveNavItem(
                iconRes = R.drawable.team_icon_active,
                iconDesc = R.string.team,
                screenName = screen
            )
        }
        else NavItem(
            iconRes = R.drawable.team_icon,
            iconDesc = R.string.team,
            onClick = { onItemClick(0) }
        )

        if(selectedPage == 1) {
            ActiveNavItem(
                iconRes = R.drawable.house_icon_active,
                iconDesc = R.string.home,
                screenName = screen
            )
        }
        else NavItem(
            iconRes = R.drawable.house_icon,
            iconDesc = R.string.home,
            onClick = { onItemClick(1) }
        )

        if(selectedPage == 2) {
            ActiveNavItem(
                iconRes = R.drawable.calender_icon_active,
                iconDesc = R.string.events,
                screenName = screen
            )
        }
        else NavItem(
            iconRes = R.drawable.calender_icon,
            iconDesc = R.string.events,
            onClick = { onItemClick(2) }
        )

        if(selectedPage == 3) {
            ActiveNavItem(
                iconRes = R.drawable.user_icon_active,
                iconDesc = R.string.account,
                screenName = screen
            )
        }
        else NavItem(
            iconRes = R.drawable.user_icon,
            iconDesc = R.string.account,
            onClick = { onItemClick(3) }
        )
    }
}

@Composable
fun NavItem(
    iconRes: Int,
    iconDesc: Int,
    onClick: () -> Unit
) {
    val interactionSource= remember { MutableInteractionSource() }

    Image(
        painter = painterResource(id = iconRes),
        contentDescription = stringResource(id = iconDesc),
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(35.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            },
    )
}

@Composable
fun ActiveNavItem(iconRes: Int, screenName: String, iconDesc: Int) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(99.237.dp))
            .background(
                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                    colors = listOf(
                        Color(0x42FF7425),
                        Color(0x12994516)
                    )
                )
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = stringResource(id = iconDesc),
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = screenName,
            color = colorResource(id = R.color.orange_500),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = poppinsFontFamily
        )
    }
}