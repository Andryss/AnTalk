package ru.andryss.antalk.mobile.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.andryss.antalk.common.message.ChatDto
import ru.andryss.antalk.common.message.MessageDto
import ru.andryss.antalk.common.message.UserDto
import java.time.Instant
import java.time.ZoneId

@Composable
fun ChatThumbnail(
    chat: ChatDto,
    lastMessage: MessageDto,
    onSelected: () -> Unit = { }
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 1.dp)
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onSelected() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ChatIcon(chat = chat)
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.recipient.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = timeFormatter.format(lastMessage.timestamp.atZone(ZoneId.systemDefault())),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Text(
                text = lastMessage.text,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview
@Composable
fun ChatThumbnailPreview() {
    val user = UserDto("1", "Some user")
    ChatThumbnail(
        ChatDto("1", user, Instant.now(), user),
        MessageDto("1", user.id, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua", Instant.now())
    )
}