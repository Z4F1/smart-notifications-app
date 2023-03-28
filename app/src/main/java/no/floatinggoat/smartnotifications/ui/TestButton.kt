package no.floatinggoat.smartnotifications.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SimpleButton(
    text: String,
    action: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick = action,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}