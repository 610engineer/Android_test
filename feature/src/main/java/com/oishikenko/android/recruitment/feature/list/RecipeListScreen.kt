package com.oishikenko.android.recruitment.feature.list

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.oishikenko.android.recruitment.data.model.CookingRecord
import com.oishikenko.android.recruitment.feature.R
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max

@OptIn(ExperimentalLayoutApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel()
) {
    val cookingRecords by viewModel.cookingRecords.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main"){
        composable("main") {
            ListRecipes(cookingRecords,navController)
        }
        composable("detailScreen/{description}/{date}",
        arguments = listOf(
//            navArgument("imageUrl"){
//                type= NavType.StringType
//                defaultValue = ""
//                nullable = true
//            },
            navArgument("description"){
                type= NavType.StringType
                defaultValue = ""
                nullable = true
            },
            navArgument("date"){
                type= NavType.StringType
                defaultValue = ""
                nullable = true
            },
        )) {
            DetailScreen(navController)
            }
        }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ListRecipes(cookingRecords:List<CookingRecord>, navController: NavController){
    Scaffold(
        topBar = {
            val imagePainter = painterResource(id = R.drawable.header)
            TopAppBar(
                backgroundColor = Color.White
            ) {
                Image(painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .consumedWindowInsets(innerPadding)
        ) {
            //cookingRecordsが空の時ローディングを表示
            if(cookingRecords.isEmpty()){
                item{
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(8.dp)
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    ) {
                        CustomCircularProgressIndicator(
                            color = Color.Blue,
                            strokeWidth = 8.dp
                        )
                    }
                }
            }else{
                items(cookingRecords) {
                    RecipeListItem(it, navController)
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewRecipeListScreen(){
    MaterialTheme {
        RecipeListScreen()
    }
}

// StrokeCapの指定ができないので、progressIndicator.ktの一部を流用
// StrokeCapをRoundにして角を丸くする
@Composable
fun CustomCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
    }

    val transition = rememberInfiniteTransition()
    // The current rotation around the circle, so we know where to start the rotation from
    val currentRotation by transition.animateValue(
        0,
        RotationsPerCycle,
        Int.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = RotationDuration * RotationsPerCycle,
                easing = LinearEasing
            )
        )
    )
    // How far forward (degrees) the base point should be from the start point
    val baseRotation by transition.animateFloat(
        0f,
        BaseRotationAngle,
        infiniteRepeatable(
            animation = tween(
                durationMillis = RotationDuration,
                easing = LinearEasing
            )
        )
    )
    // How far forward (degrees) both the head and tail should be from the base point
    val endAngle by transition.animateFloat(
        0f,
        JumpRotationAngle,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = HeadAndTailAnimationDuration + HeadAndTailDelayDuration
                0f at 0 with CircularEasing
                JumpRotationAngle at HeadAndTailAnimationDuration
            }
        )
    )

    val startAngle by transition.animateFloat(
        0f,
        JumpRotationAngle,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = HeadAndTailAnimationDuration + HeadAndTailDelayDuration
                0f at HeadAndTailDelayDuration with CircularEasing
                JumpRotationAngle at durationMillis
            }
        )
    )
    Canvas(
        modifier
            .progressSemantics()
            .size(CircularIndicatorDiameter)
    ) {

        val currentRotationAngleOffset = (currentRotation * RotationAngleOffset) % 360f

        // How long a line to draw using the start angle as a reference point
        val sweep = abs(endAngle - startAngle)

        // Offset by the constant offset and the per rotation offset
        val offset = StartAngleOffset + currentRotationAngleOffset + baseRotation
        drawIndeterminateCircularIndicator(startAngle + offset, strokeWidth, sweep, color, stroke)
    }
}

private fun DrawScope.drawIndeterminateCircularIndicator(
    startAngle: Float,
    strokeWidth: Dp,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // Length of arc is angle * radius
    // Angle (radians) is length / radius
    // The length should be the same as the stroke width for calculating the min angle
    val squareStrokeCapOffset =
        (180.0 / PI).toFloat() * (strokeWidth / (CircularIndicatorDiameter / 2)) / 2f

    // Adding a square stroke cap draws half the stroke width behind the start point, so we want to
    // move it forward by that amount so the arc visually appears in the correct place
    val adjustedStartAngle = startAngle + squareStrokeCapOffset

    // When the start and end angles are in the same place, we still want to draw a small sweep, so
    // the stroke caps get added on both ends and we draw the correct minimum length arc
    val adjustedSweep = max(sweep, 0.1f)

    drawCircularIndicator(adjustedStartAngle, adjustedSweep, color, stroke)
}

private fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}


private val CircularIndicatorDiameter = 40.dp

// The animation comprises of 5 rotations around the circle forming a 5 pointed star.
// After the 5th rotation, we are back at the beginning of the circle.
private const val RotationsPerCycle = 5

// Each rotation is 1 and 1/3 seconds, but 1332ms divides more evenly
private const val RotationDuration = 1332

// When the rotation is at its beginning (0 or 360 degrees) we want it to be drawn at 12 o clock,
// which means 270 degrees when drawing.
private const val StartAngleOffset = -90f

// How far the base point moves around the circle
private const val BaseRotationAngle = 286f

// How far the head and tail should jump forward during one rotation past the base point
private const val JumpRotationAngle = 290f

// Each rotation we want to offset the start position by this much, so we continue where
// the previous rotation ended. This is the maximum angle covered during one rotation.
private const val RotationAngleOffset = (BaseRotationAngle + JumpRotationAngle) % 360f

// The head animates for the first half of a rotation, then is static for the second half
// The tail is static for the first half and then animates for the second half
private const val HeadAndTailAnimationDuration = (RotationDuration * 0.5).toInt()
private const val HeadAndTailDelayDuration = HeadAndTailAnimationDuration

// The easing for the head and tail jump
private val CircularEasing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
