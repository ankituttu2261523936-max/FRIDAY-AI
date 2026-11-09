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

private val Orange = Color(0xFFFF7900)
private val Dark = Color(0xFF101010)
private val Panel = Color(0xFF1B1B1B)
private val Muted = Color(0xFFAAAAAA)

data class ChatMessage(val text: String, val fromUser: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FridayApp() }
    }
}

@Composable
fun FridayApp() {
    var input by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Hello. I’m FRIDAY. Your assistant is ready for setup.", false)
        )
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
                    Text("PERSONAL AI ASSISTANT", color = Muted, fontSize = 10.sp, letterSpacing = 2.sp)
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
                                ).padding(14.dp)
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
            }

            Row(
                Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(24.dp)).padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Add image/video picker in next phase */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Attach media", tint = Orange)
                }
                Box(Modifier.weight(1f).padding(horizontal = 8.dp)) {
                    if (input.isEmpty()) Text("Message FRIDAY...", color = Muted)
                    BasicTextField(
                        value = input,
                        onValueChange = { input = it },
                        textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                IconButton(onClick = { /* Voice capture is implemented in a later phase */ }) {
                    Icon(Icons.Default.Mic, contentDescription = "Voice", tint = Orange)
                }
                IconButton(onClick = {
                    val text = input.trim()
                    if (text.isNotEmpty()) {
                        messages.add(ChatMessage(text, true))
                        messages.add(ChatMessage("I received your message. Connect Gemini in the next setup phase to enable AI replies.", false))
                        input = ""
                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Orange)
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "Starter interface • AI, voice and device actions require configuration",
                color = Muted, fontSize = 10.sp, modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
