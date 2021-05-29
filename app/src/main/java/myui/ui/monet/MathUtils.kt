package myui.ui.monet

object MathUtils {
    fun lerp(d: Double, d2: Double, d3: Double): Double {
        return (1.0 - d3) * d + d3 * d2
    }

    fun matrixMultiply(dArr: DoubleArray, dArr2: Array<DoubleArray>): DoubleArray {
        return doubleArrayOf(
            dArr[0] * dArr2[0][0] + dArr[1] * dArr2[0][1] + dArr[2] * dArr2[0][2],
            dArr[0] * dArr2[1][0] + dArr[1] * dArr2[1][1] + dArr[2] * dArr2[1][2],
            dArr[0] * dArr2[2][0] + dArr[1] * dArr2[2][1] + dArr[2] * dArr2[2][2]
        )
    }
}