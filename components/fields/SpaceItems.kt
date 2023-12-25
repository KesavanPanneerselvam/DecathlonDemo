package components.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SpaceSmall() = Spacer(
    modifier = Modifier
        .width(10.dp)
        .background(Color.Transparent)
        .height(8.dp)
)

@Composable
fun SpaceMedium() = Spacer(
    modifier = Modifier
        .width(10.dp)
        .background(Color.Transparent)
        .height(16.dp)
)

@Composable
fun SpaceLarge() = Spacer(
    modifier = Modifier
        .width(10.dp)
        .background(Color.Transparent)
        .height(32.dp)
)

