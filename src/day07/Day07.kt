package day07

import println
import readInput
import kotlin.math.pow

val Cards = sequenceOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
enum class HandType {
    FIVE_KIND, FOUR_KIND, FULL, THREE_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD;
}

typealias HandStrength = Pair<HandType, Char>

data class Hand(val types : List<HandType>, val bid : Int, val base : String)

fun List<Char>.getHand(bid : Int) : Hand {
    val combinations = mutableListOf<HandStrength>()
    for (card in this) {
        val combinationIndex = combinations.indexOfFirst { it.second == card }
        if (combinationIndex < 0) {
            combinations.add(HandStrength(HandType.HIGH_CARD, card))
            continue
        }
        combinations.apply {
            val combination = this[combinationIndex]
            this[combinationIndex] = when (combination.first) {
                HandType.HIGH_CARD -> HandStrength(HandType.ONE_PAIR, card)
                HandType.ONE_PAIR -> HandStrength(HandType.THREE_KIND, card)
                HandType.TWO_PAIR -> HandStrength(HandType.FULL, card)
                HandType.THREE_KIND -> HandStrength(HandType.FOUR_KIND, card)
                else -> HandStrength(HandType.FIVE_KIND, card)
            }
        }
        val pairCount = combinations.count { it.first == HandType.ONE_PAIR }
        if (pairCount == 2) {
            combinations.replaceAll { elem -> if (elem.first == HandType.ONE_PAIR) HandStrength(HandType.TWO_PAIR, elem.second) else elem  }
        }
    }
    return Hand(combinations.map { it.first }.sorted(), bid, this.toString())
}


fun main() {
    fun part1(input: List<String>): Int {
        var ret = input.map { line ->
            val (numberSplit, bid) = line.split(" ")
            val hand = numberSplit.toList().getHand(bid.toInt())
            hand
        }
        ret = ret.sortedBy { it.base }
        "list sorted by combination:: ${ret.forEach {
            it.println() 
        }}".println()
        return ret.mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }


    val testInput = readInput("day07/Day07_test")
    part1(testInput).println()
    check(part1(testInput) == 6440)
//    check(part2(testInput) == 71503)

    val input = readInput("day07/Day07")
//    part1(input).println() // 245980028
//    part2(input).println()
}
