package day10

import println
import readInput
import kotlin.math.abs
import kotlin.time.measureTime


typealias Position = Pair<Int, Int>


fun main() {


    fun path(grid: List<String>, startPosition: Position): List<Position> {

        val tmpLoop = mutableListOf<Position>()
        for (startDir in Cardinal.entries) {
            tmpLoop.clear()
            tmpLoop.add(startPosition)
            val start = getNext(grid,startPosition, startDir) ?: continue
            var curPos = start.first
            var curDir = start.second
            while (grid[curPos.first][curPos.second] != 'S') {
                tmpLoop.add(curPos)
                val (nextPos, nextDir) = getNext(grid,curPos, curDir) ?: break
                curPos = nextPos
                curDir = nextDir
            }
            if (grid[curPos.first][curPos.second] == 'S') break
        }
        return tmpLoop
    }

    fun part1(input: List<String>): Int {

        val x = input.indexOfFirst { s -> 'S' in s }
        val start = x to input[x].indexOf('S')
        val path = path(input, start)

        return path.size / 2
    }

    fun part2(input: List<String>): Int {

        val x = input.indexOfFirst { s -> 'S' in s }
        val start = x to input[x].indexOf('S')
        val path = path(input, start)

        return (1 ..< input.size - 1).sumOf { x ->
            val idx = input[x].indices.filter { y ->
                val i1 = path.indexOf(x to y)
                val i2 = path.indexOf(x + 1 to y)
                i1 != -1 && i2 != -1 && (abs(i1 - i2) == 1 || i1 in listOf(0, path.lastIndex) && i2 in listOf(0, path.lastIndex))
            }
            (idx.indices step 2).sumOf { i ->
                (idx[i] .. idx[i + 1]).count { y -> x to y !in path }
            }
        }
    }

    check(part1(readInput("day10/Day10_test")) == 4)
    check(part2(readInput("day10/Day10_test2")) == 4)


    measureTime {
        val input = readInput("day10/Day10")
        part1(input).println()
        part2(input).println()
    }.also { println("time: ${it.inWholeMilliseconds}ms") }

}


private fun positionToCardinal(grid:List<String>,pos: Position): List<Cardinal> =
    when (grid[pos.first][pos.second]) {
        '|' -> listOf(Cardinal.NORTH, Cardinal.SOUTH)
        '-' -> listOf(Cardinal.EAST, Cardinal.WEST)
        'L' -> listOf(Cardinal.NORTH, Cardinal.EAST)
        'J' -> listOf(Cardinal.NORTH, Cardinal.WEST)
        '7' -> listOf(Cardinal.SOUTH, Cardinal.WEST)
        'F' -> listOf(Cardinal.SOUTH, Cardinal.EAST)
        'S' -> listOf(Cardinal.NORTH, Cardinal.SOUTH, Cardinal.EAST, Cardinal.WEST)
        '.' -> listOf()
        else -> error("Missed a char")
    }

private operator fun Position.plus(other: Position): Position =
    first + other.first to second + other.second

enum class Cardinal {
    NORTH, EAST, SOUTH, WEST
}

private fun Cardinal.toPosition(): Position = when (this) {
    Cardinal.NORTH -> -1 to 0
    Cardinal.EAST -> 0 to 1
    Cardinal.SOUTH -> 1 to 0
    Cardinal.WEST -> 0 to -1
}

private fun Cardinal.opposite(): Cardinal = when (this) {
    Cardinal.NORTH -> Cardinal.SOUTH
    Cardinal.EAST -> Cardinal.WEST
    Cardinal.SOUTH -> Cardinal.NORTH
    Cardinal.WEST -> Cardinal.EAST
}

fun getNext(grid:List<String>, pos: Position, dir: Cardinal): Pair<Position, Cardinal>? {
    val nextPos = pos + dir.toPosition()
    val nextDirs = positionToCardinal(grid,nextPos)
    if (dir.opposite() !in nextDirs) return null
    return nextPos to nextDirs.minus(dir.opposite()).first()
}
