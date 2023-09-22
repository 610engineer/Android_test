package com.oishikenko.android.recruitment.feature.list

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.oishikenko.android.recruitment.data.model.CookingRecord

@Composable
fun RecipeListItem(
    cookingRecord: CookingRecord
) {
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
            Text(
            text = ConvertTypeToJP(cookingRecord.recipeType),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            fontWeight = FontWeight.Bold
            )
            Text(
                text = cookingRecord.recordedAt,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
            )

        }
//

    }
}

@Preview
@Composable
fun PreviewRecipeListItem() {
    RecipeListItem(
        cookingRecord = CookingRecord(
            imageUrl= "",
            comment = "豚肉のコクとごぼうの香り、野菜の甘みで奥行きのある味わい。",
            recipeType = "soup",
            recordedAt = "2018-05-01 17:57:31"
        )
    )
}

fun ConvertTypeToJP(recipeType : String) : String{
    var convertedType = ""
    when (recipeType){
        "soup" -> convertedType = "スープ"
        "main_dish" -> convertedType = "主催/主食"
        "side_dish" -> convertedType = "副菜"
    }
    return convertedType
}