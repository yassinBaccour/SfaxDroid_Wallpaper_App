package com.yassin.sfax.tawakkolalaallah


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yassin.sfax.tawakkolalaallah.ui.SfaxDroidTheme

class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SfaxDroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!", modifier = Modifier.padding(24.dp))
}

@Composable
fun buttonShow() {
    Row {
        Column {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            Button(onClick = { }) {
                Text("Set the wallpaper")
            }
            Button(onClick = { }) {
                Text("please Rate us")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SfaxDroidTheme {
        Surface(color = MaterialTheme.colors.background) {
            buttonShow()
        }
    }
}