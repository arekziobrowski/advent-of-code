package day8

import day7.processLine
import java.io.File
import java.util.PriorityQueue
import kotlin.math.pow
import kotlin.math.sqrt

data class Point(val x: Double, val y: Double, val z: Double) {
    fun distanceTo(other: Point): Double {
        return sqrt(
            (other.x - x).pow(2) +
                    (other.y - y).pow(2) +
                    (other.z - z).pow(2)
        )
    }
}

data class PointPair(
    val first: Point,
    val second: Point,
    val distance: Double
)


fun main() {
    part1()
    part2()
}

fun part1() {
    val points = File("src/day8/input.txt").readLines()
        .map { p ->
            val (first, second, third) = p.split(",")
            Point(first.toDouble(), second.toDouble(), third.toDouble())
        }

    val minHeap = PriorityQueue<PointPair>(compareBy { it.distance })

    points.flatMapIndexed { i, p1 ->
        points.drop(i + 1).map { p2 -> PointPair(p1, p2, p1.distanceTo(p2)) }
    }.forEach(minHeap::add)

    val topPairs = List(1000) { minHeap.poll() }

    val adjacency = mutableMapOf<Point, MutableSet<Point>>()
    topPairs.forEach { pair ->
        adjacency.computeIfAbsent(pair.first) { mutableSetOf() }.add(pair.second)
        adjacency.computeIfAbsent(pair.second) { mutableSetOf() }.add(pair.first)
    }

    val visited = mutableSetOf<Point>()
    val circuits = mutableListOf<List<Point>>()

    fun dfs(point: Point, component: MutableList<Point>) {
        if (point in visited) return
        visited.add(point)
        component.add(point)
        adjacency[point]?.forEach { neighbor ->
            dfs(neighbor, component)
        }
    }

    adjacency.keys.forEach { point ->
        if (point !in visited) {
            val component = mutableListOf<Point>()
            dfs(point, component)
            circuits.add(component)
        }
    }

    val result = circuits
        .map { it.size }
        .sortedDescending()
        .take(3)
        .reduce { acc, size -> acc * size }

    println(result)
}

fun part2() {
    val points = File("src/day8/input.txt").readLines()
        .map { p ->
            val (first, second, third) = p.split(",")
            Point(first.toDouble(), second.toDouble(), third.toDouble())
        }

    val minHeap = PriorityQueue<PointPair>(compareBy { it.distance })

    points.flatMapIndexed { i, p1 ->
        points.drop(i + 1).map { p2 -> PointPair(p1, p2, p1.distanceTo(p2)) }
    }.forEach(minHeap::add)

    val topPairs = List(minHeap.size) { minHeap.poll() }

    val adjacency = mutableMapOf<Point, MutableSet<Point>>()
    val allPoints = topPairs.flatMap { listOf(it.first, it.second) }.toSet()

    fun isFullyConnected(): Boolean {
        if (adjacency.isEmpty()) return false

        val start = adjacency.keys.first()
        val visited = mutableSetOf<Point>()
        val queue = ArrayDeque<Point>()

        queue.add(start)
        visited.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            adjacency[current]?.forEach { neighbor ->
                if (neighbor !in visited) {
                    visited.add(neighbor)
                    queue.add(neighbor)
                }
            }
        }

        return visited.size == allPoints.size
    }

    var lastPair: PointPair? = null

    for (pair in topPairs) {
        adjacency.computeIfAbsent(pair.first) { mutableSetOf() }.add(pair.second)
        adjacency.computeIfAbsent(pair.second) { mutableSetOf() }.add(pair.first)
        lastPair = pair

        if (isFullyConnected()) {
           println(lastPair.first.x.toLong() * lastPair.second.x.toLong())
           break
        }
    }
}

