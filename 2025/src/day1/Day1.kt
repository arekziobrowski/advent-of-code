package day1

import java.io.File
import java.lang.Math.floorDiv

fun main() {
    part1()
    part2()
}

fun part1() {
    var dial = 50
    var counter = 0
    File("src/day1/input.txt").forEachLine {
        val diff = it.drop(1).toInt()
        if (it.first() == 'L') dial-=diff else dial+= diff
        dial = if (dial % 100 < 0) dial % 100 + 100 else dial % 100
        if (dial == 0) counter++
    }
    println(counter)
}

fun part2() {
    var dial = 50
    var counter = 0
    File("src/day1/input.txt").forEachLine {
        val diff = it.drop(1).toInt()
        if (it.first() == 'L') {
            if (dial == 0) counter--
            dial-=diff
            counter-=floorDiv(dial, 100) - if (dial %100 == 0) 1 else 0
        } else {
            dial += diff
            counter += floorDiv(dial, 100)
        }
        dial = if (dial % 100 < 0) dial % 100 + 100 else dial % 100
    }
    println(counter)
}