package com.jmballangca.pawprints.router

import com.google.gson.Gson
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.models.transaction.Transaction
import com.jmballangca.pawprints.models.users.Users
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


sealed class AppRouter(val route : String) {
    //auth
    data object StarterScreen : AppRouter(route = "start")
    data object AuthRoutes : AppRouter(route = "auth")
    data object LoginScreen : AppRouter(route = "login")

    data object RegisterScreen : AppRouter(route = "register")
    data object ForgotPasswordScreen : AppRouter(route = "forgot-password")
    data object ChangePassword : AppRouter(route = "change-password")
    data object EditProfileRoute : AppRouter(route = "edit-profile/{args}") {
        fun navigate(args: Users) : String {
            val content = Gson().toJson(args)
            val encodedJson = URLEncoder.encode(content, StandardCharsets.UTF_8.toString())
            return "edit-profile/$encodedJson"
        }
    }

    // Main routes
    data object MainScreen : AppRouter(route = "main")
    data object HomeScreen : AppRouter(route = "home")
    data object PetScreen : AppRouter(route = "pet")
    data object CartScreen : AppRouter(route = "cart")
    data object MessageScreen : AppRouter(route = "message")
    data object ProfileScreen : AppRouter(route = "profile")

    //main sub route
    data object CreatePet : AppRouter("pet/create")
    data object ViewProductRoute : AppRouter(route = "product/{args}") {
        fun navigate(productID : String) = "product/${productID}"
    }

    data object CheckoutPage : AppRouter(route = "checkout/{args}") {
        fun navigate(args: Transaction) : String {


            val content = Gson().toJson(args)
            val encodedJson = URLEncoder.encode(content, StandardCharsets.UTF_8.toString())
            return "checkout/$encodedJson"
        }
    }

    data object CreateAppointmentRoute : AppRouter(route = "create-appointment/{args}") {

        fun navigate(args: Product?): String {
            val content = args?.let { Gson().toJson(it) } ?: "null"
            val encodedJson = URLEncoder.encode(content, StandardCharsets.UTF_8.toString())
            return "create-appointment/$encodedJson"
        }
    }


    //navigation drawer
    data object ClinicScreen : AppRouter(route="clinic")
    data object AboutScreen : AppRouter(route = "about")
    data object DevelopersScreen : AppRouter(route = "developers")
    data object CreditsScreen : AppRouter(route = "credits")


    //other screens
    data object Appointments : AppRouter(route = "appointments")
    data object Transactions : AppRouter(route = "transactions")
    data object Inbox : AppRouter(route = "inbox")
}