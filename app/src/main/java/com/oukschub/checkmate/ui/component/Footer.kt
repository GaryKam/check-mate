package com.oukschub.checkmate.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

/**
 * A composable to be displayed at the bottom of the screen.
 */
@Composable
fun Footer(
    text: AnnotatedString,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(Modifier.padding(vertical = 30.dp))

        ClickableText(
            text = text,
            onClick = { onClick() }
        )
    }
}
