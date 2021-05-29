package myui.ui.clustering.kmeans

class Clusters(val allPoints: List<Point>) : ArrayList<Cluster>() {
    private var isChanged = false

    /**@param point
     * @return the index of the Cluster nearest to the point
     */
    fun getNearestCluster(point: Point): Int {
        var minSquareOfDistance = Double.MAX_VALUE
        var itsIndex = -1
        for (i in 0 until size) {
            val squareOfDistance: Double = point.getSquareOfDistance(get(i).centroid)
            if (squareOfDistance < minSquareOfDistance) {
                minSquareOfDistance = squareOfDistance
                itsIndex = i
            }
        }
        return itsIndex
    }

    fun updateClusters(): Boolean {
        for (cluster in this) {
            cluster.updateCentroid()
            cluster.points.clear()
        }
        isChanged = false
        assignPointsToClusters()
        return isChanged
    }

    fun assignPointsToClusters() {
        for (point in allPoints) {
            val previousIndex: Int = point.index
            val newIndex = getNearestCluster(point)
            if (previousIndex != newIndex) isChanged = true
            val target: Cluster = get(newIndex)
            point.index = newIndex
            target.points.add(point)
        }
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}