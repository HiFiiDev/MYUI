package myui.ui.monet.colorscience

import android.util.Log
import myui.ui.monet.MathUtils
import kotlin.math.*

class CAM16Color private constructor(
    monetColor: MonetColor,
    cAM16ViewingConditions: CAM16ViewingConditions
) {
    val C: Double
    val J: Double
    val M: Double
    val Q: Double
    val h: Double
    val color: MonetColor
    private val mViewingConditions: CAM16ViewingConditions
    val s: Double
    val ucs: DoubleArray

    companion object {
        private val DEBUG: Boolean = true
        private val LMS_TO_XYZ = arrayOf(
            doubleArrayOf(1.862067855087233, -1.011254630531684, 0.1491867754444517),
            doubleArrayOf(0.3875265432361371, 0.6214474419314753, -0.008973985167612517),
            doubleArrayOf(-0.01584149884933386, -0.03412293802851557, 1.04996443687785)
        )
        val XYZ_TO_LMS = arrayOf(
            doubleArrayOf(0.401288, 0.650173, -0.051461),
            doubleArrayOf(-0.250268, 1.204414, 0.045854),
            doubleArrayOf(-0.002079, 0.048952, 0.953127)
        )

        fun fromRgb(i: Int): CAM16Color {
            return CAM16Color(MonetColor(i))
        }

        private fun relativeLuminance(d: Double, d2: Double, d3: Double): Double {
            return MonetColor.fromXyz(xyzFromJch(d, d2, d3)).xyz[1]
        }

        fun binarySearchForYByJ(d: Double, d2: Double, d3: Double): Double {
            var d4 = 0.0
            var d5 = 100.0
            var d6 = -1.0
            var d7 = -1.0
            while (abs(d4 - d5) > 0.1) {
                val d8 = (d5 - d4) / 2.0 + d4
                val relativeLuminance = relativeLuminance(d8, d2, d3)
                val abs = abs(relativeLuminance - d)
                if (d7 == -1.0 || abs < d7) {
                    d6 = d8
                    d7 = abs
                }
                if (relativeLuminance < d) {
                    d4 = d8
                } else {
                    d5 = d8
                }
            }
            return d6
        }

        fun gamutMap(d: Double, d2: Double, d3: Double): MonetColor {
            val d4 = d
            val binarySearchForYByJ = binarySearchForYByJ(d, d2, d3)
            val fromXyz = MonetColor.fromXyz(xyzFromJch(binarySearchForYByJ, d2, d3))
            val abs = abs(d3 - CAM16Color(fromXyz).h)
            val abs2 = abs(d4 - fromXyz.xyz[1])
            if (abs <= 1.0 && abs2 <= 1.0) {
                return fromXyz
            }
            val z = DEBUG
            if (z) {
                Log.d(
                    "CAM16Color",
                    "launching second search because hueDelta is $abs and y delta is $abs2"
                )
            }
            val d5 = d
            val str = "CAM16Color"
            val d6 = abs2
            val binarySearchForYByChroma =
                binarySearchForYByChroma(d5, 0.0, d2, binarySearchForYByJ, d3)
            val d7 = d3
            val fromXyz2 = MonetColor.fromXyz(
                xyzFromJch(
                    binarySearchForYByJ(d5, binarySearchForYByChroma, d7),
                    binarySearchForYByChroma,
                    d7
                )
            )
            val cAM16Color = CAM16Color(fromXyz2)
            val abs3 = abs(d3 - cAM16Color.h)
            val abs4 = abs(d2 - cAM16Color.C)
            val abs5 = abs(d4 - fromXyz2.xyz[1])
            val monetColor = fromXyz
            val str2 = " hue delta = "
            val d8 = abs4
            if ((d6 <= 1.0 || abs5 > d6) && (abs5 >= 1.0 && abs5 >= d6 || abs3 >= abs)) {
                val str3 = str2
                val d9 = d8
                if (z) {
                    val d10 = abs3
                    Log.d(
                        str,
                        "round #2 was worse; asked for y ${d4.toInt()} got ${fromXyz2.xyz[1].toInt()} at C = ${cAM16Color.C} J = ${cAM16Color.J}. Error y = ${abs5.toInt()} chroma = ${d9.toInt()}$str3$d10"
                    )
                }
                return monetColor
            }
            if (z) {
                Log.d(
                    str,
                    "round #2 was better; asked for y ${d4.toInt()} got ${fromXyz2.xyz[1].toInt()} at C = ${cAM16Color.C} J = ${cAM16Color.J}. Error y = ${abs5.toInt()} chroma = ${d8.toInt()}$str2$abs3"
                )
            }
            return fromXyz2
        }

        fun binarySearchForYByChroma(
            d: Double,
            d2: Double,
            d3: Double,
            d4: Double,
            d5: Double
        ): Double {
            val z = relativeLuminance(d4, d2, d5) > relativeLuminance(d4, d3, d5)
            var d6 = d3
            var d7 = 0.0
            var d8 = -1.0
            var d9 = d2
            while (abs(d9 - d6) > 0.1) {
                val d10 = (d6 - d9) / 2.0 + d9
                val relativeLuminance = relativeLuminance(d4, d10, d5)
                val abs = abs(relativeLuminance - d)
                if (d8 == -1.0 || abs < d8) {
                    d7 = d10
                    d8 = abs
                }
                if (if (!z) relativeLuminance <= d else relativeLuminance > d) {
                    d9 = d10
                } else {
                    d6 = d10
                }
            }
            return d7
        }

        fun xyzFromJch(d: Double, d2: Double, d3: Double): DoubleArray {
            var d4 = 0.0
            if (d2 >= 0.0) {
                val sqrt = sqrt(d) * 0.1
                if (sqrt != 0.0) {
                    d4 = d2 / sqrt
                }
                return xyzFromJrootAlphaHue(sqrt, d4, d3, CAM16ViewingConditions.DEFAULT)
            }
            throw IllegalArgumentException()
        }

        private fun xyzFromJrootAlphaHue(
            d: Double,
            d2: Double,
            d3: Double,
            cAM16ViewingConditions: CAM16ViewingConditions
        ): DoubleArray {
            val cAM16ViewingConditions2: CAM16ViewingConditions = cAM16ViewingConditions
            val radians = Math.toRadians(d3)
            val pow =
                ((1.64 - 0.29.pow(cAM16ViewingConditions2.mBackgroundYToWhitepointY)).pow(-0.73) * d2).pow(
                    1.0 / 0.9
                )
            val pow2: Double = cAM16ViewingConditions2.mAw * d.pow(
                2.0 / cAM16ViewingConditions2.mC / (sqrt(
                    cAM16ViewingConditions2.mBackgroundYToWhitepointY
                ) + 1.48)
            )
            val cos: Double =
                cAM16ViewingConditions2.mNC * 50000.0 / 13.0 * cAM16ViewingConditions2.mNcb * (cos(
                    radians + 2.0
                ) + 3.8) * 0.25
            val d4: Double = pow2 / cAM16ViewingConditions2.mNbb
            val cos2 = cos(radians)
            val sin = sin(radians)
            val d5 = (0.305 + d4) * 23.0 * pow / (cos * 23.0 + pow * (11.0 * cos2 + 108.0 * sin))
            val d6 = cos2 * d5
            val d7 = d5 * sin
            val d8 = d4 * 460.0
            val dArr = doubleArrayOf(
                (451.0 * d6 + d8 + 288.0 * d7) / 1403.0,
                (d8 - 891.0 * d6 - 261.0 * d7) / 1403.0,
                (d8 - d6 * 220.0 - d7 * 6300.0) / 1403.0
            )
            dArr[0] = cAM16ViewingConditions2.unadapt(dArr[0])
            dArr[1] = cAM16ViewingConditions2.unadapt(dArr[1])
            dArr[2] = cAM16ViewingConditions2.unadapt(dArr[2])
            return MathUtils.matrixMultiply(
                multiply(dArr, cAM16ViewingConditions2.mDrgbInverse),
                LMS_TO_XYZ
            )
        }

        private fun multiply(dArr: DoubleArray, dArr2: DoubleArray): DoubleArray {
            val dArr3 = DoubleArray(dArr.size)
            for (i in dArr.indices) {
                dArr3[i] = dArr[i] * dArr2[i]
            }
            return dArr3
        }
    }

    constructor(monetColor: MonetColor) : this(monetColor, CAM16ViewingConditions.DEFAULT)

    override fun toString(): String {
        return "CAM16Color{color=${color.hex}, J=${J.toInt()}, C=${C.toInt()}, h=${h.toInt()}, Q=${Q.toInt()}, M=${M.toInt()}, s=${s.toInt()}}"
    }

    init {
        val cAM16ViewingConditions2: CAM16ViewingConditions = cAM16ViewingConditions
        mViewingConditions = cAM16ViewingConditions2
        color = monetColor
        val multiply = multiply(
            MathUtils.matrixMultiply(monetColor.xyz, XYZ_TO_LMS),
            cAM16ViewingConditions2.mDrgb
        )
        val dArr = doubleArrayOf(
            cAM16ViewingConditions2.adapt(multiply[0]), cAM16ViewingConditions2.adapt(
                multiply[1]
            ), cAM16ViewingConditions2.adapt(multiply[2])
        )
        val d = dArr[0]
        val d2 = dArr[1]
        val d3 = dArr[2]
        var d4 = (-12.0 * d2 + d3) / 11.0 + d
        val d5 = d + d2
        var d6 = (d5 - d3 * 2.0) / 9.0
        var atan2 = atan2(d6, d4)
        var degrees = Math.toDegrees(atan2)
        if (abs(d4) < 0.007 && abs(d6) < 0.007) {
            d6 = 0.0
            d4 = 0.0
            atan2 = 0.0
            degrees = 0.0
        }
        h = if (degrees < 0.0) degrees + 360.0 else degrees
        val pow =
            (cAM16ViewingConditions2.mNbb * (d * 2.0 + d2 + 0.05 * d3) / cAM16ViewingConditions2.mAw).pow(
                cAM16ViewingConditions2.mC * 0.5 * cAM16ViewingConditions2.mZ
            )
        val d7 = 100.0 * pow * pow
        J = d7
        Q =
            4.0 / cAM16ViewingConditions2.mC * pow * (cAM16ViewingConditions2.mAw + 4.0) * cAM16ViewingConditions2.mFLRoot
        val pow2 =
            (cAM16ViewingConditions2.mNC * 50000.0 / 13.0 * cAM16ViewingConditions2.mNcb * ((cos(
                atan2 + 2.0
            ) + 3.8) * 0.25) * sqrt(
                d4 * d4 + d6 * d6
            ) / ((d5 + (d3 * 1.05)) + 0.305)).pow(0.9) * (1.64 - 0.29.pow(cAM16ViewingConditions2.mBackgroundYToWhitepointY)).pow(
                0.73
            )
        val d8 = pow * pow2
        C = d8
        val d9: Double = d8 * cAM16ViewingConditions2.mFLRoot
        M = d9
        s = sqrt((cAM16ViewingConditions2.mC * pow2) / (cAM16ViewingConditions2.mAw + 4.0)) * 50.0
        val log = ln((d9 * 0.0228) + 1.0) / 0.0228
        ucs = doubleArrayOf(
            (1.7 * d7) / ((d7 * 0.007) + 1.0),
            cos(atan2) * log,
            log * sin(atan2)
        )
    }
}