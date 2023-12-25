package components.fields

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.chat.ChatButtonContent

@Composable
fun ButtonSmall(title: String, onClicked: () -> Unit) = Button(onClick = onClicked) {
    TextMedium(title)
}

@Composable
fun ButtonChatOption(content: ChatButtonContent, isEnabled: MutableState<Boolean>, onClicked: (ChatButtonContent) -> Unit) {
    val animatedButtonColor = animateColorAsState(
        targetValue = if (isEnabled.value) MaterialTheme.colorScheme.tertiary else Color.Gray,
        animationSpec = tween(1000, 0, LinearEasing)
    )
    return Button(
        onClick = {
            if (isEnabled.value) {
                onClicked.invoke(content)
            }
        },
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
        modifier = Modifier.height(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedButtonColor.value,
            disabledContainerColor = animatedButtonColor.value,
        )
    ) {
        TextMedium(text = content.title, color = MaterialTheme.colorScheme.background)
    }
}