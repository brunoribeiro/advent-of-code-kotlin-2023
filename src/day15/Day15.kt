package day15

import println
import readInput
import kotlin.time.measureTime

fun main() {


    fun part1(input: List<String>): Int {
        return input.first().split(",")
            .map {
                it.toCharArray().fold(0) { acc, curr ->
                    ((acc + curr.code) * 17) % 256
                }
            }.sum()
    }


    fun part2(input: List<String>): Int {

        return input.first().split(",")
            .asSequence()
            .map {
                it.split("=").let { it.first() to it.last() }
            }
            .map {

                val operation = when (it.second.endsWith("-")) {
                    true -> '-'
                    false -> '+'
                }
                val box = when (operation) {
                    '-' -> it.first.dropLast(1)
                    else -> it.first
                }.toCharArray().fold(0) { acc, curr ->
                    ((acc + curr.code) * 17) % 256
                }

                when (operation) {
                    '-' -> Instruction(box, it.first.dropLast(1), operation, -1)
                    else ->Instruction(box, it.first, operation,it.second.toInt())
                }

            }.fold(mutableMapOf<Int, MutableMap<String, Int>>()) { acc, curr ->

                acc.putIfAbsent(curr.box, mutableMapOf())
                when (curr.operation) {
                    '-' -> acc[curr.box]?.remove(curr.label) ?: emptyMap<String, Int>()
                    else -> acc[curr.box]?.put(curr.label, curr.focal) ?: emptyMap<String, Int>()
                }

                acc

            }.filter { it.value.isNotEmpty() }
            .map {

              if (it.component2().isNotEmpty())
                    it.component2().values.mapIndexed { slot, focal ->
                        (slot + 1) * focal * (it.component1() + 1)
                    }.sum()
                else 0
            }.sum()

    }



    check(part1(readInput("day15/Day15_test")) == 1320)
    check(part2(readInput("day15/Day15_test")) == 145)

    measureTime {
        val input = readInput("day15/Day15")
        part1(input).println()
       part2(input).println()
    }.also { println("time: ${it.inWholeMilliseconds}ms") }

}


data class Instruction(val box: Int, val label: String, val operation: Char, val focal: Int)
