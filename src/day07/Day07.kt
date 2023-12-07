package day07

import println
import readInput
import kotlin.time.measureTime


fun main() {

    fun part1(input: List<String>): Int = input.map {
        it.split(" ").let { Hand(it.first(), it.last().toInt()) }
    }.sortedBy { it }
        .mapIndexed { idx, hand ->
            hand.bid * (idx + 1)
        }.sum()

    fun part2(input: List<String>): Int = input.map {
        it.split(" ").let { Hand(it.first(), it.last().toInt(), true) }
    }.sortedBy { it }
        .mapIndexed { idx, hand ->
            hand.bid * (idx + 1)
        }.sum()

    check(part1(readInput("day07/Day07_test")) == 6440)
    check(part2(readInput("day07/Day07_test")) == 5905)


    measureTime {
        val input = readInput("day07/Day07")
        part1(input).println()
        part2(input).println()
    }.also { println("time: ${it.inWholeMilliseconds}ms") }

}


val hands = mapOf(
    listOf(5) to HandType.FIVE_OF_A_KIND,
    listOf(4, 1) to HandType.FOUR_OF_A_KIND,
    listOf(3, 2) to HandType.FULL_HOUSE,
    listOf(3, 1, 1) to HandType.THREE_OF_A_KIND,
    listOf(2, 2, 1) to HandType.TWO_PAIR,
    listOf(2, 1, 1, 1) to HandType.ONE_PAR,
    listOf(1, 1, 1, 1, 1) to HandType.HIGH_CARD
)

val cardOrderMap = mapOf(
    'A' to 14,
    'K' to 13,
    'Q' to 12,
    'J' to 11,
    'T' to 10,
    '9' to 9,
    '8' to 8,
    '7' to 7,
    '6' to 6,
    '5' to 5,
    '4' to 4,
    '3' to 3,
    '2' to 2
)


enum class HandType {

    HIGH_CARD,
    ONE_PAR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND;

    fun withJokers(jokers: Int): HandType {
        return when (jokers) {
            0 -> this
            in (5 downTo 4) -> FIVE_OF_A_KIND
            3 -> when (this) {
                FULL_HOUSE -> FIVE_OF_A_KIND
                else -> FOUR_OF_A_KIND
            }

            2 -> when (this) {
                FULL_HOUSE -> FIVE_OF_A_KIND
                TWO_PAIR -> FOUR_OF_A_KIND
                else -> THREE_OF_A_KIND
            }

            else -> when (this) {
                FOUR_OF_A_KIND -> FIVE_OF_A_KIND
                THREE_OF_A_KIND -> FOUR_OF_A_KIND
                TWO_PAIR -> FULL_HOUSE
                ONE_PAR -> THREE_OF_A_KIND
                else -> ONE_PAR
            }

        }
    }
}


data class Hand(val cards: String, val bid: Int, val special: Boolean = false) : Comparable<Hand> {

    private fun type(): HandType {
        val key = cards.groupBy { it }
            .map { it.value.size }.sorted().asReversed()
        val type = hands[key] ?: throw IllegalArgumentException()
        return if (special) type.withJokers(jokers()) else type
    }

    private fun jokers() = cards.filter { it == 'J' }.length

    override fun compareTo(other: Hand): Int {

        val type1 = type()
        val type2 = other.type()
        val cardMap = if (special) cardOrderMap + ('J' to 1) else cardOrderMap

        return when {
            type1 == type2 -> cards.indices.firstOrNull { cards[it] != other.cards[it] }
                ?.let { (cardMap[cards[it]] ?: 0) - (cardMap[other.cards[it]] ?: 0) } ?: 0

            else -> type1.ordinal - type2.ordinal
        }
    }
}
