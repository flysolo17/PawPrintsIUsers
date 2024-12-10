package com.jmballangca.pawprints.router

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jmballangca.pawprints.screens.auth.forgotPassword.ForgotPasswordScreen
import com.jmballangca.pawprints.screens.auth.forgotPassword.ForgotPasswordViewModel
import com.jmballangca.pawprints.screens.auth.login.LoginScreen
import com.jmballangca.pawprints.screens.auth.login.LoginViewModel
import com.jmballangca.pawprints.screens.auth.register.RegisterScreen
import com.jmballangca.pawprints.screens.auth.register.RegisterViewModel

fun NavGraphBuilder.authNavGraph(navHostController: NavHostController) {
    navigation(startDestination = AppRouter.LoginScreen.route,route = AppRouter.AuthRoutes.route) {
        composable(route = AppRouter.LoginScreen.route) {
            val viewmodel = hiltViewModel<LoginViewModel>()
            val state = viewmodel.state
            val events = viewmodel::events
            LoginScreen(navHostController = navHostController, state = state, events = events)
        }

//        composable(route = AppRouter.VerificationScreen.route) {
//            val viewmodel = hiltViewModel<VerificationViewModel>()
//            val state = viewmodel.state
//            val events = viewmodel::events
//            VerificationScreen(navHostController = navHostController, state = state, events = events)
//        }
        composable(route = AppRouter.RegisterScreen.route) {
            val viewmodel = hiltViewModel<RegisterViewModel>()
            val state = viewmodel.state
            val events = viewmodel::events
            RegisterScreen(navHostController = navHostController, state = state, events = events)
        }
//
//
        composable(route = AppRouter.ForgotPasswordScreen.route) {
            val viewModel = hiltViewModel<ForgotPasswordViewModel>()
            ForgotPasswordScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
    }
}