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

data class Hand(val cards: String, val bid: Int, val special: Boolean = false) : Comparable<Hand> {

    private fun typeWithJokers(): HandType {
        val jokers = cards.filter { it == 'J' }
        val regularType = regularType()
        return when {
            jokers.isEmpty() -> regularType
            jokers.length == 5 -> HandType.FIVE_OF_A_KIND
            jokers.length == 4 -> HandType.FIVE_OF_A_KIND
            jokers.length == 3 -> when (regularType) {
                HandType.FULL_HOUSE -> HandType.FIVE_OF_A_KIND
                else -> HandType.FOUR_OF_A_KIND
            }

            jokers.length == 2 -> when (regularType) {
                HandType.FULL_HOUSE -> HandType.FIVE_OF_A_KIND
                HandType.TWO_PAIR -> HandType.FOUR_OF_A_KIND
                else -> HandType.THREE_OF_A_KIND
            }

            else -> when (regularType) {
                HandType.FOUR_OF_A_KIND -> HandType.FIVE_OF_A_KIND
                HandType.THREE_OF_A_KIND -> HandType.FOUR_OF_A_KIND
                HandType.TWO_PAIR -> HandType.FULL_HOUSE
                HandType.ONE_PAR -> HandType.THREE_OF_A_KIND
                else -> HandType.ONE_PAR
            }

        }
    }

    private fun regularType(): HandType {

        val distinctCardsGroups = cards.groupBy { it }

        return when (distinctCardsGroups.size) {
            1 -> HandType.FIVE_OF_A_KIND
            5 -> HandType.HIGH_CARD
            else -> {
                when {
                    distinctCardsGroups.any { it.value.size == 4 } -> HandType.FOUR_OF_A_KIND
                    distinctCardsGroups.any { it.value.size == 3 } -> {
                        when {
                            distinctCardsGroups.size == 2 -> HandType.FULL_HOUSE
                            else -> HandType.THREE_OF_A_KIND
                        }
                    }

                    else -> {
                        when {
                            distinctCardsGroups.filter { it.value.size == 2 }.size == 2 -> HandType.TWO_PAIR
                            else -> HandType.ONE_PAR
                        }
                    }
                }
            }
        }
    }

    override fun compareTo(other: Hand): Int {

        val type1 = if (special) this.typeWithJokers() else this.regularType()
        val type2 = if (special) other.typeWithJokers() else other.regularType()
        val map = if (special) specialCardOrder else regularCardOrder

        return when {
            type1 == type2 -> cards.indices.firstOrNull { cards[it] != other.cards[it] }
                ?.let { (map[cards[it]] ?: 0) - (map[other.cards[it]] ?: 0) } ?: 0

            else -> type1.ordinal - type2.ordinal
        }
    }
}

val regularCardOrder = mapOf(
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


val specialCardOrder = mapOf(
    'A' to 14,
    'K' to 13,
    'Q' to 12,
    'J' to 1,
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
    FIVE_OF_A_KIND
}
