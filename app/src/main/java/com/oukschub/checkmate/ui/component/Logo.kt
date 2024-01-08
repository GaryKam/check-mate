package com.oukschub.checkmate.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.oukschub.checkmate.R

@Composable
fun Logo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_launcher_background),
        contentDescription = null,
        modifier = modifier
            .border(
                BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
                CircleShape
            )
            .clip(CircleShape)
    )
}
