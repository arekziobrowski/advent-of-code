package day2

import java.io.File


fun main() {
    part1()
    part2()
}

fun part1() {
   val sum = File("src/day2/input.txt").useLines { lines ->
        lines
            .first()
            .split(',')
            .asSequence()
            .flatMap { line ->
                line
                    .split('-')
                    .map { it.toLong() }
                    .let { LongRange(it.first(), it.last())}
            }
            .filter { l ->
                val s = l.toString()
                val len = s.length
                len % 2 == 0 && s.take(len / 2) == s.substring(len / 2)
            }
            .sumOf { it }
    }
    println(sum)
}

fun part2() {
    val sum = File("src/day2/input.txt").useLines { lines ->
        lines
            .first()
            .split(',')
            .asSequence()
            .flatMap { line ->
                line
                    .split('-')
                    .map { it.toLong() }
                    .let { LongRange(it.first(), it.last())}
            }
            .filter { l ->
                l.toString().isMadeOfRepeatedSubstring()
            }
            .sumOf { it }
    }
    println(sum)
}

fun String.isMadeOfRepeatedSubstring(): Boolean {
    if (length < 2) return false
    val doubled = this + this
    return doubled.indexOf(this, startIndex = 1) != length
}
