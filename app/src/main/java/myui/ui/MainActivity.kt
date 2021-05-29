package myui.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import myui.ui.monet.ColorScheme
import myui.ui.monet.colorscience.MonetColor
import myui.ui.theme.MYUITheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MYUITheme {
                val systemUiController = rememberSystemUiController()
                var themeColorText by remember { mutableStateOf(TextFieldValue("0000ff")) }
                var themeColorText2 by remember { mutableStateOf("0000ff") }
                LaunchedEffect(themeColorText.text) {
                    themeColorText2 = themeColorText.text
                    while (themeColorText2.length < 6) {
                        themeColorText2 = "f$themeColorText2"
                    }
                }
                val themeColor = Color("ff$themeColorText2".toLong(16))
                val colorScheme = ColorScheme(themeColor.toArgb(), false)
                val (a1, a2, a3, n1, n2) = listOf(
                    colorScheme.allAccentColors.subList(0, 11),
                    colorScheme.allAccentColors.subList(11, 22),
                    colorScheme.allAccentColors.subList(22, 33),
                    colorScheme.allNeutralColors.subList(0, 11),
                    colorScheme.allNeutralColors.subList(11, 22)
                )
                val (ra1, ra2, ra3, rn1, rn2) = listOf(a1, a2, a3, n1, n2).map {
                    Color("ff${MonetColor(it[4]).hex.drop(1)}".toLong(16))
                }
                val bgColor = Color("ff${MonetColor(n1[0]).hex.drop(1)}".toLong(16))
                SideEffect {
                    systemUiController.setSystemBarsColor(bgColor)
                }

                Surface(
                    Modifier.fillMaxSize(),
                    color = bgColor
                ) {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        Spacer(Modifier.height(56.dp))
                        Text(
                            "Monet Color System",
                            Modifier.padding(24.dp, 48.dp),
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.h4
                        )
                        Row(
                            Modifier.padding(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                Modifier.size(48.dp),
                                shape = CircleShape,
                                color = themeColor
                            ) {}
                            Spacer(Modifier.width(24.dp))
                            TextField(
                                themeColorText,
                                { themeColorText = it },
                                Modifier.clip(CircleShape),
                                label = { Text("Theme color") },
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color(
                                        "ff${MonetColor(colorScheme.allAccentColors[4]).hex.drop(1)}"
                                            .toLong(16)
                                    ),
                                    backgroundColor = Color(
                                        "ff${MonetColor(colorScheme.allNeutralColors[1]).hex.drop(1)}"
                                            .toLong(16)
                                    ),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                        Card(
                            Modifier.padding(16.dp),
                            shape = RoundedCornerShape(32.dp),
                            backgroundColor = Color.White,
                            elevation = 0.dp
                        ) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { }
                                    .padding(32.dp)
                            ) {
                                Text(
                                    "Generate palette from photo",
                                    Modifier.align(Alignment.Center),
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium,
                                    style = MaterialTheme.typography.body1
                                )
                            }
                        }
                        Spacer(Modifier.height(48.dp))
                        Palette("A-1", a1, ra1)
                        Palette("A-2", a2, ra2)
                        Palette("A-3", a3, ra3)
                        Palette("N-1", n1, rn1)
                        Palette("N-2", n2, rn2)
                    }
                }
            }
        }
    }

    @Composable
    fun Palette(
        name: String,
        colors: List<Int>,
        rippleColor: Color
    ) {
        FlowRow {
            TextButton(name, rippleColor = rippleColor)
            colors.forEachIndexed { i, color ->
                TextButton(
                    (if (i == 0) 50 else i * 100).toString(),
                    Color("ff${MonetColor(color).hex.drop(1)}".toLong(16)),
                    rippleColor
                )
            }
        }
        Spacer(Modifier.height(32.dp))
    }

    @Composable
    fun TextButton(
        text: String,
        color: Color = Color.White,
        rippleColor: Color = Color.Transparent
    ) {
        val scope = rememberCoroutineScope()
        val radius = remember { Animatable(50f) }
        val rippleSize = remember { Animatable(0f) }
        val rippleAlpha = remember { Animatable(0f) }
        Box(
            Modifier
                .padding(4.dp)
                .size(60.dp)
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
                            rippleSize.animateTo(1f, spring(stiffness = 600f))
                        }
                        scope.launch {
                            rippleAlpha.animateTo(1f, spring(stiffness = 600f))
                        }
                        awaitPointerEventScope {
                            waitForUpOrCancellation()
                            scope.launch {
                                rippleSize.animateTo(1f, spring(stiffness = 600f))
                                scope.launch {
                                    rippleAlpha.animateTo(0f, spring(stiffness = 600f))
                                    rippleSize.animateTo(0f, spring(stiffness = 600f))
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
                color = rippleColor.copy(rippleAlpha.value)
            ) {}
            Text(
                text,
                Modifier.align(Alignment.Center),
                color = if (color.luminance() <= 0.5f) Color.White else Color.Black,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.body1
            )
        }
    }
}