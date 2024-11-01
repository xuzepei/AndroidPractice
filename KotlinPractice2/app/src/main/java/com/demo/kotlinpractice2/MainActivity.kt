package com.demo.kotlinpractice2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.kotlinpractice2.ui.theme.KotlinPractice2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinPractice2Theme {
                Scaffold(modifier = Modifier.fillMaxSize(),containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
                    GreetingText("Happy Birthday Sam!", "From Emma", Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GreetingText(message: String, from: String, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.Center, horizontalAlignment= Alignment.End) {
        Text(text = message, fontSize = 50.sp, lineHeight = 50.sp, textAlign = TextAlign.Center)
        Text(text = from, fontSize = 30.sp, lineHeight = 30.sp, )
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

@Preview(showBackground = true, name="My Preview")
@Composable
fun BirthdayCardPreview() {
    KotlinPractice2Theme {
        GreetingText("Happy Birthday Sam!", "From Emma", modifier = Modifier.padding(20.dp))
    }
}