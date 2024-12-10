package com.jmballangca.pawprints.utils


import android.content.Context

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jmballangca.pawprints.models.cart.CartWithProduct
import com.jmballangca.pawprints.models.transaction.TransactionItems


fun Double?.toPhp(): String {
    return if (this != null) {
        String.format("₱%,.2f", this)
    } else {
        "₱0.00"
    }
}


fun Context.toast(message : String) {
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

@Composable
fun Title(
    modifier: Modifier = Modifier,
    title : String
) {
    Text(
        title,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun Description(
    modifier: Modifier = Modifier,
    description : String,
) {
    Text(
        description,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = Color.Gray
        )
    )
}

fun <T> String.CreateLog(
    data : T
) {
    Log.d(
        this,
        data.toString()
    )
}

public fun generateRandomNumberString(length: Int = 15): String {
    val characters = "0123456789"
    var result = ""

    for (i in 0 until length) {
        val randomIndex = (Math.random() * characters.length).toInt()
        result += characters[randomIndex]
    }

    return result
}

public  fun generateRandomString(length: Int = 15): String {
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    var result = ""

    for (i in 0 until length) {
        val randomIndex = (Math.random() * characters.length).toInt()
        result += characters[randomIndex]
    }

    return result
}


fun List<CartWithProduct>.computeAll() : Double {
    val items = this
    var total  = 0.0
    items.forEach {
        total += (it.cart.quantity!! * it.product.price!!)
    }
    return  total
}

fun  List<CartWithProduct>.getAllQuantity() : Int {
    var count = 0
    this.forEach {
        count += it.cart.quantity ?: 0
    }
    return count;
}


fun List<TransactionItems>.getItemTotalPrice() : Double {
    val items = this
    var total  = 0.0
    items.forEach {
        total += (it.quantity!! * it.price!!)
    }
    return  total
}

fun  List<TransactionItems>.getItemsTotalQuantity() : Int {
    var count = 0
    this.forEach {
        count += it.quantity ?: 0
    }
    return count;
}


//fun String.createLog(
//    message : String
//) {
//    Log.d(this,message)
//}
//fun String.createErrorLog(
//    message: String,
//    e : Exception
//) {
//    Log.e(this,message,e)
//}