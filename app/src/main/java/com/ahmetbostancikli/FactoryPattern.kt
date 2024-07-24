package com.ahmetbostancikli

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview


interface ButtonFactory {
    @Composable
    fun CreateButton(onClick: () -> Unit)
}

class PrimaryButtonFactory : ButtonFactory {
    @Composable
    override fun CreateButton(onClick: () -> Unit) {
        Button(onClick = onClick, colors = ButtonDefaults.buttonColors(Color.Blue)) {
            Text(text = "Primary Button", color = Color.White)
        }
    }
}

class SecondaryButtonFactory : ButtonFactory {
    @Composable
    override fun CreateButton(onClick: () -> Unit) {
        Button(onClick = onClick, colors = ButtonDefaults.buttonColors(Color.Gray)) {
            Text(text = "Secondary Button", color = Color.White)
        }
    }
}

object ButtonFactoryProvider {
    fun getFactory(type: ButtonType): ButtonFactory {
        return when (type) {
            ButtonType.PRIMARY -> PrimaryButtonFactory()
            ButtonType.SECONDARY -> SecondaryButtonFactory()
        }
    }
}

enum class ButtonType {
    PRIMARY,
    SECONDARY
}


@Composable
fun FactoryPatternScreen(buttonType: ButtonType) {
    val buttonFactory = ButtonFactoryProvider.getFactory(buttonType)
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        buttonFactory.CreateButton {
            Toast.makeText(context, "Primary Button Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun FactoryPatternApp() {
    FactoryPatternScreen(buttonType = ButtonType.PRIMARY)
}

@Preview
@Composable
fun PreviewMyApp2() {
   FactoryPatternApp()
}