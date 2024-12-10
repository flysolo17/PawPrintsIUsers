package com.jmballangca.pawprints.router

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.jmballangca.pawprints.models.cart.CartWithProduct
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.models.transaction.Transaction
import com.jmballangca.pawprints.models.users.Users
import com.jmballangca.pawprints.screens.auth.change_password.ChangePasswordScreen
import com.jmballangca.pawprints.screens.auth.change_password.ChangePasswordViewModel
import com.jmballangca.pawprints.screens.auth.edit_profile.EditProfileScreen
import com.jmballangca.pawprints.screens.auth.edit_profile.EditProfileViewModel
import com.jmballangca.pawprints.screens.drawer.about.AboutScreen
import com.jmballangca.pawprints.screens.drawer.clinic.ClinicScreen
import com.jmballangca.pawprints.screens.drawer.credits.CreditScreen
import com.jmballangca.pawprints.screens.drawer.developer.DeveloperScreen
import com.jmballangca.pawprints.screens.main.appointments.create.CreateAppointmentEvents
import com.jmballangca.pawprints.screens.main.appointments.create.CreateAppointmentScreen
import com.jmballangca.pawprints.screens.main.appointments.create.CreateAppointmentViewModel
import com.jmballangca.pawprints.screens.main.appointments.view.AppointmentEvents
import com.jmballangca.pawprints.screens.main.appointments.view.AppointmentScreen
import com.jmballangca.pawprints.screens.main.appointments.view.AppointmentViewModel
import com.jmballangca.pawprints.screens.main.cart.CartEvents
import com.jmballangca.pawprints.screens.main.cart.CartScreen
import com.jmballangca.pawprints.screens.main.cart.CartViewModel
import com.jmballangca.pawprints.screens.main.checkout.CheckoutEvents
import com.jmballangca.pawprints.screens.main.checkout.CheckoutScreen
import com.jmballangca.pawprints.screens.main.checkout.CheckoutVIewModel
import com.jmballangca.pawprints.screens.main.home.HomeEvents
import com.jmballangca.pawprints.screens.main.home.HomeScreen
import com.jmballangca.pawprints.screens.main.home.HomeViewModel
import com.jmballangca.pawprints.screens.main.inbox.InboxEvents
import com.jmballangca.pawprints.screens.main.inbox.InboxScreen
import com.jmballangca.pawprints.screens.main.inbox.InboxViewModel
import com.jmballangca.pawprints.screens.main.messages.MessageEvents
import com.jmballangca.pawprints.screens.main.messages.MessageScreen
import com.jmballangca.pawprints.screens.main.messages.MessageViewModel
import com.jmballangca.pawprints.screens.main.pets.PetEvents
import com.jmballangca.pawprints.screens.main.pets.PetScreen
import com.jmballangca.pawprints.screens.main.pets.PetViewModel
import com.jmballangca.pawprints.screens.main.products.view_product.ViewProductEvents

