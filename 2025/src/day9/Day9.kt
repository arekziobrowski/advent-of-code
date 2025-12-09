package day9

import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() {
    val points = File("src/day9/input.txt").readLines()
        .map { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            x to y
        }

    val result = points.flatMapIndexed { i, p1 ->
        points.drop(i + 1).map { p2 ->
            rectangleArea(p1, p2)
        }
    }.max()

    println(result)
}

data class Point(val x: Int, val y: Int)
data class Segment(val start: Point, val end: Point)

fun part2() {
    val points = File("src/day9/input.txt").readLines()
        .map { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            Point(x, y)
        }

    val byX = points.groupBy({ it.x }, { it.y })
    val byY = points.groupBy({ it.y }, { it.x })

    val verticalSegments = byX.flatMap { (x, ys) ->
        ys.sorted().chunked(2).filter { it.size == 2 }.map { (y1, y2) ->
            Segment(Point(x, y1), Point(x, y2))
        }
    }

    val horizontalSegments = byY.flatMap { (y, xs) ->
        xs.sorted().chunked(2).filter { it.size == 2 }.map { (x1, x2) ->
            Segment(Point(x1, y), Point(x2, y))
        }
    }

    val result = points.flatMapIndexed { i, p1 ->
        points.drop(i + 1).map { p2 ->
            p1 to p2
        }
    }.filter { (p1, p2) ->
        val minX = minOf(p1.x, p2.x)
        val maxX = maxOf(p1.x, p2.x)
        val minY = minOf(p1.y, p2.y)
        val maxY = maxOf(p1.y, p2.y)

        verticalSegments.none { it.intersectsVertically(minX, maxX, minY, maxY) } &&
                horizontalSegments.none { it.intersectsHorizontally(minX, maxX, minY, maxY) }
    }.maxOf { (p1, p2) -> rectangleArea(p1.x to p1.y, p2.x to p2.y) }

    println(result)
}

fun Segment.intersectsVertically(minX: Int, maxX: Int, minY: Int, maxY: Int): Boolean {
    val segX = start.x
    val segMinY = minOf(start.y, end.y)
    val segMaxY = maxOf(start.y, end.y)
    return segX in (minX + 1)..<maxX && !(segMaxY <= minY || segMinY >= maxY)
}

fun Segment.intersectsHorizontally(minX: Int, maxX: Int, minY: Int, maxY: Int): Boolean {
    val segY = start.y
    val segMinX = minOf(start.x, end.x)
    val segMaxX = maxOf(start.x, end.x)
    return segY in (minY + 1)..<maxY && !(segMaxX <= minX || segMinX >= maxX)
}

fun rectangleArea(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Long {
    val (x1, y1) = p1
    val (x2, y2) = p2
    return (kotlin.math.abs(x2.toLong() - x1.toLong()) + 1) * (kotlin.math.abs(y2.toLong() - y1.toLong()) + 1)
}