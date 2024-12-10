package com.jmballangca.pawprints.utils

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.jmballangca.pawprints.R

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
    image: Uri?,
    onChangeImage : () -> Unit
) {
    Column (
        modifier = modifier
            .width(180.dp)
            .clip(RoundedCornerShape(8.dp))
    ){
        Image(
            painter = if (image != null)
                rememberAsyncImagePainter(image)
            else
                painterResource(id = R.drawable.add_image),
            contentDescription = "Product Image",
            modifier = modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )
        Button(
            onClick = onChangeImage,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Image")
                Spacer(modifier = modifier.width(8.dp))
                Text(text = "${if (image == null) "Add" else "Change"} Image")
            }
        }
    }
}