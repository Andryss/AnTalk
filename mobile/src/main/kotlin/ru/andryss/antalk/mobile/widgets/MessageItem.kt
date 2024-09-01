package ru.andryss.antalk.mobile.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.andryss.antalk.common.message.MessageDto
import ru.andryss.antalk.common.message.UserDto
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun MessageItem(
    message: MessageDto,
    alignRight: Boolean = false,
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface (
            modifier = Modifier
                .widthIn(min = 100.dp, max = 300.dp)
                .align(if (alignRight) Alignment.CenterEnd else Alignment.CenterStart),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = timeFormatter.format(message.timestamp.atZone(ZoneId.systemDefault())),
                    modifier = Modifier.align(Alignment.End),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun MessageItemPreview() {
    MessageItem(
        message = MessageDto("1", "1", "Commit and push your changes: Once you have set up the workflow file, commit and push it to your repository. GitHub Actions will automatically trigger the workflow based on the specified events (e.g., push or pull request to the main branch).", Instant.now())
    )
}