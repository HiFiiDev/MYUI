package myui.ui.monet

import myui.ui.monet.ColorShades.shadesOf
import myui.ui.monet.colorscience.CAM16Color
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.Lambda

class ColorScheme(i: Int, private val darkTheme: Boolean) {
    val accent1: List<Int>
    private val accent2: List<Int>
    private val accent3: List<Int>
    private val neutral1: List<Int>
    private val neutral2: List<Int>
    val allAccentColors: List<Int>
        get() {
            val arrayList: ArrayList<Int> = ArrayList()
            arrayList.addAll(accent1)
            arrayList.addAll(accent2)
            arrayList.addAll(accent3)
            return arrayList
        }
    val allNeutralColors: List<Int>
        get() {
            val arrayList: ArrayList<Int> = ArrayList()
            arrayList.addAll(neutral1)
            arrayList.addAll(neutral2)
            return arrayList
        }

    private fun humanReadable(list: List<Int>): String {
        return listOf(
            list,
            null,
            null,
            null,
            0,
            null,
            HumanReadable.INSTANCE,
            31,
            null
        ).joinToString()
    }

    override fun toString(): String {
        return """ColorScheme {
  neutral1: ${humanReadable(neutral1)}
  neutral2: ${humanReadable(neutral2)}
  accent1: ${humanReadable(accent1)}
  accent2: ${humanReadable(accent2)}
  accent3: ${humanReadable(accent3)}
}"""
    }

    internal class HumanReadable : Lambda<CharSequence?>(1), Function1<Int?, CharSequence?> {
        companion object {
            val INSTANCE = HumanReadable()
        }

        operator fun invoke(i: Int): CharSequence {
            return "#${i.toString(16)}"
        }

        override fun invoke(obj: Int?): CharSequence {
            return invoke(obj)
        }
    }

    init {
        val fromRgb = CAM16Color.fromRgb(i)
        val y = fromRgb.color.y.toDouble()
        val cAM16Color = CAM16Color(CAM16Color.gamutMap(y, 48.0, fromRgb.h))
        val cAM16Color2 = CAM16Color(CAM16Color.gamutMap(y, 16.0, fromRgb.h))
        val cAM16Color3 = CAM16Color(CAM16Color.gamutMap(y, 32.0, fromRgb.h + 60.0))
        val cAM16Color4 = CAM16Color(CAM16Color.gamutMap(y, 8.0, fromRgb.h))
        val cAM16Color5 = CAM16Color(CAM16Color.gamutMap(y, 4.0, fromRgb.h))
        val shadesOf = shadesOf(cAM16Color.h, cAM16Color.C)
        Intrinsics.checkNotNullExpressionValue(shadesOf, "shadesOf(camAccent1.h, camAccent1.C)")
        accent1 = shadesOf.toList()
        val shadesOf2 = shadesOf(cAM16Color2.h, cAM16Color2.C)
        Intrinsics.checkNotNullExpressionValue(shadesOf2, "shadesOf(camAccent2.h, camAccent2.C)")
        accent2 = shadesOf2.toList()
        val shadesOf3 = shadesOf(cAM16Color3.h, cAM16Color3.C)
        Intrinsics.checkNotNullExpressionValue(shadesOf3, "shadesOf(camAccent3.h, camAccent3.C)")
        accent3 = shadesOf3.toList()
        val shadesOf4 = shadesOf(cAM16Color4.h, cAM16Color4.C)
        Intrinsics.checkNotNullExpressionValue(shadesOf4, "shadesOf(camNeutral1.h, camNeutral1.C)")
        neutral1 = shadesOf4.toList()
        val shadesOf5 = shadesOf(cAM16Color5.h, cAM16Color5.C)
        Intrinsics.checkNotNullExpressionValue(shadesOf5, "shadesOf(camNeutral2.h, camNeutral2.C)")
        neutral2 = shadesOf5.toList()
    }
}