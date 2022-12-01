package com.eastx7.clips.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VerticalDivider() {
    Divider(
        color = Color.Black,
        modifier = Modifier
            .fillMaxHeight()
            .width(thicknessDivider)
    )
}

@Composable
fun HorizontalDivider() {
    Divider(color = Color.Black, thickness = thicknessDivider)
}

@Composable
fun VertFieldSpacer() {
    Spacer(modifier = Modifier.height(12.dp))
}

