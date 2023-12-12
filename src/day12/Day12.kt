package day12

import println
import readInput
import kotlin.time.measureTime

fun main() {

    fun part1(input: List<String>): Long {

        val map = input.map { line -> line.split(" ").let { (l, r) -> l to r } }

        return map.sumOf { (s, counts) ->
            arrangements(s, counts.split(",").map(String::toInt))
        }
    }

    fun part2(input: List<String>): Long {

        val map = input.map { line -> line.split(" ").let { (l, r) -> l to r } }
        return map.sumOf { (s, counts) ->
            arrangements("$s?$s?$s?$s?$s", "$counts,$counts,$counts,$counts,$counts".split(",").map(String::toInt))
        }
    }


    check(part1(readInput("day12/Day12_test")) == 21L)
    check(part2(readInput("day12/Day12_test")) == 525152L)

    measureTime {
        val input = readInput("day12/Day12")
        part1(input).println()
        part2(input).println()
    }.also { println("time: ${it.inWholeMilliseconds}ms") }

}
fun arrangements(s: String, ls: List<Int>): Long {
    val memo = IntArray(s.length) { i -> s.drop(i).takeWhile { c -> c != '.' }.length }

    val dp = mutableMapOf<Pair<Int, Int>, Long>()

    fun canTake(i: Int, l: Int) = memo[i] >= l && (i + l == s.length || s[i + l] != '#')
    fun helper(si: Int, lsi: Int): Long =
        when {
            lsi == ls.size -> if (s.drop(si).none { c -> c == '#' }) 1L else 0
            si >= s.length -> 0L
            else -> {
                if (dp[si to lsi] == null) {
                    val take = when {
                        canTake(si, ls[lsi]) -> helper(si + ls[lsi] + 1, lsi + 1)
                        else -> 0L
                    }
                    val dontTake = when {
                        s[si] != '#' -> helper(si + 1, lsi)
                        else -> 0L
                    }
                    dp[si to lsi] = take + dontTake
                }
                dp[si to lsi] ?: 0
            }
        }
    return helper(0, 0)
}
