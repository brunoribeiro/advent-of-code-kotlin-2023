package day14

import println
import readInput
import kotlin.time.measureTime


fun main() {


    fun part1(input: List<String>): Int {

        val patterns = split(input) {
            it.isBlank()
        }

        val rotated = (0 until 1).fold(patterns) { acc, _ ->
            acc.map {
                tiltNS(it, Direction.N)
            }
        }

        val sum = rotated.sumOf { pattern ->
            pattern.mapIndexed { idx, str ->
                str.map {
                    when (it) {
                        'O' -> pattern.size - idx
                        else -> 0
                    }
                }.sum()
            }.sum()
        }


        return sum

    }

    fun part2(input: List<String>): Int {

        val patterns = split(input) {
            it.isBlank()
        }


        val map = mutableMapOf<String, Pair<Int, Int>>()
        var cycleSize = -1
        var cycleStart = -1
        var tmp = patterns

        while (true) {

            val sum = tmp.sumOf { pattern ->
                pattern.mapIndexed { idx, str ->
                    str.map {
                        when (it) {
                            'O' -> pattern.size - idx
                            else -> 0
                        }
                    }.sum()
                }.sum()
            }

            val key = tmp.flatMap { it.map { it } }.joinToString()

            if (map.contains(key)) {
                when (cycleStart == -1) {
                    true -> {
                        cycleStart = map.size; map.clear();
                    }

                    else -> {
                        cycleSize = map.size
                        break
                    }
                }

            }
            map[key] = map.size to sum

            tmp = tmp.map {
                tiltWE(tiltNS(tiltWE(tiltNS(it, Direction.N), Direction.W), Direction.S), Direction.E)
            }


        }

        val idx = (1_000_000_000 - cycleStart) % cycleSize


        return map.values.first { it.first == idx }.second
    }



    check(part1(readInput("day14/Day14_test")) == 136)
    check(part2(readInput("day14/Day14_test")) == 64)

    measureTime {
        val input = readInput("day14/Day14")
        part1(input).println()
        part2(input).println()
    }.also { println("time: ${it.inWholeMilliseconds}ms") }

}


fun split(input: List<String>, predicate: (String) -> Boolean): List<List<String>> {

    return input.fold(mutableListOf<MutableList<String>>(mutableListOf())) { acc, curr ->

        when {
            predicate(curr) -> {
                acc.add(mutableListOf())
            }

            else -> {
                acc.last().add(curr)

            }
        }
        acc

    }.toList()
}


fun tiltNS(it: List<String>, d: Direction) = it.flatMap {
    it.mapIndexed { index, c ->
        index to c
    }
}.groupBy { it.first }.map { it.value.map { it.second }.joinToString("") }
    .map {
        it.split("#").joinToString("#") {
            when (d) {
                Direction.S -> it.toList().sorted()
                Direction.N -> it.toList().sorted().reversed()
                else -> it.toList()
            }.joinToString("")
        }
    }.flatMap {
        it.mapIndexed { index, c ->
            index to c
        }
    }.groupBy { it.first }.map { it.value.map { it.second }.joinToString("") }

fun tiltWE(it: List<String>, d: Direction) =
    it.map {
        it.split("#").joinToString("#") {
            when (d) {
                Direction.E -> it.toList().sorted()
                Direction.W -> it.toList().sorted().reversed()
                else -> it.toList()
            }.joinToString("")
        }
    }

enum class Direction {
    N, S, W, E
}







