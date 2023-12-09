package day08

import println
import readInput
import kotlin.time.measureTime





fun main() {

    fun part1(input: List<String>): Int {

        val directions = input.first()
        val map = input.drop(1).filter { it.isNotBlank() }.map {
            val tmp = it.split("=").map { it.trim() }
            val name = tmp.first()
            val connections = tmp.last().replace("(","").replace(")","").split(",")
            name.trim() to ( connections.first().trim() to connections.last().trim())
        }.toMap()

        return path(directions, map, "AAA")

    }



    fun part2(input: List<String>): Long {

        val directions = input.first()
        val map = input.drop(1).filter { it.isNotBlank() }.map {
            val tmp = it.split("=").map { it.trim() }
            val name = tmp.first()
            val connections = tmp.last().replace("(","").replace(")","").split(",")
            name.trim() to ( connections.first().trim() to connections.last().trim())
        }.toMap()

        return map.keys.filter { location -> location.last() == 'A' }.map { start ->
            path(directions, map, start).toBigInteger()
        }.reduce { acc, curr -> acc * curr / acc.gcd(curr) }.toLong()

    }


    check(part1(readInput("day08/Day08_test")) == 6)
    check(part2(readInput("day08/Day08_test")) == 6L)


    measureTime {
        val input = readInput("day08/Day08")
         part1(input).println()
         part2(input).println()
    }.also { println("time: ${it.inWholeMilliseconds}ms") }

}


fun path(directions: String, map: Map<String, Pair<String, String>>, start: String): Int {
    var i = 0
    var location = start
    do {
        val (l, r) = map[location] ?: TODO()
        location = if (directions[i++ % directions.length] == 'L') l else r
    } while (!location.endsWith('Z'))
    return i
}
