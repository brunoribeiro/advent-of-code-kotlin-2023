package day09

import println
import readInput
import kotlin.time.measureTime


fun main() {


    fun part1(input: List<String>): Int {


        return input.sumOf {

            val line = it.split(" ").map { it.toInt() }
            var curr = line
            val all = mutableListOf(line.last())
            while (curr.any { it != 0 }) {

                curr = curr.windowed(2).map { it.last() - it.first() }
                all.add(curr.last())
            }
            all.sum()

        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val line = it.split(" ").map { it.toInt() }
            var curr = line
            val all = mutableListOf(line.first())
            while (curr.any { it != 0 }) {
                curr = curr.windowed(2).map { it.first() - it.last() }
                all.add(curr.first())
            }
            all.sum()
        }
    }

    check(part1(readInput("day09/Day09_test")) == 114)
    check(part2(readInput("day09/Day09_test")) == 2)


    measureTime {
        val input = readInput("day09/Day09")
        part1(input).println()
        part2(input).println()
    }.also { println("time: ${it.inWholeMilliseconds}ms") }

}
