package myui.ui

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
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import myui.ui.monet.ColorScheme
import myui.ui.monet.colorscience.MonetColor
import myui.ui.theme.MYUITheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MYUITheme {
                var themeColorText by remember {
                    mutableStateOf(TextFieldValue(MonetColor(Color.Blue.toArgb()).hex.drop(1)))
                }
                val themeColor = Color(themeColorText.text.toLong(16))
                val colorScheme = ColorScheme(themeColor.toArgb(), false)
                Log.e(
                    "ColorScheme nc",
                    colorScheme.allNeutralColors.joinToString { MonetColor(it).hex.drop(1) }
                )
                Log.e(
                    "ColorScheme ac",
                    colorScheme.allAccentColors.joinToString { MonetColor(it).hex.drop(1) }
                )
                val (n1, n2, a1, a2, a3) = listOf(
                    colorScheme.allNeutralColors.subList(0, 11),
                    colorScheme.allNeutralColors.subList(11, 22),
                    colorScheme.allAccentColors.subList(0, 11),
                    colorScheme.allAccentColors.subList(11, 22),
                    colorScheme.allAccentColors.subList(22, 33)
                )
                val (rn1, rn2, ra1, ra2, ra3) = listOf(n1, n2, a1, a2, a3).map {
                    Color("ff${MonetColor(it[4]).hex.drop(1)}".toLong(16))
                }

                Surface(
                    Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(32.dp),
                    color = Color(
                        "ff${MonetColor(colorScheme.allNeutralColors[0]).hex.drop(1)}".toLong(
                            16
                        )
                    )
                ) {
                    Column {
                        Card(
                            Modifier.fillMaxWidth(),
                            backgroundColor = themeColor
                        ) {
                            Column(
                                Modifier.padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CompositionLocalProvider(LocalContentColor provides if (themeColor.luminance() <= 0.5f) Color.White else Color.Black) {
                                    Text(
                                        "Monet Color System",
                                        style = MaterialTheme.typography.h4
                                    )
                                    TextField(
                                        themeColorText,
                                        { themeColorText = it },
                                        label = {
                                            CompositionLocalProvider(LocalContentColor provides if (themeColor.luminance() <= 0.5f) Color.White else Color.Black) {
                                                Text("Theme color")
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        LazyVerticalGrid(
                            cells = GridCells.Fixed(4),
                            Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            item { TextButton("N-1", rippleColor = rn1) }
                            itemsIndexed(n1) { i, color ->
                                TextButton(
                                    (if (i == 1) 50 else i * 100).toString(),
                                    Color("ff${MonetColor(color).hex.drop(1)}".toLong(16)),
                                    rn1
                                )
                            }
                            items(4) { Spacer(Modifier.height(48.dp)) }

                            item { TextButton("N-2", rippleColor = rn2) }
                            itemsIndexed(n2) { i, color ->
                                TextButton(
                                    (if (i == 1) 50 else i * 100).toString(),
                                    Color("ff${MonetColor(color).hex.drop(1)}".toLong(16)),
                                    rn2
                                )
                            }
                            items(4) { Spacer(Modifier.height(48.dp)) }

                            item { TextButton("A-1", rippleColor = ra1) }
                            itemsIndexed(a1) { i, color ->
                                TextButton(
                                    (if (i == 1) 50 else i * 100).toString(),
                                    Color("ff${MonetColor(color).hex.drop(1)}".toLong(16)),
                                    ra1
                                )
                            }
                            items(4) { Spacer(Modifier.height(48.dp)) }

                            item { TextButton("A-2", rippleColor = ra2) }
                            itemsIndexed(a2) { i, color ->
                                TextButton(
                                    (if (i == 1) 50 else i * 100).toString(),
                                    Color("ff${MonetColor(color).hex.drop(1)}".toLong(16)),
                                    ra2
                                )
                            }
                            items(4) { Spacer(Modifier.height(48.dp)) }

                            item { TextButton("A-3", rippleColor = ra3) }
                            itemsIndexed(a3) { i, color ->
                                TextButton(
                                    (if (i == 1) 50 else i * 100).toString(),
                                    Color("ff${MonetColor(color).hex.drop(1)}".toLong(16)),
                                    ra3
                                )
                            }
                            items(4) { Spacer(Modifier.height(48.dp)) }
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
        rippleColor: Color = Color.Transparent
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