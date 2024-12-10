package com.jmballangca.pawprints.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jmballangca.pawprints.R


@Composable
fun RoundedImage(
    modifier: Modifier = Modifier,
    image : String,
) {
    AsyncImage(
        model = image,
        contentDescription = "product image",
        error = painterResource(R.drawable.paw_logo),
        placeholder = painterResource(R.drawable.paw_logo),
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(
            shape = MaterialTheme.shapes.medium
        )
    )
}