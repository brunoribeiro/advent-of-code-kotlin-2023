package day05

import println
import readInput

fun main() {

    fun part1(input: List<String>): Long {

        val seeds = input.first().split(":")
            .last().split(" ")
            .filter { it.isNotBlank() }
            .map { it.trim().toLong() }

        val maps = toMaps(input)

        return seeds.map {
            maps.fold(it) { acc, curr ->
                curr.convert(acc)
            }
        }.min()
    }

    fun part2(input: List<String>): Long {

        val seedsRange = input.first().split(":")
            .last().split(" ")
            .filter { it.isNotBlank() }
            .map { it.trim().toLong() }
            .chunked(2)
            .map { it.first() until it.first() + it.last() }

        val maps = toMaps(input)

        return seedsRange.minOf { longRange ->
            longRange.minOf {
                maps.fold(it) { acc, curr ->
                    curr.convert(acc)
                }
            }
        }
    }


    check(part1(readInput("day05/Day05_test")) == 35L)
    check(part2(readInput("day05/Day05_test")) == 46L)

    val input = readInput("day05/Day05")
    part1(input).println()
    part2(input).println()

}


fun toMaps(input: List<String>): List<ConversionMap> {
    return (input.mapIndexed { index, s -> index to s }
        .partition { it.second.contains(":") }
        .first
        .drop(1)
        .map { (idx, _) -> idx } + (input.lastIndex + 1))
        .windowed(2)
        .map {
            input.subList(it.first() + 1, it.last()).filter { it.isNotBlank() }
                .map {
                    val parts = it.split(" ")
                    MapLine(parts[0].toLong(), parts[1].toLong(), parts[2].toLong())
                }
        }.map { ConversionMap(it) }
}

data class MapLine(val destination: Long, val source: Long, val length: Long)

data class ConversionMap(val lines: List<MapLine>) {
    fun convert(n: Long): Long {
        return lines.firstOrNull {
            it.source <= n && n < (it.source + it.length)
        }?.let {
            val delta = n - it.source
            it.destination + delta
        } ?: n
    }
}
