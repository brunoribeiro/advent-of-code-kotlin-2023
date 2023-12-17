package day13

import println
import readInput
import kotlin.math.absoluteValue
import kotlin.time.measureTime

fun main() {

    fun part1(input: List<String>): Int {

        val patterns = input.joinToString("\n").split("\n\n").map { it.lines() }

        return  patterns.sumOf { mirror(it, 0) }
    }

    fun part2(input: List<String>): Int {

        val patterns = input.joinToString("\n").split("\n\n").map { it.lines() }
        return  patterns.sumOf { mirror(it, 1) }
    }




    measureTime {
        val input = readInput("day13/Day13")
        part1(input).println()
       //part2(input).println()
    }.also { println("time: ${it.inWholeMilliseconds}ms") }

}


fun split(input: List<String>, predicate: (String) -> Boolean): List<List<String>> {

   return input.fold(mutableListOf<MutableList<String>>(mutableListOf())){ acc, curr ->

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


private fun mirror(pattern: List<String>, goal: Int): Int =
    horizontalMirror(pattern, goal) ?:
    verticalMirror(pattern, goal) ?:
    throw IllegalStateException("Pattern does not mirror")

private fun horizontalMirror(pattern: List<String>, goalTotal: Int): Int? =
    (0 until pattern.lastIndex).firstNotNullOfOrNull { start ->
        if (createMirrorRanges(start, pattern.lastIndex)
                .sumOf { (up, down) ->
                    diff(pattern[up],pattern[down])
                } == goalTotal
        ) (start + 1) * 100
        else null
    }

private fun verticalMirror(pattern: List<String>, goalTotal: Int): Int? =
    (0 until pattern.first().lastIndex).firstNotNullOfOrNull { start ->
        if (createMirrorRanges(start, pattern.first().lastIndex)
                .sumOf { (left, right) ->
                    diff(pattern.map { it[left] }.joinToString(""),pattern.map { it[right] }.joinToString(""))
                } == goalTotal
        ) start + 1
        else null
    }

private fun createMirrorRanges(start: Int, max: Int): List<Pair<Int, Int>> =
    (start downTo 0).zip(start + 1..max)


fun diff(one: String, other: String): Int =
    one.indices.count { one[it] != other[it] } + (one.length - other.length).absoluteValue
