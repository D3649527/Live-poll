package uk.ac.tees.mad.livepoll.presentation.navigation

import CreatePoll
import PollScreen
import VotingScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uk.ac.tees.mad.livepoll.presentation.ui.LoginScreen
import uk.ac.tees.mad.livepoll.presentation.ui.ProfileScreen
import uk.ac.tees.mad.livepoll.presentation.ui.SignUp
import uk.ac.tees.mad.livepoll.presentation.ui.SplashScreen
import uk.ac.tees.mad.livepoll.presentation.viewmodel.PollViewModel

sealed class ApplicationNavigation(val route : String){
    object Splash : ApplicationNavigation("splash")
    object Login : ApplicationNavigation("login")
    object Signup : ApplicationNavigation("signup")
    object Create : ApplicationNavigation("create")
    object Poll : ApplicationNavigation("home")
    object Vote : ApplicationNavigation("poll/{pollId}"){
        fun createRoute(pollId : String) = "poll/$pollId"
    }
    object Profile : ApplicationNavigation("profile")
}

@Composable
fun ApplicationNavigation(){
    val navController = rememberNavController()
    val viewModel : PollViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = ApplicationNavigation.Splash.route) {
        composable(route = ApplicationNavigation.Splash.route){
            SplashScreen(navController)
        }
        composable(route = ApplicationNavigation.Login.route){
            LoginScreen(viewModel,navController)
        }
        composable(route = ApplicationNavigation.Signup.route){
            SignUp(vm = viewModel,navController = navController)
        }
        composable(route = ApplicationNavigation.Poll.route){
            PollScreen(viewModel,navController)
        }
        composable(route = ApplicationNavigation.Create.route){
            CreatePoll(viewModel, navController)
        }
        composable(
            route = ApplicationNavigation.Vote.route,
            arguments = listOf(navArgument("pollId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pollId = backStackEntry.arguments?.getString("pollId")
            VotingScreen(viewModel, navController, pollId)
        }
        composable(route = ApplicationNavigation.Profile.route){
            ProfileScreen(viewModel,navController)
        }
    }
}