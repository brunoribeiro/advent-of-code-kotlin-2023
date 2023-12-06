package day06

import println
import readInput

fun main() {

    fun part1(input: List<String>): Int = input.map {
        it.split(":").last().split(" ").filter { it.isNotBlank() }
    }.let { inp ->
        (0..<inp[0].size).map { inp[0][it].toLong() to inp[1][it].toLong() }
    }.map { (duration, record) ->
        (0..duration).filter { press -> (duration - press) * press > record }.size
    }.reduce { acc, i -> i * acc }


    fun part2(input: List<String>): Int =
        input.map { it.split(":").last().split(" ").filter { it.isNotBlank() } }
            .let {
                val (duration, record) =
                    it[0].joinToString("").toLong() to it[1].joinToString("").toLong()
                (0..duration).filter { press -> (duration - press) * press > record }.size
            }


    check(part1(readInput("day06/Day06_test")) == 288)
    check(part2(readInput("day06/Day06_test")) == 71503)

    val input = readInput("day06/Day06")
    part1(input).println()
    part2(input).println()

}
