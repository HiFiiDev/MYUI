package myui.ui.monet.colorscience

import kotlin.math.pow

object Contrast {
    fun lstarToY(d: Double): Double {
        return (if (d > 8.0) ((d + 16.0) / 116.0).pow(3.0) else d / 903.0) * 100.0
    }

    fun yToLstar(d: Double): Double {
        val d2 = d / 100.0
        return if (d2 <= 0.008856451679035631) d2 * 24389.0 / 27.0 else Math.cbrt(d2) * 116.0 - 16.0
    }
}