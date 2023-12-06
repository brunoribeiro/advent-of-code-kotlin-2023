package day06

import println
import readInput

fun main() {

    fun part1(input: List<String>): Long {

        val raceInfo = input.map { it.split(":").last().split(" ").filter { it.isNotBlank() } }
            .let { inp ->
                (0..<inp[0].size)
                    .map {
                        RaceRecord(inp[0][it].toLong(), inp[1][it].toLong())
                    }
            }

        return raceInfo
            .map { inp ->
                (0..inp.duration).map {
                    Race(it, (inp.duration - it) * it)
                }.filter { it.distance > inp.record }.size.toLong()
            }.reduce { acc, i -> i * acc }


    }

    fun part2(input: List<String>): Int {

        val record = input.map { it.split(":").last().split(" ").filter { it.isNotBlank() } }
            .let { inp ->
                RaceRecord(inp[0].joinToString("").toLong(), inp[1].joinToString("").toLong())
            }

        return listOf(record)
            .map { inp ->
                (0..inp.duration).map {
                    Race(it, (inp.duration - it) * it)
                }.filter { it.distance > inp.record }.size
            }.reduce { acc, i -> i * acc }
    }


    check(part1(readInput("day06/Day06_test")) == 288L)
    check(part2(readInput("day06/Day06_test")) == 71503)

    val input = readInput("day06/Day06")
    part1(input).println()
    part2(input).println()

}

data class RaceRecord(val duration: Long, val record: Long)
data class Race(val press: Long, val distance: Long)
