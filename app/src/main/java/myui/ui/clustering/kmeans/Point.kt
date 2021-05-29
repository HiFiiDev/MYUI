package myui.ui.clustering.kmeans

class Point(var x: Double, var y: Double, var z: Double) {
    var index = -1 //denotes which Cluster it belongs to
    fun getSquareOfDistance(anotherPoint: Point): Double {
        return (x - anotherPoint.x) * (x - anotherPoint.x) + (y - anotherPoint.y) * (y - anotherPoint.y) + (z - anotherPoint.z) * (z - anotherPoint.z)
    }

    override fun toString(): String {
        return "($x,$y,$z)"
    }
}