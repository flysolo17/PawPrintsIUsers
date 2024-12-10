package com.jmballangca.pawprints.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
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
fun ProfileImage(
    modifier: Modifier = Modifier,
    image : String,
) {
    AsyncImage(
        model = image,
        contentDescription = "Profile",
        error = painterResource(R.drawable.placeholder),
        placeholder = painterResource(R.drawable.placeholder),
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(
            CircleShape
        )
    )
}