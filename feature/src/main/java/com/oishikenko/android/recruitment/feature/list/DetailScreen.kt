package com.oishikenko.android.recruitment.feature.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.oishikenko.android.recruitment.feature.R

@Composable
fun DetailScreen(
    navController: NavController
) {
    val navBackStackEntry = navController.previousBackStackEntry
    val imageUrl = navBackStackEntry?.arguments?.getString("image")
    val description = navBackStackEntry?.arguments?.getString("description")
    val date = navBackStackEntry?.arguments?.getString("date")
    Scaffold(
        topBar = {
            val imagePainter = painterResource(id = R.drawable.header)
            TopAppBar(
                backgroundColor = Color.White
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .padding(top = 8.dp, bottom = 8.dp)

        ) {
            // recipe image
            AsyncImage(
                model = imageUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(4.dp)),
            )

            // description
            if (description != null) {
                Text(
                    text = description
                )
            }
            // date
            if (date != null) {
                Text(
                    text = date
                )
            }
        }
    }
}
