package day6

import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() {
    val result = File("src/day6/input.txt").readLines()
        .map { it.split(" ").filter(String::isNotBlank) }
        .let { rows ->
            rows.first().indices.map { col ->
                rows.map { it[col] }
            }
        }
        .sumOf { row ->
            val op = row.last()
            val numbers = row.dropLast(1).map { it.toLong() }
            when (op) {
                "*" -> numbers.reduce { acc, l -> acc * l }
                "+" -> numbers.sum()
                else -> 0L
            }
        }

    println(result)
}

fun part2() {
    val lines = File("src/day6/input.txt").readLines()
    val (rows, ops) = lines.dropLast(1) to lines.last().split(" ").filter { it.isNotBlank() }

    val numberRows = rows.first().indices.map { col ->
        rows.joinToString("") { row ->
            (row.getOrNull(col) ?: ' ').toString()        }.trim()
    }.splitOn { it.isBlank() }

    val result = numberRows
        .withIndex()
        .sumOf { (idx, numbers) ->
            val op = ops.getOrNull(idx)
            val longs = numbers.map { it.toLong() }
            when (op) {
                "*" -> longs.reduce { acc, l -> acc * l.toLong() }
                "+" -> longs.sum()
                else -> 0L
            }
        }

    println(result)
}

fun <T> List<T>.splitOn(predicate: (T) -> Boolean): List<List<T>> {
    return fold(mutableListOf(mutableListOf<T>())) { acc, element ->
        if (predicate(element)) {
            acc.add(mutableListOf())
        } else {
            acc.last().add(element)
        }
        acc
    }.filter { it.isNotEmpty() }
}