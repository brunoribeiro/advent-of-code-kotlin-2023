package day02

import println
import readInput

fun main() {

    fun part1(input: List<String>): Int {

        val numbers = puzzleItems("[^0-9]".toRegex(), input)
        val symbols = puzzleItems("[0-9]".toRegex(), input)

        return numbers.filter { n ->
            symbols.any { s -> isAdjacent(s, n) }
        }.sumOf { it.token.toInt() }
    }

    fun part2(input: List<String>): Int {

        val numbers = puzzleItems("[^0-9]".toRegex(), input)
        val symbols = puzzleItems("[0-9]".toRegex(), input)

        return symbols.map { s ->
            s to numbers.filter { n -> isAdjacent(s, n) }
        }.filter { it.second.size > 1 }
            .map {
                it.first to it.second.map { it.token.toInt() }.reduce { acc, i ->
                    acc * i
                }
            }.sumOf { it.second }

    }

    check(part1(readInput("day03/Day03_test")) == 4361)
    check(part2(readInput("day03/Day03_test")) == 467835)

    val input = readInput("day03/Day03")
    part1(input).println()
    part2(input).println()
}

fun isAdjacent(s: PuzzleItem, n: PuzzleItem) =
    ((s.point.y == n.point.y - 1 && s.point.x in (n.point.x - 1..n.point.x + n.token.length))
            || (s.point.y == n.point.y + 1 && s.point.x in (n.point.x - 1..n.point.x + n.token.length))
            || (s.point.y == n.point.y && s.point.x == n.point.x - 1)
            || (s.point.y == n.point.y && s.point.x == n.point.x + n.token.length))

fun puzzleItems(regex: Regex, input: List<String>) =
    input.foldIndexed(listOf<PuzzleItem>()) { y, acc, str ->
        acc + str.split(regex)
            .flatMap { it.split(".") }
            .foldIndexed(listOf()) { x, lineAcc, number ->
                val cont = lineAcc.sumOf { it.token.length }
                lineAcc + PuzzleItem(number.trim(), Point(x + cont, y))
            }
    }.filter { it.token.isNotBlank() }

data class PuzzleItem(val token: String, val point: Point)
data class Point(val x: Int, val y: Int)
