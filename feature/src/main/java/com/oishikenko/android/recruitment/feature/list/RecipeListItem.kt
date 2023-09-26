package com.oishikenko.android.recruitment.feature.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.oishikenko.android.recruitment.data.model.CookingRecord
import com.oishikenko.android.recruitment.feature.R

@Composable
fun RecipeListItem(
    cookingRecord: CookingRecord,
    navController: NavController
) {
    Column(
        modifier = Modifier.clickable {
            navController.navigate("detailScreen/${cookingRecord.comment}/${cookingRecord.recordedAt}")
        }
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .padding(top = 8.dp, bottom = 8.dp)
                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))

        ) {
            AsyncImage(
                model = cookingRecord.imageUrl,
                contentDescription = cookingRecord.comment,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(4.dp)),
            )
            Column(
                modifier = Modifier
                    .padding(top = 25.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                //料理タイプ
                Text(
                    text = convertTypeToJP(cookingRecord.recipeType),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                //登録日
                Text(
                    text = cookingRecord.recordedAt,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                )
            }
        }
    }

}


//料理タイプを日本語に変換
fun convertTypeToJP(recipeType : String) : String{
    var convertedType = ""
    when (recipeType){
        "soup" -> convertedType = "スープ"
        "main_dish" -> convertedType = "主催/主食"
        "side_dish" -> convertedType = "副菜"
    }
    return convertedType
}