package com.friday.assistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeBackend
import com.google.firebase.ai.ai
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import kotlinx.coroutines.launch

private val Orange = Color(0xFFFF7900)
private val Dark = Color(0xFF101010)
private val Panel = Color(0xFF1B1B1B)
private val Muted = Color(0xFFAAAAAA)

data class ChatMessage(val text: String, val fromUser: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )

        setContent { FridayApp() }
    }
}

@Composable
fun FridayApp() {
    var input by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Hello. I'm FRIDAY. Gemini AI is ready to connect.", false)
        )
    }

    val model = remember {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-3.8-flash")
    }

    fun sendMessage() {
        val userText = input.trim()
        if (userText.isEmpty() || isLoading) return

        messages.add(ChatMessage(userText, true))
        input = ""

        val prompt = buildString {
            append(
                "You are FRIDAY, a helpful, intelligent and polite personal AI assistant. " +
                "Speak naturally and clearly. Keep answers useful and easy to understand.\n\n"
            )
            messages.takeLast(12).forEach { message ->
                append(if (message.fromUser) "User: " else "FRIDAY: ")
                append(message.text)
                append("\n")
            }
            append("\nReply to the latest user message.")
        }

        isLoading = true

        scope.launch {
            try {
                val response = model.generateContent(prompt)
                val answer = response.text?.takeIf { it.isNotBlank() }
                    ?: "I couldn't generate a response. Please try again."
                messages.add(ChatMessage(answer, false))
            } catch (e: Exception) {
                messages.add(
                    ChatMessage(
                        "I couldn't connect to Gemini. Please check your internet and Firebase App Check setup. Error: ${e.localizedMessage ?: "Unknown error"}",
                        false
                    )
                )
            } finally {
                isLoading = false
            }
        }
    }

    MaterialTheme {
        Column(
            Modifier.fillMaxSize().background(Dark).padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("FRIDAY", color = Orange, fontSize = 26.sp)
                    Text(
                        "PERSONAL AI ASSISTANT",
                        color = Muted,
                        fontSize = 10.sp,
                        letterSpacing = 2.sp
                    )
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.History, contentDescription = "History", tint = Orange)
                }
            }

            HorizontalDivider(color = Orange.copy(alpha = 0.35f))
            Spacer(Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages) { message ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = if (message.fromUser) Arrangement.End else Arrangement.Start
                    ) {
                        Column(
                            Modifier.fillMaxWidth(if (message.fromUser) 0.78f else 1f)
                                .background(
                                    if (message.fromUser) Color(0xFF38200F) else Panel,
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(14.dp)
                        ) {
                            Text(
                                if (message.fromUser) "YOU" else "FRIDAY",
                                color = Orange,
                                fontSize = 10.sp
                            )
                            Spacer(Modifier.height(5.dp))
                            Text(message.text, color = Color.White, fontSize = 15.sp)
                        }
                    }
                }

                if (isLoading) {
                    item {
                        Text("FRIDAY is thinking...", color = Orange, fontSize = 13.sp)
                    }
                }
            }

            Row(
                Modifier.fillMaxWidth()
                    .background(Panel, RoundedCornerShape(24.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Add, contentDescription = "Attach media", tint = Orange)
                }

                Box(Modifier.weight(1f).padding(horizontal = 8.dp)) {
                    if (input.isEmpty()) {
                        Text("Message FRIDAY...", color = Muted)
                    }
                    BasicTextField(
                        value = input,
                        onValueChange = { input = it },
                        textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                IconButton(onClick = { }) {
                    Icon(Icons.Default.Mic, contentDescription = "Voice", tint = Orange)
                }

                IconButton(
                    onClick = { sendMessage() },
                    enabled = !isLoading
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Orange)
                }
            }

            Spacer(Modifier.height(6.dp))
            Text(
                "Gemini AI • Voice and device actions coming in later phases",
                color = Muted,
                fontSize = 10.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
