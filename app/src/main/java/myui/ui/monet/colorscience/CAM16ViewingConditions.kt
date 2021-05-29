package myui.ui.monet.colorscience

import myui.ui.monet.MathUtils
import kotlin.math.*

class CAM16ViewingConditions @JvmOverloads constructor(
    dArr: DoubleArray? = D65,
    d: Double = 40.0,
    d2: Double = 18.0,
    d3: Double = 2.0,
    z: Boolean = false
) {
    var mAdaptingLuminance = 0.0
    var mAw = 0.0
    var mBackgroundRelativeLuminance = 0.0
    var mBackgroundYToWhitepointY = 0.0
    var mC = 0.0
    var mDiscountingIlluminant = false
    val mDrgb: DoubleArray
    val mDrgbInverse: DoubleArray
    var mFL = 0.0
    var mFLRoot = 0.0
    private var mK = 0.0
    var mNC = 0.0
    var mNbb = 0.0
    var mNcb = 0.0
    var mSurround = 0.0
    private var mSurroundFactor = 0.0
    val mWhitepoint: DoubleArray
    var mZ = 0.0

    companion object {
        val D65 = doubleArrayOf(95.04705587, 100.0, 108.88287364)
        val DEFAULT = CAM16ViewingConditions()
    }

    fun adapt(d: Double): Double {
        val pow = (mFL * abs(d) * 0.01).pow(0.42)
        return sign(d) * 400.0 * pow / (pow + 27.13)
    }

    fun unadapt(d: Double): Double {
        val pow = 100.0 / mFL * 27.13.pow(1.0 / 0.42)
        val abs = abs(d)
        return sign(d) * pow * abs(abs / (400.0 - abs)).pow(1.0 / 0.42)
    }

    init {
        val d4: Double
        val d5: Double
        val d6: Double
        requireNotNull(dArr) { "Whitepoint is null" }
        if (dArr.size == 3) {
            mWhitepoint = dArr
            mAdaptingLuminance = d
            mBackgroundRelativeLuminance = d2
            mSurround = d3
            mDiscountingIlluminant = z
            d4 = if (d3 >= 1.0) {
                MathUtils.lerp(0.59, 0.69, d3 - 1.0)
            } else {
                MathUtils.lerp(0.525, 0.59, d3)
            }
            mC = d4
            d5 = if (d4 >= 0.59) {
                MathUtils.lerp(0.9, 1.0, (d4 - 0.59) / 0.1)
            } else {
                MathUtils.lerp(0.8, 0.9, (d4 - 0.525) / 0.065)
            }
            mSurroundFactor = d5
            mNC = d5
            val d10 = 1.0 / (d * 5.0 + 1.0)
            mK = d10
            val pow = d10.pow(4.0)
            val d11 = 1.0 - pow
            val pow2 = pow * d + 0.1 * d11 * d11 * (5.0 * d).pow(1.0 / 3.0)
            mFL = pow2
            mFLRoot = pow2.pow(0.25)
            val d12 = d2 / dArr[1]
            mBackgroundYToWhitepointY = d12
            mZ = sqrt(d12) + 1.48
            val pow3 = (if (d12 != 0.0) d12.pow(-0.2) else 0.0) * 0.725
            mNbb = pow3
            mNcb = pow3
            d6 = if (z) {
                1.0
            } else {
                ((1.0 - exp((-d - 42.0) / 92.0) / 3.6) * d5).coerceIn(0.0..1.0)
            }
            val matrixMultiply: DoubleArray =
                MathUtils.matrixMultiply(dArr, CAM16Color.XYZ_TO_LMS)
            val dArr3 = doubleArrayOf(
                MathUtils.lerp(1.0, dArr[1] / matrixMultiply[0], d6),
                MathUtils.lerp(1.0, dArr[1] / matrixMultiply[1], d6),
                MathUtils.lerp(1.0, dArr[1] / matrixMultiply[2], d6)
            )
            mDrgb = dArr3
            mDrgbInverse = doubleArrayOf(1.0 / dArr3[0], 1.0 / dArr3[1], 1.0 / dArr3[2])
            val dArr4 = doubleArrayOf(
                matrixMultiply[0] * dArr3[0],
                matrixMultiply[1] * dArr3[1],
                matrixMultiply[2] * dArr3[2]
            )
            val dArr5 = doubleArrayOf(adapt(dArr4[0]), adapt(dArr4[1]), adapt(dArr4[2]))
            mAw = pow3 * (dArr5[0] * 2.0 + dArr5[1] + dArr5[2] * 0.05)
        } else {
            throw IllegalArgumentException("Whitepoint needs 3 coordinates in XYZ space")
        }
    }
}