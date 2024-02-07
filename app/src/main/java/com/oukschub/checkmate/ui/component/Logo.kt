package com.oukschub.checkmate.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.oukschub.checkmate.R

/**
 * The CheckMate logo.
 */
@Composable
fun Logo(
    modifier: Modifier = Modifier,
    isSad: Boolean = false
) {
    Image(
        painter = painterResource(if (isSad) R.drawable.ic_checkmate_sad else R.drawable.ic_checkmate),
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .background(colorResource(R.color.logo_background))
            .scale(0.8F)
            .clip(CircleShape),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}
