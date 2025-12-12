
package day12

import java.io.File

fun main() {
    part1()
}

data class Region(val width: Int, val height: Int, val presentCounts: List<Int>)

fun part1() {
    val lines = File("src/day12/input.txt").readLines()

    val regionPattern = Regex("""\d+x\d+:.*""")
    val regionStartIndex = lines.indexOfFirst { regionPattern.matches(it) }

    val presentSizes = parsePresents(lines.take(regionStartIndex))

    val regions = lines.drop(regionStartIndex)
        .filter { it.isNotBlank() }
        .map { parse(it) }

    val result = regions.count { (width, height, presentCounts) ->
        val area = width * height
        val presentsArea = presentCounts.mapIndexed { index, count ->
            count * presentSizes[index]
        }.sum()
        area > presentsArea // only leave the regions where the presents can fit
    }

    println(result)
}

private fun parsePresents(lines: List<String>): List<Int> {
    val indexPattern = Regex("""\d+:""")

    return lines
        .joinToString("\n")
        .split(indexPattern)
        .filter { it.isNotBlank() }
        .map { block -> block.sumOf { char -> if (char == '#') 1L else 0L }.toInt() }
}

private fun parse(line: String): Region {
    val regex = Regex("""(\d+)x(\d+): (\d+) (\d+) (\d+) (\d+) (\d+) (\d+)""")
    val match = regex.find(line) ?: return Region(width = 0, height = 0, presentCounts = emptyList())
    val values = match.groupValues.drop(1).map { it.toInt() }

    return Region(
        width = values[0],
        height = values[1],
        presentCounts = values.drop(2)
    )
}