package day3

import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() {
    val sum = File("src/day3/input.txt").useLines { lines ->
        lines
            .map { line ->
                line
                    .map { it.digitToInt() }
                    .toIntArray()
                    .largestNumberFromDigitsInArray()
            }
            .sum()
    }
    println(sum)
}

fun part2() {
    val sum = File("src/day3/input.txt").useLines { lines ->
        lines
            .map { line ->
                line
                    .map { it.digitToInt() }
                    .toIntArray()
                    .largestNumberFromDigitsInArray(12)
            }
            .sum()
    }
    println(sum)
}

fun IntArray.largestNumberFromDigitsInArray(digitsToPick: Int = 2): Long {
    var remaining = digitsToPick
    var fromIndex = 0
    var result: Long = 0

    while (remaining > 0) {
        val toIndex = size - remaining
        val nextDigit = this.maxInRange(fromIndex, toIndex)!!

        var chosenIndex = fromIndex
        for (i in fromIndex..toIndex) {
            if (this[i] == nextDigit) {
                chosenIndex = i
                break
            }
        }

        result = result * 10 + nextDigit
        fromIndex = chosenIndex + 1
        remaining--
    }

    return result
}

fun IntArray.maxInRange(fromIndex: Int, toIndexInclusive: Int): Int? {
    if (fromIndex > toIndexInclusive) return null
    return (fromIndex..toIndexInclusive).maxOf { this[it] }
}