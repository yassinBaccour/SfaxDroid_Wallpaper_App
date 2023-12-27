import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.sfaxdroid.base.themes.appTypography
import com.sfaxdroid.base.themes.DarkColors
import com.sfaxdroid.base.themes.LightColors

@Composable
fun SfaxDroidTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (useDarkColors) DarkColors else LightColors,
        typography = appTypography,
        content = content
    )
}
