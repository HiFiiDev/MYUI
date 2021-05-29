package myui.ui.monet.colorscience

import myui.ui.monet.MathUtils
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt

class MonetColor(val argb: Int) {
    val hex: String
    val lstar: Double
    val rgb = toRgb(argb)
    val xyz: DoubleArray

    override fun toString(): String {
        return "Color{hex='$hex', argb=$argb, xyz=$xyz, rgb=$rgb}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || MonetColor::class.java != other.javaClass) {
            return false
        }
        return argb == (other as MonetColor).argb
    }

    override fun hashCode(): Int {
        return Objects.hash(arrayOf(Integer.valueOf(argb)))
    }

    companion object {
        var srgbToXyz = arrayOf(
            doubleArrayOf(0.4124, 0.3576, 0.1805),
            doubleArrayOf(0.2126, 0.7152, 0.0722),
            doubleArrayOf(0.0193, 0.1192, 0.9505)
        )
        var xyzToSrgb = arrayOf(
            doubleArrayOf(3.2406, -1.5372, -0.4986),
            doubleArrayOf(-0.9689, 1.8758, 0.0415),
            doubleArrayOf(0.0557, -0.204, 1.057)
        )

        private fun getBlue(i: Int): Int {
            return i and 255
        }

        private fun getGreen(i: Int): Int {
            return i and 65280 shr 8
        }

        private fun getRed(i: Int): Int {
            return i and 16711680 shr 16
        }

        fun toRgb(i: Int): DoubleArray {
            return doubleArrayOf(
                getRed(i).toDouble(),
                getGreen(i).toDouble(), getBlue(i).toDouble()
            )
        }

        fun toHex(i: Int): String {
            var hexString = Integer.toHexString(i)
            while (hexString.length < 6) {
                hexString = "0$hexString"
            }
            return "#$hexString"
        }

        fun toXyz(i: Int): DoubleArray {
            val rgb = toRgb(i)
            return MathUtils.matrixMultiply(
                doubleArrayOf(
                    linearized(rgb[0] / 255.0) * 100.0, linearized(
                        rgb[1] / 255.0
                    ) * 100.0, linearized(rgb[2] / 255.0) * 100.0
                ), srgbToXyz
            )
        }

        fun fromXyz(dArr: DoubleArray): MonetColor {
            return MonetColor(intFromXyz(dArr))
        }

        fun intFromXyz(dArr: DoubleArray): Int {
            val matrixMultiply: DoubleArray = MathUtils.matrixMultiply(
                doubleArrayOf(
                    dArr[0] / 100.0,
                    dArr[1] / 100.0,
                    dArr[2] / 100.0
                ), xyzToSrgb
            )
            val delinearized = delinearized(matrixMultiply[0])
            val delinearized2 = delinearized(matrixMultiply[1])
            val delinearized3 = delinearized(matrixMultiply[2])
            return intFromRgb(
                intArrayOf(
                    max(min(255.0, delinearized * 255.0), 0.0).roundToInt(),
                    max(min(255.0, delinearized2 * 255.0), 0.0).roundToInt(),
                    max(min(255.0, delinearized3 * 255.0), 0.0).roundToInt()
                )
            )
        }

        fun intFromRgb(iArr: IntArray): Int {
            return iArr[2] and 255 or (iArr[0] and 255 shl 16) or (iArr[1] and 255 shl 8)
        }

        fun linearized(d: Double): Double {
            return if (d <= 0.04045) d / 12.92 else ((d + 0.055) / 1.055).pow(2.4)
        }

        fun delinearized(d: Double): Double {
            return if (d <= 0.0031308) d * 12.92 else d.pow(0.4166666666666667) * 1.055 - 0.055
        }
    }

    val y: Float
        get() = xyz[1].toFloat()

    init {
        val xyz = toXyz(argb)
        this.xyz = xyz
        hex = toHex(argb)
        lstar = Contrast.yToLstar(xyz[1])
    }
}