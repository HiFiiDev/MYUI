package myui.ui.monet.colorscience

import myui.ui.monet.MathUtils
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
        return arrayOf(Integer.valueOf(argb)).hashCode()
    }

    companion object {
        var srgbToXyz = arrayOf(
            doubleArrayOf(0.4124574456426238, 0.3575758652297323, 0.1804372478276439),
            doubleArrayOf(0.2126733704094779, 0.7151517304594646, 0.07217489913105756),
            doubleArrayOf(0.01933394276449797, 0.1191919550765775, 0.9503028385589246)
        )
        var xyzToSrgb = arrayOf(
            doubleArrayOf(3.240446254174338, -1.537134761595519, -0.4985301929498981),
            doubleArrayOf(-0.9692666062874638, 1.876011959871178, 0.04155604221626438),
            doubleArrayOf(0.05564350356396922, -0.2040261797345535, 1.057226567715413)
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
            var hexString = i.toString(16)
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
                dArr.map { it / 100.0 }.toDoubleArray(),
                xyzToSrgb
            )
            return intFromRgb(
                matrixMultiply.map {
                    (delinearized(it) * 255.0).coerceIn(0.0..255.0).roundToInt()
                }.toIntArray()
            )
        }

        fun intFromRgb(iArr: IntArray): Int {
            return iArr[2] and 255 or (iArr[0] and 255 shl 16) or (iArr[1] and 255 shl 8)
        }

        fun linearized(d: Double): Double {
            return if (d <= 0.04045) d / 12.92 else ((d + 0.055) / 1.055).pow(2.4)
        }

        fun delinearized(d: Double): Double {
            return if (d <= 0.0031308) d * 12.92 else d.pow(1.0 / 2.4) * 1.055 - 0.055
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