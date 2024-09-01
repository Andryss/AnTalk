package ru.andryss.antalk.mobile.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.andryss.antalk.mobile.AppState
import ru.andryss.antalk.mobile.widgets.ChatThumbnail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(state: AppState, onChatSelected: (String) -> Unit) {
    val chats = state.cache.chats
    val messages = state.cache.messages

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AnTalk",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
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
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        chats.forEach { (chatId, chat) ->
                            ChatThumbnail(
                                chat = chat,
                                lastMessage = messages[chatId]!!.last(),
                                onSelected = { onChatSelected(chatId) }
                            )
                            HorizontalDivider(thickness = 2.dp)
                        }
                    }
                }
            }
        }
    )
}