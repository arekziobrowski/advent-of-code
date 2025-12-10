package day10

import io.ksmt.KContext
import io.ksmt.solver.z3.KZ3Solver
import io.ksmt.utils.mkConst
import kotlin.time.Duration.Companion.seconds
import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() {
    val result = File("src/day10/input.txt").readLines()
        .map(::parseLine)
        .sumOf { (lights, buttons) -> lightsCost(lights, buttons) }

    println(result)
}

fun part2() {
    val result = File("src/day10/input.txt").readLines().sumOf { line ->
        val parts = line.split(" ")
        val buttonIndexes = parts.drop(1).dropLast(1).map(::parseButtonIndices)
        val joltages = parts.last()
            .filter { it.isDigit() || it == ',' }
            .split(",")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
        joltagesCost(joltages, buttonIndexes)
    }
    println(result)
}

private fun parseLine(line: String): Triple<Int, List<Int>, List<Int>> {
    val parts = line.split(" ")

    val lights = parts.first()
        .drop(1).dropLast(1)
        .reversed()
        .replace('.', '0')
        .replace('#', '1')
        .toInt(2)

    val buttons = parts.drop(1).dropLast(1).map { buttonStr ->
        buttonStr.filter { it.isDigit() || it == ',' }
            .split(",")
            .filter { it.isNotEmpty() }
            .sumOf { 1 shl it.toInt() }
    }

    val joltages = parts.last()
        .filter { it.isDigit() || it == ',' }
        .split(",")
        .filter { it.isNotEmpty() }
        .map { it.toInt() }

    return Triple(lights, buttons, joltages)
}

private fun lightsCost(lights: Int, buttons: List<Int>): Int {
    val seen = mutableSetOf(0)
    return generateSequence(listOf(0)) { n ->
        buttons.flatMap { button ->
            n.map { parent -> button xor parent }
        }.filter { seen.add(it) }
    }.indexOfFirst { frontier -> lights in frontier }
}

private fun joltagesCost(joltages: List<Int>, buttonIndexes: List<List<Int>>): Int {
    KContext().use { ctx ->
        KZ3Solver(ctx).use { solver ->
            val buttonPresses = buttonIndexes.indices.map { i ->
                ctx.mkIntSort().mkConst("x$i")
            }

            for (presses in buttonPresses) {
                solver.assert(ctx.mkArithGe(presses, ctx.mkIntNum(0)))
            }

            joltages.forEachIndexed { i, joltage ->
                val terms = mutableListOf<io.ksmt.expr.KExpr<io.ksmt.sort.KIntSort>>()
                terms.add(ctx.mkIntNum(joltage))

                buttonIndexes.forEachIndexed { buttonIdx, indices ->
                    if (i in indices) {
                        terms.add(ctx.mkArithUnaryMinus(buttonPresses[buttonIdx]))
                    }
                }

                val sum = terms.reduce { acc, term -> ctx.mkArithAdd(acc, term) }
                solver.assert(ctx.mkEq(sum, ctx.mkIntNum(0)))
            }

            val totalPresses: io.ksmt.expr.KExpr<io.ksmt.sort.KIntSort> =
                if (buttonPresses.isEmpty()) ctx.mkIntNum(0)
                else buttonPresses.fold(ctx.mkIntNum(0) as io.ksmt.expr.KExpr<io.ksmt.sort.KIntSort>) { a, b ->
                    ctx.mkArithAdd(a, b)
                }

            var lo = 0
            var hi = joltages.sum()
            while (lo < hi) {
                val mid = (lo + hi) / 2
                solver.push()
                solver.assert(ctx.mkArithLe(totalPresses, ctx.mkIntNum(mid)))
                if (solver.check(60.seconds) == io.ksmt.solver.KSolverStatus.SAT) {
                    hi = mid
                } else {
                    lo = mid + 1
                }
                solver.pop()
            }
            return lo
        }
    }
}

private fun parseButtonIndices(buttonStr: String): List<Int> =
    buttonStr.filter { it.isDigit() || it == ',' }
        .split(",")
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
