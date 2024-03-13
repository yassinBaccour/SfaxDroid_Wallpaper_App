package com.sfaxdoird.anim.img

import android.content.Context
import android.content.DialogInterface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.sfaxdroid.base.Constants
import com.sfaxdroid.base.utils.Utils

@Composable
fun WorldImg() {
    WorldImg(viewModel = hiltViewModel())
}

@Composable
internal fun WorldImg(
    viewModel: WordImgViewModel
) {

    val state by viewModel.state.collectAsState(
        initial = AnimImgViewState.Empty
    )

    val context = LocalContext.current

    WorldImg(state, selectColor = {}, selectLiveWallpaper = {
        Constants.ifBackgroundChanged = true
        Constants.nbIncrementationAfterChange = 0
        Utils.openLiveWallpaper<LwpService>(context)
    })
}

@Composable
internal fun WorldImg(
    state: AnimImgViewState,
    selectColor: () -> Unit,
    selectLiveWallpaper: () -> Unit
) {
    Column {
        Text(
            text = if (state.progressionInfo == ProgressionInfo.IdTwoCompleted) stringResource(R.string.download_completed) else stringResource(
                R.string.download_resource_txt_witing
            ),
            style = MaterialTheme.typography.h3,
            fontStyle = FontStyle.Italic
        )

        val progressValue = state.progressValue.first
        val progressByte = state.progressValue.second

        Text(
            text = when (state.progressionInfo) {
                is ProgressionInfo.IdOneCompleted -> stringResource(R.string.download_resource_completed)
                is ProgressionInfo.IdTwoCompleted -> stringResource(R.string.download_terminated_sucessful)
                ProgressionInfo.Error -> stringResource(R.string.failed_dwn)
                ProgressionInfo.Idle -> TODO()
            },
            style = MaterialTheme.typography.h3,
            fontStyle = FontStyle.Italic
        )
        Spacer(Modifier.height(30.dp))
        Text(
            text = if (progressValue == 0) stringResource(R.string.download_resource_txt_witing) else "$progressValue%  " +
                    Utils.getBytesDownloaded(
                        progressValue,
                        progressByte
                    ),
            style = MaterialTheme.typography.h6,
            fontStyle = FontStyle.Italic
        )

        LinearProgressIndicator(progress = progressValue.toFloat())
        Spacer(Modifier.height(30.dp))
        Text(
            text = stringResource(R.string.writing_color),
            style = MaterialTheme.typography.h6,
            fontStyle = FontStyle.Italic
        )
        Spacer(Modifier.height(10.dp))
        AsyncImage(
            model = Icons.Filled.Palette,
            contentDescription = "",
            modifier = Modifier
                .size(60.dp)
                .clickable {
                    selectColor()
                }
        )
        Button(
            onClick = { if (state.isOpenLwpButtonEnable) selectLiveWallpaper() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
        ) {
            Text(
                text = stringResource(R.string.create_animated_wallpapers),
                color = MaterialTheme.colors.secondary
            )
        }
        Spacer(Modifier.height(20.dp))
    }
}

private fun chooseColor(context: Context) {
    ColorPickerDialogBuilder
        .with(context)
        .setTitle(context.getString(R.string.choose_color))
        .initialColor(android.graphics.Color.BLUE)
        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
        .density(12)
        .setPositiveButton(
            context.getString(R.string.btn_ok)
        ) { _: DialogInterface?, _: Int, _: Array<Int?>? ->
            //viewModel.submitAction(AnimImgAction.ChangeColor(selectedColor))
        }
        .setNegativeButton(
            context.getString(R.string.btn_cancel)
        ) { _: DialogInterface?, _: Int -> }
        .build()
        .show()
}


@Preview
@Composable
fun WorldImgPreview() {
    WorldImg()
}