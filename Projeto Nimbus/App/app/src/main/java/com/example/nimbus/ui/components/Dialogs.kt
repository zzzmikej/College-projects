package com.example.nimbus.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.nimbus.R
import com.example.nimbus.ui.theme.catamaranFontFamily
import com.example.nimbus.ui.theme.poppinsFontFamily

@Composable
fun DeleteDialog(
    onConfirmButton: () -> Unit = {},
    onDismissButton: () -> Unit = {},
    setShowDialog: (Boolean) -> Unit = {},
    athleteName: String
) {
    AlertDialog(
        onDismissRequest = { setShowDialog(false) },
        confirmButton = {
            TextButton(onClick = {onConfirmButton(); setShowDialog(false) }) {
                Text(
                    text = stringResource(id = R.string.delete),
                    fontFamily = catamaranFontFamily,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.orange_500),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissButton(); setShowDialog(false) }) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    fontFamily = catamaranFontFamily,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.orange_500),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.delete_athlete_title),
                fontFamily = catamaranFontFamily,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.orange_100)
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.delete_athlete_text, athleteName),
                fontFamily = poppinsFontFamily,
                color = colorResource(id = R.color.orange_100)
            )
        },
        containerColor = colorResource(id = R.color.gray_800),

    )
}

@Composable
fun InfoDialog(
    onConfirmButton: () -> Unit = {},
    onDismissButton: () -> Unit = {},
    setShowDialog: (Boolean) -> Unit = {},
    dialogText: String
) {
    AlertDialog(
        onDismissRequest = { setShowDialog(false) },
        confirmButton = {
            TextButton(onClick = { onConfirmButton(); setShowDialog(false) }) {
                Text(
                    text = "Fechar",
                    fontFamily = catamaranFontFamily,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.orange_500),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {},
        title = {
            Text(
                text = "Eventos",
                fontFamily = catamaranFontFamily,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.orange_100)
            )
        },
        text = {
            Text(
                text = dialogText,
                fontFamily = poppinsFontFamily,
                color = colorResource(id = R.color.orange_100)
            )
        },
        containerColor = colorResource(id = R.color.gray_800),
    )
}

@Composable
fun LogoutDialog(
    onConfirmButton: () -> Unit = {},
    onDismissButton: () -> Unit = {},
    setShowDialog: (Boolean) -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = { setShowDialog(false) },
        confirmButton = {
            TextButton(onClick = {onConfirmButton(); setShowDialog(false) }) {
                Text(
                    text = stringResource(id = R.string.logout),
                    fontFamily = catamaranFontFamily,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.orange_500),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissButton(); setShowDialog(false) }) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    fontFamily = catamaranFontFamily,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.orange_500),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.logout),
                fontFamily = catamaranFontFamily,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.orange_100)
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.confirm_logout),
                fontFamily = poppinsFontFamily,
                color = colorResource(id = R.color.orange_100)
            )
        },
        containerColor = colorResource(id = R.color.gray_800),

        )
}

@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    DeleteDialog(athleteName = "João pedro")
}