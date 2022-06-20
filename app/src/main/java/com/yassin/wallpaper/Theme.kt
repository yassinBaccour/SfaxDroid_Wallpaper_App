import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.yassin.wallpaper.DarkColors
import com.yassin.wallpaper.LightColors

@Composable
fun SfaxDroidTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (useDarkColors) DarkColors else LightColors,
        content = content
    )
}
