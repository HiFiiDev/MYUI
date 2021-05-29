package myui.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import myui.ui.monet.ColorScheme
import myui.ui.monet.colorscience.MonetColor
import myui.ui.theme.MYUITheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @SuppressLint("NewApi")
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MYUITheme {
                val themeColor = Color.Blue
                val colorScheme = ColorScheme(themeColor.toArgb(), false)
                Log.e(
                    "ColorScheme nc",
                    colorScheme.allNeutralColors.joinToString { MonetColor(it).hex.drop(1) }
                )
                Log.e(
                    "ColorScheme ac",
                    colorScheme.allAccentColors.joinToString { MonetColor(it).hex.drop(1) }
                )
                Surface(
                    Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(32.dp),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        Card(
                            Modifier.fillMaxWidth(),
                            backgroundColor = themeColor
                        ) {
                            Box(
                                Modifier.padding(32.dp)
                            ) {
                                Text(
                                    "Monet Color System",
                                    Modifier.align(Alignment.Center),
                                    color = if (themeColor.luminance() <= 0.5f) Color.White else Color.Black,
                                    style = MaterialTheme.typography.h4
                                )
                            }
                        }

                        LazyVerticalGrid(
                            cells = GridCells.Fixed(4),
                            Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            item { TextButton("N-1") }
                            itemsIndexed(
                                listOf(
                                    Color(0xfff1effb),
                                    Color(0xffe3e1ec),
                                    Color(0xffc7c5cf),
                                    Color(0xffabaab5),
                                    Color(0xff90909a),
                                    Color(0xff76757e),
                                    Color(0xff5e5d67),
                                    Color(0xff46464f),
                                    Color(0xff2f3038),
                                    Color(0xff1b1b21),
                                    Color(0xff000002)
                                )
                            ) { i, color ->
                                TextButton((if (i == 0) 50 else i * 100).toString(), color)
                            }
                            items(4) { Spacer(Modifier.height(48.dp)) }

                            item { TextButton("N-2") }
                            itemsIndexed(
                                listOf(
                                    Color(0xfff1f0f5),
                                    Color(0xffe3e2e7),
                                    Color(0xffc7c6ca),
                                    Color(0xffababaf),
                                    Color(0xff919095),
                                    Color(0xff76757a),
                                    Color(0xff5e5e62),
                                    Color(0xff46464a),
                                    Color(0xff303034),
                                    Color(0xff1b1b1f),
                                    Color(0xff000000)
                                )
                            ) { i, color ->
                                TextButton((if (i == 0) 50 else i * 100).toString(), color)
                            }
                            items(4) { Spacer(Modifier.height(48.dp)) }

                            item { TextButton("A-1") }
                            itemsIndexed(
                                listOf(
                                    Color(0xffeaf1ff),
                                    Color(0xffdbe1ff),
                                    Color(0xffbcc2ff),
                                    Color(0xff9da4fd),
                                    Color(0xff8389e0),
                                    Color(0xff686ec3),
                                    Color(0xff5056a9),
                                    Color(0xff383e8f),
                                    Color(0xff202578),
                                    Color(0xff060864),
                                    Color(0xff000000)
                                )
                            ) { i, color ->
                                TextButton((if (i == 0) 50 else i * 100).toString(), color)
                            }
                            items(4) { Spacer(Modifier.height(48.dp)) }

                            item { TextButton("A-2") }
                            itemsIndexed(
                                listOf(
                                    Color(0xfff0efff),
                                    Color(0xffe0e0fb),
                                    Color(0xffc4c4de),
                                    Color(0xffa9a9c2),
                                    Color(0xff8e8fa6),
                                    Color(0xff74748b),
                                    Color(0xff5c5d72),
                                    Color(0xff44455a),
                                    Color(0xff2d2f42),
                                    Color(0xff191a2c),
                                    Color(0xff00000b)
                                )
                            ) { i, color ->
                                TextButton((if (i == 0) 50 else i * 100).toString(), color)
                            }
                            items(4) { Spacer(Modifier.height(48.dp)) }

                            item { TextButton("A-3") }
                            itemsIndexed(
                                listOf(
                                    Color(0xffffebff),
                                    Color(0xffffd6fd),
                                    Color(0xfff2b4db),
                                    Color(0xffd499bf),
                                    Color(0xffb880a3),
                                    Color(0xff9a6688),
                                    Color(0xff804e70),
                                    Color(0xff663757),
                                    Color(0xff4c2140),
                                    Color(0xff330c29),
                                    Color(0xff020004)
                                )
                            ) { i, color ->
                                TextButton((if (i == 0) 50 else i * 100).toString(), color)
                            }
                            items(4) { Spacer(Modifier.height(48.dp)) }

                            items(3) {
                                TextButton("%", Color(0xFFD1E5C5))
                            }
                            items(15) {
                                TextButton("1")
                            }
                            item {
                                TextButton("=", Color(0xFFBAF39D))
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TextButton(
        text: String,
        color: Color = Color.White,
        rippleColor: Color = Color(0xFFA1B299)
    ) {
        val scope = rememberCoroutineScope()
        val radius = remember { Animatable(50f) }
        val ripple = remember { Animatable(color) }
        val rippleSize = remember { Animatable(0f) }
        Box(
            Modifier
                .padding(4.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(radius.value.roundToInt()))
                .background(color)
                .clickable { }
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        scope.launch {
                            radius.animateTo(25f, spring(stiffness = Spring.StiffnessHigh))
                            radius.animateTo(50f, spring(stiffness = Spring.StiffnessLow))
                        }
                        scope.launch {
                            ripple.animateTo(rippleColor)
                        }
                        scope.launch {
                            rippleSize.animateTo(1f, spring(stiffness = 600f))
                        }
                        awaitPointerEventScope {
                            waitForUpOrCancellation()
                            scope.launch {
                                ripple.animateTo(rippleColor)
                            }
                            scope.launch {
                                rippleSize.animateTo(1f, spring(stiffness = 600f))
                                scope.launch {
                                    ripple.animateTo(color, spring(stiffness = Spring.StiffnessLow))
                                }
                                scope.launch {
                                    rippleSize.animateTo(
                                        0f,
                                        spring(stiffness = Spring.StiffnessLow)
                                    )
                                }
                            }
                        }
                    })
                }
        ) {
            Surface(
                Modifier
                    .fillMaxSize(rippleSize.value)
                    .align(Alignment.Center),
                shape = CircleShape,
                color = ripple.value
            ) {}
            Text(
                text,
                Modifier.align(Alignment.Center),
                color = if (color.luminance() <= 0.5f) Color.White else Color.Black,
                style = MaterialTheme.typography.h5
            )
        }
    }
}