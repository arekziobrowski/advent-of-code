package day11

import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() {
    val graph = File("src/day11/input.txt").readLines()
        .associate { line ->
            line.split(": ").let { (node, neighbors) -> node to neighbors.split(" ") }
        }

    val memo = mutableMapOf<String, Long>()

    fun countPaths(node: String): Long = memo.getOrPut(node) {
        when (node) {
            "out" -> 1L
            !in graph -> 0L
            else -> graph[node]!!.sumOf { countPaths(it) }
        }
    }

    println(countPaths("you"))
}

fun part2() {
    val graph = File("src/day11/input.txt").readLines()
        .associate { line ->
            line.split(": ").let { (node, neighbors) -> node to neighbors.split(" ") }
        }

    val required = setOf("dac", "fft")
    val memo = mutableMapOf<Pair<String, Set<String>>, Long>()

    fun countPaths(node: String, visited: Set<String>): Long {
        val currentVisited = if (node in required) visited + node else visited

        return memo.getOrPut(node to currentVisited) {
            when (node) {
                "out" -> if (currentVisited == required) 1L else 0L
                !in graph -> 0L
                else -> graph[node]!!.sumOf { countPaths(it, currentVisited) }
            }
        }
    }

    println(countPaths("svr", emptySet()))
}