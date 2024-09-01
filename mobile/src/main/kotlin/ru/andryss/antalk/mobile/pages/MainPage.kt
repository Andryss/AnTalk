package ru.andryss.antalk.mobile.pages

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.andryss.antalk.mobile.AppState

enum class Routes(val route: String) {
    HOME("home"),
    CHAT("chat/{chatId}"),
}

@Composable
fun MainPage(state: AppState, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME.route,
        builder = {
            composable(Routes.HOME.route) {
                HomePage(
                    state = state,
                    onChatSelected = { chatId ->
                        navController.navigate("chat/$chatId")
                    }
                )
            }
            composable(
                Routes.CHAT.route,
                arguments = listOf(navArgument("chatId") { type = NavType.StringType })
            ) { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId")
                if (chatId == null) {
                    HomePage(
                        state = state,
                        onChatSelected = { id ->
                            navController.navigate("chat/$id")
                        }
                    )
                } else {
                    ChatPage(
                        state = state,
                        chatId = chatId,
                        onExitChat = { navController.navigateUp() }
                    )
                }
            }
        }
    )
}