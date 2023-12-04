package day04

import println
import readInput
import kotlin.math.pow

fun main() {

    fun part1(input: List<String>): Int = toCards(input).sumOf { card ->
        card.numbers.filter { card.winnings.contains(it) }
            .let {
                when (it.size) {
                    0 -> 0
                    1 -> 1
                    else -> 2.0.pow((it.size - 1).toDouble()).toInt()
                }
            }
    }

    fun part2(input: List<String>): Int = toCards(input).let { cards ->
        cards.foldIndexed(cards.associate { it.cardId to 1 }) { idx, acc, curr ->
            val currentCard = idx + 1
            val wins = curr.numbers.filter { curr.winnings.contains(it) }
            val cardsWon = (currentCard + 1)..currentCard + wins.size
            val currentCardAmount = (acc[currentCard] ?: 0)
            acc + cardsWon.map {
                it to (acc[it] ?: 0) + currentCardAmount
            }

        }.values.sum()
    }

    check(part1(readInput("day04/Day04_test")) == 13)
    check(part2(readInput("day04/Day04_test")) == 30)

    val input = readInput("day04/Day04")
    part1(input).println()
    part2(input).println()
}

fun toCards(input: List<String>) = input.map {
    it.split(":")
        .let {
            val parts = it.last().split("|")
                .map { it.split(" ").filter { it.isNotBlank() }.map { it.toInt() } }
            Card(it.first().split(" ").last().toInt(), parts.first(), parts.last())
        }
}


data class Card(val cardId: Int, val winnings: List<Int>, val numbers: List<Int>)
