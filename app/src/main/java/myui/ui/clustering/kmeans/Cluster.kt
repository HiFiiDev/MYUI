package myui.ui.clustering.kmeans

class Cluster(firstPoint: Point) {
    val points: MutableList<Point> = arrayListOf()
    var centroid: Point = firstPoint

    fun updateCentroid() {
        var newx = 0.0
        var newy = 0.0
        var newz = 0.0
        for (point in points) {
            newx += point.x
            newy += point.y
            newz += point.z
        }
        centroid = Point(newx / points.size, newy / points.size, newz / points.size)
    }

    override fun toString(): String {
        val builder = StringBuilder("This cluster contains the following points:\n")
        for (point in points) builder.append("$point,\n")
        return builder.deleteCharAt(builder.length - 2).toString()
    }
}