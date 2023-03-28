package no.floatinggoat.smartnotifications.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = Color(0xFF497174),
    background = Color(0xFFEFF5F5),
    secondary = Color(0xFFEB6440)
)

@Composable
fun SmartHomeControllerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}