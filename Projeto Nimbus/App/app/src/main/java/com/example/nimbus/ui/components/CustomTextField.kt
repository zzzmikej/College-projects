package com.example.nimbus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nimbus.R
import com.example.nimbus.domain.Team
import com.example.nimbus.ui.theme.NimbusTheme
import com.example.nimbus.ui.theme.catamaranFontFamily
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun colors(): TextFieldColors {
    val colors = TextFieldDefaults.colors(
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = colorResource(id = R.color.orange_500),
        unfocusedContainerColor = colorResource(id = R.color.gray_800),
        unfocusedPlaceholderColor = colorResource(id = R.color.gray_placeholder),
        unfocusedLeadingIconColor = colorResource(id = R.color.gray_placeholder),
        unfocusedTrailingIconColor = colorResource(id = R.color.gray_placeholder),
        focusedContainerColor = colorResource(id = R.color.gray_800),
        focusedPlaceholderColor = colorResource(id = R.color.gray_placeholder),
        focusedLeadingIconColor = colorResource(id = R.color.gray_placeholder),
        focusedTrailingIconColor = colorResource(id = R.color.gray_placeholder),
    )

    return colors
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String,
    fraction: Float = 1f,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier,
    traillingIcon: Int? = null,
    iconDescription: String? = null
) {
    Column {
        if(label != null) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        if (value != null) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(text = placeholder) },
                modifier = modifier
                    .fillMaxWidth(fraction)
                    .padding(top = 4.dp)
                    .border(
                        BorderStroke(1.dp, colorResource(id = R.color.gray_500)),
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = colors(),
                readOnly = readOnly,
                trailingIcon = {
                    traillingIcon?.let { painterResource(id = it) }?.let {
                        Icon(
                            painter = it,
                            contentDescription = iconDescription,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun CustomTextFieldWithIcon(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String,
    icon: Int,
    iconDescription: String
) {
    Column {
        if(label != null) {
            Text(
                text = label,
                color = Color(0xFFFFEAE0),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .border(
                    BorderStroke(1.dp, colorResource(id = R.color.gray_500)),
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = colors(),
            leadingIcon = {
                icon?.let {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = iconDescription,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        )
    }
}

@Composable
fun CustomTextFieldPassword(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    onTrailingIconClick: (() -> Unit)? = null,
    isPasswordVisible: Boolean = false,
    onPassVisibilityChange: (Boolean) -> Unit = {}

) {
    Column {
        Text(
            text = label,
            color = Color(0xFFFFEAE0),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = catamaranFontFamily,
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .border(
                    BorderStroke(1.dp, colorResource(id = R.color.gray_500)),
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.lock_icon),
                    contentDescription = stringResource(id = R.string.lock_icon_desc),
                    modifier = Modifier.size(25.dp)
                )
            },
            colors = colors(),
            trailingIcon = {
                IconButton(onClick = {
                    onTrailingIconClick?.invoke()
                    onPassVisibilityChange(!isPasswordVisible)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.eye_icon),
                        contentDescription = stringResource(id = R.string.eye_icon_desc),
                        modifier = Modifier.size(25.dp)
                    )
                }
            },
            visualTransformation = if (!isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        )
    }
}

fun Long.toBrazilianDateFormat(
    pattern: String = "dd/MM/yyyy"
): String {
    val date = Date(this)
    val formatter = SimpleDateFormat(
        pattern, Locale("pt-br")
    ).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }
    return formatter.format(date)
}

fun Long.toFormattedDateString(pattern: String = "yyyy-MM-dd"): String {
    val date = Date(this)
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(date)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(
    label: String,
    fraction: Float = 1f,
    onValueChange: (String?) -> Unit?
) {
    val focusManager = LocalFocusManager.current
    var showDatePickerDialog by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember {
        mutableStateOf("")
    }
    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, )
                ) {
                    Button(
                        text = stringResource(id = R.string.pick_date),
                        fontSize = 20,
                        onClick = {
                            val formattedDate = datePickerState.selectedDateMillis?.toFormattedDateString()

                            onValueChange(formattedDate)

                            datePickerState
                                .selectedDateMillis?.let { millis ->
                                    selectedDate = millis.toBrazilianDateFormat()
                                }
                            showDatePickerDialog = false
                        }
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = colorResource(id = R.color.gray_900)
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = colorResource(id = R.color.orange_500),
                    todayDateBorderColor = colorResource(id = R.color.orange_500),
                    titleContentColor = colorResource(id = R.color.orange_500),
                    currentYearContentColor = colorResource(id = R.color.orange_500),
                    selectedDayContentColor = colorResource(id = R.color.orange_100),
                    dayContentColor = colorResource(id = R.color.orange_100),
                    headlineContentColor = colorResource(id = R.color.orange_100),
                    navigationContentColor = colorResource(id = R.color.orange_100),
                    weekdayContentColor = colorResource(id = R.color.gray_placeholder),
                    todayContentColor = colorResource(id = R.color.orange_100),
                    yearContentColor = colorResource(id = R.color.orange_100),
                    selectedYearContainerColor = colorResource(id = R.color.orange_500),
                    selectedYearContentColor = colorResource(id = R.color.orange_100),
                )
            )
        }
    }
    CustomTextField(
        label = label,
        value = selectedDate,
        onValueChange = { },
        placeholder = "dd/mm/aaaa",
        readOnly = true,
        traillingIcon = R.drawable.calender_icon,
        fraction = fraction,
        modifier = Modifier
            .onFocusEvent {
                if (it.isFocused) {
                    showDatePickerDialog = true
                    focusManager.clearFocus(force = true)
                }
            },
    )
}

@Preview(showBackground = true)
@Composable
fun TextFieldsPreview() {
    NimbusTheme {
        Column {
            var value1 by remember { mutableStateOf("") }
            var value2 by remember { mutableStateOf("") }
            var value3 by remember { mutableStateOf("") }

            CustomTextField(
                value = value1,
                onValueChange = { value1 = it },
                label = "Label",
                placeholder = "Placeholder"
            )

            CustomTextFieldWithIcon(
                value = value2,
                onValueChange = { value2 = it },
                label = "Label",
                placeholder = "Placeholder",
                icon = R.drawable.envelope_icon,
                iconDescription = stringResource(id = R.string.email_icon_desc),
            )

            CustomTextFieldPassword(
                value = value3,
                onValueChange = { value3 = it },
                label = "Label",
                placeholder = "Placeholder"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomTextField(
                    value = value1,
                    onValueChange = { value1 = it },
                    label = "Label",
                    placeholder = "Placeholder",
                    fraction = .5f
                )

                CustomTextField(
                    value = value1,
                    onValueChange = { value1 = it },
                    label = "Label",
                    placeholder = "Placeholder",
                    fraction = 1f
                )
            }
        }
    }
}

@Composable
fun DropdownTeams(
    options: List<Team>,
    onOptionSelected: (Team) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    var selectedOption by remember {
        mutableStateOf("")
    }

    Column {
        CustomTextField(
            value = selectedOption,
            onValueChange = {},
            placeholder = "Selecione um time",
            readOnly = true,
            modifier = Modifier.clickable { expanded = !expanded }
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.name) },
                    onClick = {
                        selectedOption = it.name
                        expanded = false
                        onOptionSelected(it)
                    }
                )
            }
        }
    }
}