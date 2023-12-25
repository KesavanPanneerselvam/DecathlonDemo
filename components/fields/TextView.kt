package components.fields

import AppTypography
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun TextSmall(text: String = "", modifier :Modifier = Modifier, color: Color = MaterialTheme.colorScheme.onBackground) = Text(
    text,
    style = AppTypography.labelSmall,
    modifier = modifier,
    color = color
)

@Composable
fun TextMedium(text: String = "", modifier :Modifier = Modifier, color: Color = MaterialTheme.colorScheme.onBackground) = Text(
    text,
    style = AppTypography.labelMedium,
    modifier = modifier,
    color = color
)

@Composable
fun TextTitle(text: String = "", modifier :Modifier = Modifier, color: Color = MaterialTheme.colorScheme.onBackground) = Text(
    text,
    style = AppTypography.labelLarge,
    modifier = modifier,
    color = color
)