package day01

import println
import readInput

val one = mapOf(
    "1" to 1,
    "2" to 2,
    "3" to 3,
    "4" to 4,
    "5" to 5,
    "6" to 6,
    "7" to 7,
    "8" to 8,
    "9" to 9
)


val two = one + mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)


fun str2Code(str: String, dic: Map<String, Int>): Int {

    val lowest = dic.keys
        .map { it to str.indexOf(it) }
        .filter { (_, value) -> value != -1 }
        .minBy { (_, value) -> value }
        .let { (key, _) -> key }
        .let { dic[it] } ?: 0

    val highest = dic.keys
        .map { it to str.lastIndexOf(it) }
        .filter { (_, value) -> value != -1 }
        .maxBy { (_, value) -> value }
        .let { (key, _) -> key }
        .let { dic[it] } ?: 0

    return lowest * 10 + highest
}

fun main() {

    fun part1(input: List<String>): Int = input.fold(0) { acc, curr ->
        acc + str2Code(curr, one)
    }

    fun part2(input: List<String>): Int = input.fold(0) { acc, curr ->
        acc + str2Code(curr, two)
    }

    check(part1(readInput("day01/Day01_test")) == 142)
    check(part2(readInput("day01/Day01_2_test")) == 281)

    val input = readInput("day01/Day01")
    part1(input).println()
    part2(input).println()
}
