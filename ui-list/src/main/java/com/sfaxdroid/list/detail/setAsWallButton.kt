package com.sfaxdroid.list.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sfaxdroid.detail.ActionTypeEnum
import com.sfaxdroid.detail.utils.DetailUtils


@Composable
fun SetAsWallButton(url: String?, viewModel: DetailViewModel) {
    val context = LocalContext.current

    Button(shape = RoundedCornerShape(15),
        onClick = {
            if (url != null) {
                DetailUtils
                    .saveToFileToTempsDirAndChooseAction(
                        url,
                        ActionTypeEnum
                            .SetAsWallpaper,
                        context,
                        viewModel.fileManager
                    ) { isSaved, action ->
                        if (isSaved) {

                        }
                    }
            }
        }) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = "set as wallpaper",
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "set as wallpaper",
            fontSize = 18.sp,
            style = MaterialTheme.typography.button
        )
    }
}