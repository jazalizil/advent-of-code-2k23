package day07

import println
import readInput
import kotlin.math.pow

val Cards = sequenceOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
enum class HandType {
    FULL, FIVE_KIND, FOUR_KIND, THREE_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD;
}

typealias HandStrength = Pair<HandType, Set<Char>>

data class Hand(val combinations : Map<HandType, List<HandStrength>>, val bid : Int)

fun List<Char>.getHand(bid : Int) : Hand {
    val combinations = mutableListOf<HandStrength>()
    for (card in this) {
        val combinationIndex = combinations.indexOfFirst { it.second.contains(card) }
        if (combinationIndex < 0) {
            combinations.add(HandStrength(HandType.HIGH_CARD, setOf(card)))
            continue
        }
        combinations.apply {
            val combination = this[combinationIndex]
            this[combinationIndex] = when (combination.first) {
                HandType.HIGH_CARD -> HandStrength(HandType.ONE_PAIR, combination.second.plus(card))
                HandType.ONE_PAIR -> HandStrength(HandType.THREE_KIND, combination.second.plus(card))
                HandType.TWO_PAIR -> HandStrength(HandType.FULL, combination.second.plus(card))
                HandType.THREE_KIND -> HandStrength(HandType.FOUR_KIND, combination.second.plus(card))
                else -> HandStrength(HandType.FIVE_KIND, combination.second.plus(card))
            }
        }
    }
    val pairCount = combinations.count { it.first == HandType.ONE_PAIR }
    if (pairCount == 2) {
        combinations.replaceAll { elem -> if (elem.first == HandType.ONE_PAIR) HandStrength(HandType.TWO_PAIR, elem.second) else elem  }
    }
    return Hand(combinations.groupBy { it.first }.toSortedMap(), bid)
}


fun main() {
    fun part1(input: List<String>): Int {
        val ret = input.map { line ->
            val (numberSplit, bid) = line.split(" ")
            val numbers = numberSplit.toList().sortedBy { -Cards.indexOf(it) }
            val hand = numbers.getHand(bid.toInt())
            hand
        }.sortedBy { hand ->
            hand.combinations.maxOf { cb ->
                cb.value.maxOf { it.second.sumOf { Cards.indexOf(it) } } + 4.0.pow((HandType.entries.size - cb.key.ordinal))
            }
        }
        return ret.mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }


    val testInput = readInput("day07/Day07_test")
    check(part1(testInput) == 6440)
//    check(part2(testInput) == 71503)

    val input = readInput("day07/Day07")
    part1(input).println() // 245980028
//    part2(input).println()
}