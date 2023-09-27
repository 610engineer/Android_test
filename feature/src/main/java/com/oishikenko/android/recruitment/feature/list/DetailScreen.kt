package com.oishikenko.android.recruitment.feature.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.oishikenko.android.recruitment.feature.R
import java.text.SimpleDateFormat

@Composable
fun DetailScreen(
    navController: NavController,
    imageUrl: String?,
    description: String?,
    date: String?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White
            ) {
                Text(
                    text = "レシピ",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)

        ) {
            // recipe image
            AsyncImage(
                model = imageUrl,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Fit,
            )

            // description
            if (description != null) {
                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = description,

                )
            }
            // date
            if (date != null) {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    text = convertTimeFormat(date),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

//登録時間表示形式変更
private fun convertTimeFormat(date: String): String {
    //dateに変換
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var convertedDate = formatter.parse(date)

    //formatを変えてStringに変換
    val convertedFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
    return convertedFormat.format(convertedDate)
}
