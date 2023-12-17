package day16

import day16.BeamState.ACTIVE
import day16.BeamState.DONE
import day16.Direction.*
import println
import readInput
import kotlin.time.measureTime

fun main() {


    fun getEnergy(start: Point, puzzle: Array<Array<Point>>, direction: Direction): Int {
        val startDirection = when (start.c) {
            '/' -> NORTH
            '\\' -> SOUTH
            '|' -> SOUTH
            else -> direction
        }
        val cave = Cave(
            map = puzzle,
            beams = mutableListOf(Beam(currentLocation = start, startDirection)),
            energized = mutableSetOf()
        )


        while (!cave.isEnergized()) {
            cave.moveBeams()
        }

        return cave.energized.distinctBy { it.second }.size
    }

    fun part1(input: List<String>): Int {

        val puzzle: Array<Array<Point>> = input.mapIndexed { y, line ->
            line.toCharArray().mapIndexed { x, c ->
                Point(c, x, y)
            }.toTypedArray()
        }.toTypedArray()


        val start = puzzle[0][0]
        return getEnergy(start, puzzle, EAST)

    }


    fun part2(input: List<String>): Int {


        val puzzle: Array<Array<Point>> = input.mapIndexed { y, line ->
            line.toCharArray().mapIndexed { x, c ->
                Point(c, x, y)
            }.toTypedArray()
        }.toTypedArray()


        var max = 0

        puzzle[0].forEach {

            val energy = getEnergy(it, puzzle, SOUTH)

            if (energy > max) {
                max = energy
                return@forEach
            }
        }

        puzzle[puzzle.size - 1]
            .forEach {

                val energy = getEnergy(it, puzzle, NORTH)

                if (energy > max) {
                    max = energy
                    return@forEach
                }
            }


        val columns = puzzle.map { it.groupBy { it.x } }.map { it.values.flatten() }

        columns[0].toList().forEach {
            val energy = getEnergy(it, puzzle, EAST)

            if (energy >= max) {
                max = energy
                return@forEach
            }
        }

        columns[puzzle[0].size - 1].toList().forEach {

            val energy = getEnergy(it, puzzle, WEST)

            if (energy > max) {
                max = energy
                return@forEach
            }
        }


        return max


    }


    check(part1(readInput("day16/Day16_test")) == 46)
    check(part2(readInput("day16/Day16_test")) == 51)

    measureTime {
        val input = readInput("day16/Day16")
        part1(input).println()
        part2(input).println()
    }.also { println("time: ${it.inWholeMilliseconds}ms") }

}


data class Beam(val currentLocation: Point, val currentDirection: Direction, val state: BeamState = ACTIVE)
class Cave(
    val map: Array<Array<Point>>,
    var beams: List<Beam>,
    val energized: MutableSet<Pair<Direction, Point>>
) {
    fun isEnergized(): Boolean {
        return beams.all { it.state == DONE }
    }

    fun moveBeams() {

        beams = beams.filter { it.state == ACTIVE }.flatMap { beam: Beam ->

            if (energized.any { it.second == beam.currentLocation && it.first == beam.currentDirection })
                return@flatMap listOf(beam.copy(state = DONE))
            else
                energized.add(beam.currentDirection to beam.currentLocation)

            try {

                val nextX = beam.currentLocation.x + beam.currentDirection.x
                val nextY = beam.currentLocation.y + beam.currentDirection.y

                val next = map[nextY][nextX]


                if (beam.state == DONE)
                    listOf(beam)
                else
                    when (next.c) {
                        '.' -> {
                            listOf(beam.copy(currentLocation = next))
                        }

                        '/' -> {
                            val newDirection = when (beam.currentDirection) {
                                NORTH -> EAST
                                SOUTH -> WEST
                                WEST -> SOUTH
                                EAST -> NORTH
                            }

                            listOf(beam.copy(currentLocation = next, currentDirection = newDirection))

                        }

                        '\\' -> {
                            val newDirection = when (beam.currentDirection) {
                                NORTH -> WEST
                                SOUTH -> EAST
                                WEST -> NORTH
                                EAST -> SOUTH
                            }

                            listOf(beam.copy(currentLocation = next, currentDirection = newDirection))
                        }

                        '-' -> {

                            listOf(
                                beam.copy(currentLocation = next, currentDirection = WEST),
                                beam.copy(currentLocation = next, currentDirection = EAST),
                                beam.copy(state = DONE),
                            )
                        }

                        '|' -> {

                            listOf(
                                beam.copy(currentLocation = next, currentDirection = SOUTH),
                                beam.copy(currentLocation = next, currentDirection = NORTH),

                                )
                        }

                        else -> listOf(beam)
                    }


            } catch (e: ArrayIndexOutOfBoundsException) {
                listOf(beam.copy(state = DONE))
            }

        }

    }


}

data class Point(val c: Char, val x: Int, val y: Int)
enum class Direction(val x: Int, val y: Int) {
    NORTH(0, -1),
    SOUTH(0, 1),
    WEST(-1, 0),
    EAST(1, 0),
}

enum class BeamState {
    ACTIVE,
    DONE
}
