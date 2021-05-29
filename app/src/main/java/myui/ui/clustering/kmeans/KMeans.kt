package myui.ui.clustering.kmeans

import java.util.*

class KMeans(val allPoints: List<Point>, val k: Int) {
    private var pointClusters: Clusters? = null //the k Clusters

    private fun getPointByLine(line: String): Point {
        val xyz = line.split(",").toTypedArray()
        return Point(xyz[0].toDouble(), xyz[1].toDouble(), xyz[2].toDouble())
    }

    /**step 1: get random seeds as initial centroids of the k clusters
     */
    private val initialKRandomSeeds: Unit
        get() {
            pointClusters = Clusters(allPoints)
            val kRandomPoints: List<Point> = kRandomPoints
            for (i in 0 until k) {
                kRandomPoints[i].index = i
                pointClusters!!.add(Cluster(kRandomPoints[i]))
            }
        }
    private val kRandomPoints: List<Point>
        get() {
            val kRandomPoints: MutableList<Point> = ArrayList<Point>()
            val alreadyChosen = BooleanArray(allPoints.size)
            var size = allPoints.size
            for (i in 0 until k) {
                var index = -1
                val r = random.nextInt(size--) + 1
                for (j in 0 until r) {
                    index++
                    while (alreadyChosen[index]) index++
                }
                kRandomPoints.add(allPoints[index])
                alreadyChosen[index] = true
            }
            return kRandomPoints
        }

    /**step 2: assign points to initial Clusters
     */
    private val initialClusters: Unit
        get() {
            pointClusters!!.assignPointsToClusters()
        }

    /** step 3: update the k Clusters until no changes in their members occur
     */
    private fun updateClustersUntilNoChange() {
        var isChanged = pointClusters!!.updateClusters()
        while (isChanged) isChanged = pointClusters!!.updateClusters()
    }

    /**do K-means clustering with this method
     */
    val pointsClusters: List<Cluster>?
        get() {
            if (pointClusters == null) {
                initialKRandomSeeds
                initialClusters
                updateClustersUntilNoChange()
            }
            return pointClusters?.toList()
        }

    companion object {
        private val random = Random()

    }
}