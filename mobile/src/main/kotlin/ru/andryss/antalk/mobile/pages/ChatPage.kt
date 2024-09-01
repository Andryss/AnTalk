package ru.andryss.antalk.mobile.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.andryss.antalk.mobile.AppState
import ru.andryss.antalk.mobile.widgets.MessagesList
import ru.andryss.antalk.mobile.widgets.ReturnTopAppBar
import ru.andryss.antalk.mobile.widgets.SendMessageBar

@Composable
fun ChatPage(state: AppState, chatId: String, onExitChat: () -> Unit) {
    val user = state.cache.user
    val chat = state.cache.chats[chatId]!!
    val messages = state.cache.messages[chatId]!!

    Scaffold(
        topBar = {
            ReturnTopAppBar(
                title = chat.recipient.name,
                showBackIcon = true,
                onIconClick = { onExitChat() }
            )
        }, bottomBar = {
            SendMessageBar()
        }, content = { padding ->
            Box(
                modifier = Modifier.padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MessagesList(messages, user)
                }
            }
        }
    )
}