import com.jmballangca.pawprints.screens.main.products.view_product.ViewProductScreen
import com.jmballangca.pawprints.screens.main.products.view_product.ViewProductViewModel
import com.jmballangca.pawprints.screens.main.profile.ProfileEvents
import com.jmballangca.pawprints.screens.main.profile.ProfileScreen
import com.jmballangca.pawprints.screens.main.profile.ProfileViewModel
import com.jmballangca.pawprints.screens.main.transactions.TransactionEvents
import com.jmballangca.pawprints.screens.main.transactions.TransactionScreen
import com.jmballangca.pawprints.screens.main.transactions.TransactionViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun MainNavGraph(
    navHostController: NavHostController = rememberNavController(),
    mainNav : NavHostController,
    users: Users,
    carts : List<CartWithProduct>
) {
    NavHost(
        navHostController,
        route = AppRouter.MainScreen.route,
        startDestination = AppRouter.HomeScreen.route
    ) {
        composable(AppRouter.HomeScreen.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            viewModel.events(HomeEvents.OnSetUser(users = users))
            HomeScreen(state = viewModel.state, events = viewModel::events, navHostController=navHostController)
        }
        composable(AppRouter.CartScreen.route) {
            val viewModel = hiltViewModel<CartViewModel>()
            viewModel.events(CartEvents.OnUpdateCart(carts = carts))
            CartScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController=navHostController
            )
        }

        composable(AppRouter.PetScreen.route) {
            val viewmodel = hiltViewModel<PetViewModel>()
            viewmodel.events(PetEvents.OnSetUser(users))
            PetScreen(
                state = viewmodel.state,
                events = viewmodel::events,
                userID = users.id ?: "",
                navHostController=navHostController
            )

        }
        composable(AppRouter.MessageScreen.route) {
            val viewModel = hiltViewModel<MessageViewModel>()
            viewModel.events(MessageEvents.OnSetUsers(users))
            MessageScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController=navHostController
            )
        }
        composable(AppRouter.ProfileScreen.route) {
            val viewModel = hiltViewModel<ProfileViewModel>()
            viewModel.events(ProfileEvents.OnSetUser(users))
            ProfileScreen(
                navHostController=navHostController,
                mainNav = mainNav,
                state = viewModel.state,
                events = viewModel::events
            )
        }


        //nav drawer
        composable(
            route = AppRouter.AboutScreen.route
        ) {
            AboutScreen()
        }
        composable(
            route = AppRouter.DevelopersScreen.route
        ) {
            DeveloperScreen()
        }
        composable(
            route = AppRouter.CreditsScreen.route
        ) {
            CreditScreen()
        }
        composable(
            route = AppRouter.ClinicScreen.route
        ) {
            ClinicScreen()
        }

        //sub routes

        composable(AppRouter.ViewProductRoute.route) { backStackEntry ->
            val productID = backStackEntry.arguments?.getString("args")
            val viewModel = hiltViewModel<ViewProductViewModel>()
            viewModel.events(ViewProductEvents.OnSetUsers(users))
            ViewProductScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController,
                productID = productID ?: ""
            )
        }

        composable(AppRouter.CreateAppointmentRoute.route) { backStackEntry ->
            val args = backStackEntry.arguments?.getString("args")
            val decodedJson = args?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
            val product = decodedJson?.let { Gson().fromJson(it, Product::class.java) }
            val viewModel = hiltViewModel<CreateAppointmentViewModel>()
            viewModel.events(CreateAppointmentEvents.OnSetUsers(users))
            CreateAppointmentScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController,
                product = product
            )
        }




        composable(
            route = AppRouter.CheckoutPage.route,
        ) { backStackEntry ->
            val args = backStackEntry.arguments?.getString("args")
            val decodedJson = URLDecoder.decode(args, StandardCharsets.UTF_8.toString())
            val transaction = Gson().fromJson(decodedJson, Transaction::class.java)
            val viewModel = hiltViewModel<CheckoutVIewModel>()
            viewModel.events(CheckoutEvents.OnSetUser(users))
            CheckoutScreen(
                transaction = transaction,
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }






        composable(route = AppRouter.ChangePassword.route) {
            val viewModel = hiltViewModel<ChangePasswordViewModel>()
            ChangePasswordScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
        composable(
            route = AppRouter.EditProfileRoute.route,

            ) { backStackEntry ->
            val args = backStackEntry.arguments?.getString("args")
            val decodedJson = URLDecoder.decode(args, StandardCharsets.UTF_8.toString())
            val users = Gson().fromJson(decodedJson, Users::class.java)
            val viewModel = hiltViewModel<EditProfileViewModel>()
            users?.let {
                EditProfileScreen(
                    users = it,
                    state = viewModel.state,
                    events = viewModel::events,
                    navHostController = navHostController
                )
            }
        }

        //other screens
        composable(
            route = AppRouter.Appointments.route
        ) {
            val viewModel = hiltViewModel<AppointmentViewModel>()
            viewModel.events(AppointmentEvents.OnSetUsers(users))
            AppointmentScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
        composable(
            route = AppRouter.Transactions.route
        ) {
            val viewModel = hiltViewModel<TransactionViewModel>()
            viewModel.events(TransactionEvents.OnSetUsers(users))
            TransactionScreen(
                state =  viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
        composable(
            route = AppRouter.Inbox.route
        ) {
            val viewmodel = hiltViewModel<InboxViewModel>()
            viewmodel.events(InboxEvents.OnSetUsers(users))
            InboxScreen(
                state = viewmodel.state,
                events = viewmodel::events,
                navHostController = navHostController
            )
        }
    }
}