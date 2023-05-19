package com.example.timetable_application.ui.screen.timetable.pickers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.timetable_application.R
import com.example.timetable_application.entity.TemplateColors
import com.github.skydoves.colorpicker.compose.*
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.color.ColorDialog
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.models.ColorSelection
import com.maxkeppeler.sheets.color.models.ColorSelectionMode
import com.maxkeppeler.sheets.color.models.MultipleColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPicker(
    initialColor: String,
    onColorChanged: (String) -> Unit,
) {
    var selectedColor by remember { mutableStateOf(initialColor) }
    val colorPickerState = UseCaseState(embedded = false)

    Box(
        Modifier
            .fillMaxWidth()
            .clickable { colorPickerState.show() }
            .height(50.dp)
            .padding(10.dp)
    ) {
        Row(
            Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.color_card),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "点此更改颜色",
                color = MaterialTheme.colorScheme.onSurface,
//                Color(java.lang.Long.parseLong("FF$initialColor", 16)),
                style = MaterialTheme.typography.bodyMedium
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(id = R.drawable.square),
                    contentDescription = null,
                    tint = Color(java.lang.Long.parseLong("FF$initialColor", 16)),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
    ColorDialog(
        state = colorPickerState,
        config = ColorConfig(
            allowCustomColorAlphaValues = false,
            defaultDisplayMode = ColorSelectionMode.CUSTOM,
            templateColors = templateColors()
        ),
        selection = ColorSelection { color->
            selectedColor = String.format("%06X", 0xFFFFFF and color)
            onColorChanged(selectedColor)
        }
    )
}
//备选颜色
fun templateColors(): MultipleColors.ColorsHex {
    return MultipleColors.ColorsHex(
        TemplateColors.colorList
    )
}