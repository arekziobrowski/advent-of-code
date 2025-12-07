package day7

import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() {
    val tree = File("src/day7/input.txt").readLines()
        .fold(emptyList<String>()) { acc, line ->
            val processed = processLine(line, acc.lastOrNull())
            acc + processed
        }

    val result = tree
        .zipWithNext()
        .sumOf { (prev, curr) ->
            curr.indices.count { col ->
                curr[col] == '^' && prev[col] == '|'
            }
        }
    println(result)
}

fun part2() {
    val tree = File("src/day7/input.txt").readLines()
        .foldIndexed(emptyList<String>()) { index, acc, line ->
            val processed = processLine(line, acc.lastOrNull())
            acc + processed
        }
        .zipWithNext().flatMap { (prev, curr) ->
            listOf(curr.mapIndexed { col, c ->
                if (c == '^' && prev.getOrNull(col) != '|') '.' else c
            }.joinToString(""))
        }

    val counters = MutableList(tree.first().length) { 1L }

    tree.reversed().forEach { line ->
        line.forEachIndexed { col, c ->
            if (c == '^') counters[col] = counters[col - 1] + counters[col + 1]
        }
    }
    println(counters[tree.first().indexOf('|')])
}

fun processLine(curr: String, prev: String?): String {
    return curr.indices.fold(curr.toCharArray()) { chars, col ->
        when {
            curr[col] == '^' -> {
                if (col > 0) chars[col - 1] = '|'
                if (col < chars.size - 1) chars[col + 1] = '|'
            }
            chars[col] == '.' && prev?.get(col) == '|' -> chars[col] = '|'
            chars[col] == '.' && prev?.get(col) == 'S' -> chars[col] = '|'
        }
        chars
    }.concatToString()
}