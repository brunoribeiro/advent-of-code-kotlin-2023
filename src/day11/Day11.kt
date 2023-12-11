package day11

import println
import readInput
import kotlin.math.abs
import kotlin.time.measureTime

fun main() {

    fun part1(input: List<String>): Long {

        val puzzle: Array<Array<SpaceItem>> = input.mapIndexed { y, line ->
            line.toCharArray().mapIndexed { x, c ->
                SpaceItem(c,x,y)
            }.toTypedArray()
        }.toTypedArray()

        return calc(puzzle,1)
    }

    fun part2(input: List<String>): Long {

        val factor = 1000000
        val puzzle: Array<Array<SpaceItem>> = input.mapIndexed { y, line ->
            line.toCharArray().mapIndexed { x, c ->
                SpaceItem(c,x,y)
            }.toTypedArray()
        }.toTypedArray()


        return calc(puzzle,factor-1)
    }

    measureTime {
        val input = readInput("day11/Day11")
         part1(input).println()
          part2(input).println()
    }.also { println("time: ${it.inWholeMilliseconds}ms") }

}

data class SpaceItem(val char: Char,val x: Int, val y: Int)
val EMPTY_ITEM = SpaceItem('.',-1,-1)
fun rotateMap(puzzle: Array<Array<SpaceItem>>): Array<Array<SpaceItem>> {

    val rotated: Array<Array<SpaceItem>> = Array(puzzle.size) {
        Array(puzzle.size) {
            EMPTY_ITEM
        }
    }
    puzzle.forEachIndexed { y, chars ->
        chars.forEachIndexed { x, c ->
            rotated[y][x] = puzzle[x][y]
        }
    }

    return rotated
}


fun calc(puzzle: Array<Array<SpaceItem>>, factor: Int): Long {
    val expandRows = puzzle.filter { it.all { it.char == '.' } }.flatMap { it.map { it.y } }.distinct()
    val expandColumns = rotateMap(puzzle).filter { it.all { it.char == '.' } }.flatMap { it.map { it.x } }.distinct()
    val galaxies = puzzle.flatMap { it.filter { it.char == '#' } }

    val expanded  = galaxies.map { g ->
        val incx =  expandColumns.count { it < g.x } * (factor)
        val incy =  expandRows.count { it < g.y } * (factor)
        g.copy(x = g.x + incx, y = g.y + incy)
    }

    val distances = mutableListOf(0L)

    (0 until expanded.size-1).forEach{  i ->
        (i+1 until expanded.size).forEach {  j ->

            val p1 = expanded[i]
            val p2 = expanded[j]
            val distance = abs(p1.x -p2.x) + abs(p1.y -p2.y)
            distances.add(distance.toLong())

        }

    }

    return distances.sum()

}
