package day02

import println
import readInput

fun main() {

    fun part1(input: List<String>): Int {

        val checkMap = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )

        return input.map {
            toRound(it)
        }.filter {
            it.sets.all {
                (it.all {
                    (checkMap[it.key] ?: 0) >= it.value
                })
            }
        }.sumOf { it.id }

    }

    fun part2(input: List<String>): Int {

        return input.map {
            toRound(it)
        }.sumOf {
            val tmpMap = mutableMapOf<String, Int>()
            it.sets.forEach {
                it.entries.forEach {
                    tmpMap.compute(it.key) { _, v ->
                        if (it.value > (v ?: 0))
                            it.value
                        else
                            (v ?: 0)
                    }
                }
            }

            tmpMap.values.reduce { acc, i ->
                acc * i
            }

        }
    }

    check(part1(readInput("day02/Day02_test")) == 8)
    check(part2(readInput("day02/Day02_test")) == 2286)

    val input = readInput("day02/Day02")
    part1(input).println()
    part2(input).println()
}


fun toRound(str: String): Round {
    return str.split(":").map { it.trim() }
        .map { it.replace("Game", "").trim() }
        .let {
            Round(
                it.first().toInt(),
                toSets(it.last())
            )
        }
}

fun toSets(str: String): List<Map<String, Int>> {
    return str.split(";")
        .map { it.trim() }
        .map {
            it.split(",").map { it.trim() }
                .associate { it.split(" ").let { it.last() to it.first().toInt() } }
        }
}

data class Round(val id: Int, val sets: List<Map<String, Int>>)
