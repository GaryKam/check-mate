package com.oukschub.checkmate.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.oukschub.checkmate.R

@Preview
@Composable
fun Logo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_logo),
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .background(colorResource(R.color.logo_background))
    )
}
