package ru.andryss.antalk.mobile.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.andryss.antalk.common.message.ChatDto
import ru.andryss.antalk.common.message.UserDto
import java.time.Instant
import kotlin.random.Random

@Composable
fun ChatIcon(chat: ChatDto) {
    val random = Random(chat.id[0].code)
    val color = Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))

    val letters = remember(chat.recipient) {
        val words = chat.recipient.name.split(' ')
        if (words.isEmpty()) "?"
        else if (words.size == 1) words[0][0].uppercase()
        else words[0][0].uppercase() + words[1][0].uppercase()
    }

    Surface(
        modifier = Modifier.size(60.dp),
        shape = RoundedCornerShape(50),
        color = color
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letters,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Preview
@Composable
fun ChatIconPreview() {
    ChatIcon(
        ChatDto("100", UserDto("1", "Some user"), Instant.now(), UserDto("2", "Another user"))
    )
}