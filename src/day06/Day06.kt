package day06

import println
import readInput
import kotlin.time.measureTime

fun main() {

    fun part1(input: List<String>): Int = input.map {
        it.split(":").last().split(" ").filter { it.isNotBlank() }
    }.let { inp ->
        (0..<inp[0].size).map { inp[0][it].toLong() to inp[1][it].toLong() }
    }.map { (duration, record) ->
        (0..duration).filter { press -> (duration - press) * press > record }.size
    }.reduce { acc, i -> i * acc }


    fun part2(input: List<String>): Long =
        input.map { it.split(":").last().split(" ").filter { it.isNotBlank() } }
            .let {
                val (duration, record) = it[0].joinToString("").toLong() to it[1].joinToString("").toLong()
                val start = (1..<duration).find { press -> (duration - press) * press > record } ?: 0
                val end = (duration-2 downTo start).find { press -> (duration - press) * press > record } ?: 0
                end - start + 1
            }

    check(part1(readInput("day06/Day06_test")) == 288)
    check(part2(readInput("day06/Day06_test")) == 71503L)



    measureTime {
        val input = readInput("day06/Day06")
        part1(input).println()
        part2(input).println()
    }.also { println("time: ${it.inWholeMilliseconds}ms") }

}
