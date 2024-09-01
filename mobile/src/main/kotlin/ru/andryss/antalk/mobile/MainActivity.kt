package ru.andryss.antalk.mobile

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateMapOf
import androidx.navigation.compose.rememberNavController
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import ru.andryss.antalk.common.message.ChatDto
import ru.andryss.antalk.common.message.MessageDto
import ru.andryss.antalk.common.message.UpdateDto
import ru.andryss.antalk.common.message.UpdateType.CHAT_CREATED
import ru.andryss.antalk.common.message.UpdateType.MESSAGE_SENT
import ru.andryss.antalk.common.message.UserDto
import ru.andryss.antalk.mobile.pages.MainPage
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import java.io.IOException
import java.util.Properties

const val TAG = "antalk-mobile"

class Cache {
    val user: UserDto = UserDto("c3c0c6ad-cd6d-4003-be65-ba5db465f168", "andryss")
    val chats: MutableMap<String, ChatDto> = mutableStateMapOf()
    val messages: MutableMap<String, MutableList<MessageDto>> = mutableStateMapOf()
}

class AppState : Application() {
    lateinit var properties: Properties
    lateinit var httpClient: OkHttpClient
    lateinit var mapper: ObjectMapper
    lateinit var stompClient: StompClient
    lateinit var cache: Cache
}

fun AppState.configureWith(applicationContext: Context) {
    properties = Properties().apply {
        load(applicationContext.assets.open("app.properties"))
    }
    httpClient = OkHttpClient()
    mapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
    }
    val wsUrl = properties.getProperty("ws.url", "ws://localhost:8080/ws")
    stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, wsUrl)
    stompClient.connect()
    cache = Cache()
    subscribeOnTopics()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appState = application as AppState

        appState.configureWith(applicationContext)

        setContent {
            MainPage(
                state = appState, navController = rememberNavController()
            )
        }
    }
}

@SuppressLint("CheckResult")
fun AppState.subscribeOnTopics() {
    stompClient.topic("/user/${cache.user.id}/updates").subscribe {
        Log.i(TAG, "Received stomp message $it")
        val update: UpdateDto = mapper.readValue(it.payload)
        when (update.type) {
            CHAT_CREATED -> {
                val chat = update.chat!!
                cache.chats[chat.id] = chat
                cache.messages[chat.id] = mutableListOf(update.message)
            }
            MESSAGE_SENT -> {
                cache.messages[update.chatId]?.add(update.message)
            }
        }
    }
}

inline fun <reified T> AppState.newRequest(
    path: String, // e.g. "/some/12/url"
    crossinline callback: (T) -> Unit
) {
    val urlBuilder = HttpUrl.Builder()
        .scheme("http")
        .host(properties.getProperty("server.host", "localhost"))
        .port(properties.getProperty("server.port", "8080").toInt())
        .encodedPath(path)
        .addQueryParameter("userId", cache.user.id.toString())
    val request = Request.Builder()
        .get().url(urlBuilder.build())
        .build()
    Log.i(TAG, "Making http request $request")
    httpClient.newCall(request).enqueue(httpCallbackObj<T>(request, callback))
}

inline fun <reified T> AppState.httpCallbackObj(
    request: Request?,
    crossinline callback: (T) -> Unit
) = object : Callback {
    override fun onFailure(call: Call, e: IOException) {
        Log.e(TAG, "Received http IOException within ${call.timeout().timeoutNanos() / 1e6}ms", e)
    }
    override fun onResponse(call: Call, response: Response) {
        Log.e(TAG, "Received http response within ${call.timeout().timeoutNanos() / 1e6}ms ($response)")
        if (response.code() != 200) {
            Log.w(TAG, "Got http response unexpected code ${response.code()} ($request)")
            return
        }
        val body = response.body()
        if (body == null) {
            Log.w(TAG, "Gor http empty response body ($request)")
            return
        }
        val res: T = mapper.readValue(body.bytes())
        callback(res)
    }
}