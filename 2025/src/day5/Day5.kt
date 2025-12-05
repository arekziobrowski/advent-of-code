package day5

import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() {
    val (ranges, numbers) = File("src/day5/input.txt").readLines()
        .splitAt { it.isBlank() }
        .let { (rangeLines, numberLines) ->
            val ranges = rangeLines.map { line ->
                line.split("-").let { (start, end) -> start.toLong()..end.toLong() }
            }
            val numbers = numberLines.map { it.toLong() }
            ranges to numbers
        }

    val count = numbers.count { number ->
        ranges.any { range -> number in range }
    }

    println(count)
}

fun part2() {
    val ranges = File("src/day5/input.txt").readLines()
        .splitAt { it.isBlank() }
        .first
        .map { line ->
            line.split("-").let { (start, end) -> start.toLong()..end.toLong() }
        }

    val sum = ranges
        .mergeOverlapping()
        .sumOf { it.last - it.first + 1 }
    println(sum)
}

fun <T> List<T>.splitAt(predicate: (T) -> Boolean): Pair<List<T>, List<T>> {
    val index = indexOfFirst(predicate)
    return if (index != -1) take(index) to drop(index + 1) else this to emptyList()
}

fun List<LongRange>.mergeOverlapping(): Sequence<LongRange> = sequence {
    if (isEmpty()) return@sequence

    val sorted = sortedBy { it.first }
    var current = sorted[0]

    for (i in 1 until sorted.size) {
        val next = sorted[i]

        if (next.first <= current.last + 1) {
            current = current.first..maxOf(current.last, next.last)
        } else {
            yield(current)
            current = next
        }
    }
    yield(current)
}