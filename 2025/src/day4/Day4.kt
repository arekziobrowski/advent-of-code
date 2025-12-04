package day4

import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() {
    val result = File("src/day4/input.txt").readLines().let { grid ->
        val directions = listOf(
            -1 to -1,
            -1 to 0,
            -1 to 1,
            0 to -1,
            0 to 1,
            1 to -1,
            1 to 0,
            1 to 1
        )

        grid.indices.sumOf { row ->
            grid[row].indices.count { col ->
                grid[row][col] == '@' &&
                        directions.count { (dr, dc) ->
                            val newRow = row + dr
                            val newCol = col + dc
                            newRow in grid.indices &&
                                    newCol in grid[newRow].indices &&
                                    grid[newRow][newCol] == '@'
                        } < 4
            }
        }
    }

    println(result)
}

fun part2() {
    val grid = File("src/day4/input.txt").readLines().map { it.toMutableList() }.toMutableList()

    val directions = listOf(
        -1 to -1,
        -1 to 0,
        -1 to 1,
        0 to -1,
        0 to 1,
        1 to -1,
        1 to 0,
        1 to 1
    )

    val totalRemoved = generateSequence {
        grid.indices.flatMap { row ->
            grid[row].indices.mapNotNull { col ->
                (row to col).takeIf {
                    grid[row][col] == '@' &&
                            directions.count { (dr, dc) ->
                                (row + dr) in grid.indices &&
                                        (col + dc) in grid[row + dr].indices &&
                                        grid[row + dr][col + dc] == '@'
                            } < 4
                }
            }
        }.takeIf { it.isNotEmpty() }?.also { rolls ->
            rolls.forEach { (row, col) -> grid[row][col] = '.' }
        }
    }.sumOf { it.size }

    println(totalRemoved)
}