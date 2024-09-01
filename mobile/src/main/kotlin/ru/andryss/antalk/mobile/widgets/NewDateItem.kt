package ru.andryss.antalk.mobile.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM")

@Composable
fun NewDateItem(date: LocalDate) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Surface(
            modifier = Modifier.align(Alignment.Center),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = date.format(dateFormatter),
                modifier = Modifier.padding(vertical = 3.dp, horizontal = 7.dp)
            )
        }
    }
}

@Preview
@Composable
fun NewDateItemPreview() {
    NewDateItem(date = LocalDate.of(2000, Month.NOVEMBER, 10))
}