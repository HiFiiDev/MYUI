package myui.ui.monet

import android.util.Log
import myui.ui.monet.colorscience.CAM16Color
import myui.ui.monet.colorscience.Contrast.lstarToY
import myui.ui.monet.colorscience.MonetColor

object ColorShades {
    private val DEBUG: Boolean = true
    fun shadesOf(d: Double, d2: Double): IntArray {
        val iArr = IntArray(11)
        iArr[0] = getShade(95.0, d, d2).argb
        var i = 1
        while (i < 11) {
            iArr[i] = getShade(if (i == 5) 49.6 else (100 - i * 10).toDouble(), d, d2).argb
            i++
        }
        return iArr
    }

    private fun getShade(d: Double, d2: Double, d3: Double): MonetColor {
        val gamutMap = CAM16Color.gamutMap(lstarToY(d), d3, d2)
        val cAM16Color = CAM16Color(gamutMap)
        if (DEBUG) {
            Log.d(
                "ColorShades",
                "requested LHC ${d.toInt()}, ${d2.toInt()}, ${d3.toInt()}"
            )
            Log.d(
                "ColorShades",
                "got LHC ${gamutMap.lstar.toInt()}, ${cAM16Color.h.toInt()}, ${cAM16Color.C.toInt()}"
            )
        }
        return gamutMap
    }
}