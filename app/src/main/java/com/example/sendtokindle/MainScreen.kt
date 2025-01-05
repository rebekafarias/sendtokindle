package com.example.sendtokindle

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sendtokindle.ui.theme.SendToKindleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(openDocument: ActivityResultLauncher<String>?, selectedUri: Uri?, onSendEmail: (String) -> Unit) {
    var fileName by remember { mutableStateOf("No file selected") }
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    selectedUri?.let {
        fileName = it.path ?: "File selected"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = fileName,
            style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { openDocument?.launch("application/pdf") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1a1a1a), contentColor = Color.White),
            modifier = Modifier
                .wrapContentWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Select File")
        }

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter email address") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.isNotEmpty(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Black.copy(alpha = 0.3f),
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
            )
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isValidKindleEmail(email)) {
                    onSendEmail(email)
                } else {
                    errorMessage = "Invalid email. Must be @kindle.com"
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1a1a1a), contentColor = Color.White),
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(text = "Send to Kindle")
        }
    }
}

private fun isValidKindleEmail(email: String): Boolean {
    return email.endsWith("@kindle.com")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SendToKindleTheme {
        MainScreen(openDocument = null, selectedUri = null) { }
    }
}
