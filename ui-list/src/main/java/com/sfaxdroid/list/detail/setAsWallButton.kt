package com.sfaxdroid.list.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sfaxdroid.detail.ActionTypeEnum
import com.sfaxdroid.detail.utils.DetailUtils


@Composable
fun BoxScope.SetAsWallButton(url: String?, viewModel: DetailViewModel) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(bottom = 60.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically


    ){
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
}