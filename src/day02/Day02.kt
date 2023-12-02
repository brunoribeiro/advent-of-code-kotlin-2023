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

        val sum = input.map { toRound(it) }.filter {
            it.all { round ->
                (round.cubes.all {
                    (checkMap[it.key] ?: 0) >= it.value
                })
            }
        }.flatMap {
            it.groupBy { it.id }.keys.distinct()
        }.sum()


        return sum

    }

    fun part2(input: List<String>): Int {

        return input.map { toRound(it) }.map {
            val tmpMap = mutableMapOf<String, Int>()
            it.forEach {
                it.cubes.entries.forEach {
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
        }.sum()
    }

    check(part1(readInput("day02/Day02_test")) == 8)
    check(part2(readInput("day02/Day02_test")) == 2286)

    val input = readInput("day02/Day02")
    part1(input).println()
    part2(input).println()
}

fun toRound(play: String): List<MappedRound> {
    val parts = play.split(":").map { it.trim() }
    val id = parts.first().replace("Game ", "").toInt()
    val sets = parts.last().split(";")
    return sets.mapIndexed { i, str ->
        Round(id, i + 1,
            str.split(",").map { it.trim() }
                .map { it.split(" ").let { CubeInfo(it.first().toInt(), it.last()) } }
        )
    }.map {
        MappedRound(
            id,
            it.set,
            joinCubes(it.cubes).groupBy { it.color }.map { it.key to it.value.sumOf { it.amount } }.toMap()
        )
    }
}

fun joinCubes(cubes: List<CubeInfo>): List<CubeInfo> {
    return cubes.groupBy { it.color }
        .map { it.key to it.value.sumOf { it.amount } }
        .map { CubeInfo(it.second, it.first) }
}

data class Round(val id: Int, val set: Int, val cubes: List<CubeInfo>)
data class MappedRound(val id: Int, val set: Int, val cubes: Map<String, Int>)
data class CubeInfo(val amount: Int, val color: String)
