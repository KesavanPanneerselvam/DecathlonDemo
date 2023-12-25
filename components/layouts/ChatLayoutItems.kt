package components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import components.fields.TextMedium
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBubbleLayout(
    isFromMe: Boolean,
    content: @Composable() () -> Unit,
) {
    val backgroundBubbleColor = if (isFromMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    }
    if(isFromMe) {
        Row {
            Column(
                modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                //ChatLayout(isFromMe, content)
                Surface(
                    color = backgroundBubbleColor,
                    shape = if(isFromMe)  chatBubbleShape1 else chatBubbleShape2
                ) {
                    TextMedium(
                        text = "Test",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }else {
        Column() {
            Surface(
                color = backgroundBubbleColor,
                shape = if(isFromMe)  chatBubbleShape1 else chatBubbleShape2
            ) {
                TextMedium(
                    text = "Test",
                    modifier = Modifier.padding(8.dp)
                )

            }
            //ChatLayout(isFromMe, content)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatLayout(isUserMe: Boolean,content: @Composable() () -> Unit){
    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }
    Surface(
        color = backgroundBubbleColor,
        shape = if(isUserMe)  chatBubbleShape1 else chatBubbleShape2
    ) {
        Layout(
            modifier = Modifier.padding(vertical = 16.dp).background(color = Color.White),
            content = content
        ) { measurables, constraints ->
            // Don't constrain child views further, measure them with given constraints
            // List of measured children
            val placeables = measurables.map { measurable ->
                // Measure each children
                measurable.measure(constraints)
            }

            // Set the size of the layout as big as it can
            layout(constraints.maxWidth, constraints.maxHeight) {
                // Track the y co-ord we have placed children up to
                var yPosition = 0

                // Place children in the parent layout
                placeables.forEach { placeable ->
                    // Position item on the screen
                    placeable.placeRelative(x = 0, y = yPosition)

                    // Record the y co-ord placed up to
                    yPosition += placeable.height
                }
            }
        }
    }
}

private val chatBubbleShape2 = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)

private val chatBubbleShape1 = RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)

@Composable
fun ClickableMessage(
    message: String,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit,
) {
    /*val uriHandler = LocalUriHandler.current

    val mainTextColor = when (isUserMe) {
        true -> MaterialTheme.colorScheme.onPrimary
        false -> MaterialTheme.colorScheme.onSecondary
    }

    val styledMessage = messageFormatter(
        text = message,
        primary = isUserMe,
    )*/
    //val styledMessage =

    TextMedium(
        text = message,
        modifier = Modifier.padding(16.dp)
    )
}

/*
val symbolPattern by lazy {
    Regex("""(https?://[^\s\t\n]+)|(`[^`]+`)|(@\w+)|(\*[\w]+\*)|(_[\w]+_)|(~[\w]+~)""")
}

@Composable
fun messageFormatter(
    text: String,
    primary: Boolean,
): AnnotatedString {
    val tokens = symbolPattern.findAll(text)

    return buildAnnotatedString {

        var cursorPosition = 0

        val codeSnippetBackground =
            if (primary) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.secondaryContainer
            }

        for (token in tokens) {
            append(text.slice(cursorPosition until token.range.first))

            val (annotatedString, stringAnnotation) = getSymbolAnnotation(
                matchResult = token,
                primary = primary,
                codeSnippetBackground = codeSnippetBackground,
            )
            append(annotatedString)

            if (stringAnnotation != null) {
                val (item, start, end, tag) = stringAnnotation
                addStringAnnotation(tag = tag, start = start, end = end, annotation = item)
            }

            cursorPosition = token.range.last + 1
        }

        if (!tokens.none()) {
            append(text.slice(cursorPosition..text.lastIndex))
        } else {
            append(text)
        }
    }
}


// Accepted annotations for the ClickableTextWrapper
enum class SymbolAnnotationType {
    PERSON, LINK
}
typealias StringAnnotation = AnnotatedString.Range<String>
// Pair returning styled content and annotation for ClickableText when matching syntax token
typealias SymbolAnnotation = Pair<AnnotatedString, StringAnnotation?>

@Composable
private fun getSymbolAnnotation(
    matchResult: MatchResult,
    primary: Boolean,
    codeSnippetBackground: Color,
): SymbolAnnotation {
    return when (matchResult.value.first()) {
        '@' -> SymbolAnnotation(
            AnnotatedString(
                text = matchResult.value,
                spanStyle = SpanStyle(
                    color = if (primary) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            ),
            StringAnnotation(
                item = matchResult.value.substring(1),
                start = matchResult.range.first,
                end = matchResult.range.last,
                tag = SymbolAnnotationType.PERSON.name
            )
        )

        '*' -> SymbolAnnotation(
            AnnotatedString(
                text = matchResult.value.trim('*'),
                spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
            ),
            null
        )

        '_' -> SymbolAnnotation(
            AnnotatedString(
                text = matchResult.value.trim('_'),
                spanStyle = SpanStyle(fontStyle = FontStyle.Italic)
            ),
            null
        )

        '~' -> SymbolAnnotation(
            AnnotatedString(
                text = matchResult.value.trim('~'),
                spanStyle = SpanStyle(textDecoration = TextDecoration.LineThrough)
            ),
            null
        )

        '`' -> SymbolAnnotation(
            AnnotatedString(
                text = matchResult.value.trim('`'),
                spanStyle = SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    background = codeSnippetBackground,
                    baselineShift = BaselineShift(0.2f)
                )
            ),
            null
        )

        'h' -> SymbolAnnotation(
            AnnotatedString(
                text = matchResult.value,
                spanStyle = SpanStyle(
                    color = if (primary) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
                )
            ),
            StringAnnotation(
                item = matchResult.value,
                start = matchResult.range.first,
                end = matchResult.range.last,
                tag = SymbolAnnotationType.LINK.name
            )
        )

        else -> SymbolAnnotation(AnnotatedString(matchResult.value), null)
    }
}*/
