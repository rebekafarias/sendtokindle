package com.example.sendtokindle

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sendtokindle.ui.theme.SendToKindleTheme

class MainActivity : ComponentActivity() {

    private lateinit var openDocument: ActivityResultLauncher<String>
    private var selectedUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        openDocument = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedUri = uri
        }

        setContent {
            SendToKindleTheme {
                MainScreen(openDocument = openDocument, selectedUri = selectedUri) { email ->
                    sendEmail(email)
                }
            }
        }
    }

    private fun sendEmail(email: String) {
        selectedUri?.let { uri ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, "Send to Kindle - Document")
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "Sending document to Kindle.")
            }

            val chooser = Intent.createChooser(intent, "Send Email")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(chooser)
            } else {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
