package com.ahmetbostancikli


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


data class BuilderPattern(
    val text: String,
    val textColor: Color,
    val backgroundColor: Color,
    val padding: PaddingValues,
    val onClick: () -> Unit
) {
    class Builder {
        private var text: String = "Button"
        private var textColor: Color = Color.White
        private var backgroundColor: Color = Color.Blue
        private var padding: PaddingValues = PaddingValues(16.dp)
        private var onClick: () -> Unit = {}

        fun text(text: String) = apply { this.text = text }
        fun textColor(textColor: Color) = apply { this.textColor = textColor }
        fun backgroundColor(backgroundColor: Color) =
            apply { this.backgroundColor = backgroundColor }

        fun padding(padding: PaddingValues) = apply { this.padding = padding }
        fun onClick(onClick: () -> Unit) = apply { this.onClick = onClick }

        fun build() = BuilderPattern(text, textColor, backgroundColor, padding, onClick)
    }
}

@Composable
fun StyleButton(button: BuilderPattern) {


}

@Composable
fun BuilderDesignPatternScreen() {

    val context = LocalContext.current
    // Create a BuilderPattern instance
    val myButton = BuilderPattern.Builder()
        .text("Click Me")
        .textColor(Color.White)
        .backgroundColor(Color.Red)
        .padding(PaddingValues(20.dp))
        .onClick { Toast.makeText(context, "Button is clicked", Toast.LENGTH_SHORT).show() }
        .build()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = myButton.onClick,
            colors = ButtonDefaults.buttonColors(containerColor = myButton.backgroundColor),
            contentPadding = myButton.padding
        ) {
            Text(text = myButton.text, color = myButton.textColor)
        }
    }
}

@Composable
fun BuilderDesignPatternApp() {
    MaterialTheme {
        BuilderDesignPatternScreen()
    }
}

@Preview
@Composable
private fun PreviewMyApp() {
    BuilderDesignPatternApp()
}


/*
object RetrofitInstance {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
 */