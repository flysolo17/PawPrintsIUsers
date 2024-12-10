package com.jmballangca.pawprints.screens.main.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.models.products.ProductType
import com.jmballangca.pawprints.utils.RoundedImage
import com.jmballangca.pawprints.utils.toPhp


@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    isAdding : Boolean,
    onClick : () -> Unit,
    onAddToCart : () -> Unit
) {
    OutlinedCard(
        shape = MaterialTheme.shapes.small,
        modifier = modifier.fillMaxWidth().padding(4.dp).clickable {
            onClick()
        }
    ) {
        Row(
            modifier = modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            RoundedImage(
                modifier = modifier.size(80.dp),
                image = product.image ?: ""
            )
            Column(
                modifier = modifier.weight(1f)
            ) {
                Text(
                    "${product.name}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,

                        )
                )
                Text(
                    "${product.description}",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
                Text(
                    "${product.price.toPhp()}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            if (product.type == ProductType.SERVICES) {
                Icon(
                    imageVector = Icons.Filled.ArrowForwardIos,
                    contentDescription = "View"
                )
            } else {
                if (isAdding) {
                    CircularProgressIndicator(modifier = modifier.size(16.dp))
                } else {
                    IconButton(
                        onClick = {
                            onAddToCart()
                        }
                    ) { Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Cart"
                    )
                    }
                }

            }

        }
    }
}