package ru.andryss.antalk.mobile.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.andryss.antalk.common.message.MessageDto
import ru.andryss.antalk.common.message.UserDto
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun MessagesList(messages: List<MessageDto>, user: UserDto) {
    var previousDate = LocalDate.from(Instant.EPOCH.atZone(ZoneId.systemDefault()))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        messages.forEach {
            val date = LocalDate.from(it.timestamp.atZone(ZoneId.systemDefault()))
            if (date.isAfter(previousDate)) {
                NewDateItem(date)
                previousDate = date
            }
            MessageItem(
                message = it,
                alignRight = (it.senderId == user.id)
            )
        }
    }
}

@Preview
@Composable
fun MessagesListPreview() {
    MessagesList(
        messages = listOf(
            MessageDto("1", "first", "Hello", Instant.now()),
            MessageDto("2", "first", "I'm first", Instant.now()),
            MessageDto("3", "second", "Hi!", Instant.now()),
            MessageDto("4", "first", "What's up?", Instant.now()),
            MessageDto("5", "second", "I'm oK bro", Instant.now()),
        ),
        user = UserDto("second", "Some username")
    )
}
