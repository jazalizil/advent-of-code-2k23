package day03

import println
import readInput

val NumberRegex = Regex("([0-9])+")
fun Char.isSymbol(): Boolean {
    return !this.isDigit() && this != '.'
}
fun Char.isGear(): Boolean = this == '*'

fun List<String>.findAdjacentIndexes(lineIndex: Int, charIndexes: IntRange, callback: Char.() -> Boolean): Pair<Int, Int>? {
    if (lineIndex-1 in this.indices && charIndexes.any { it in this[lineIndex-1].indices && callback.invoke(this[lineIndex-1][it]) }) {
        return Pair(lineIndex-1, charIndexes.find { it in this[lineIndex-1].indices && callback.invoke(this[lineIndex-1][it]) }!!)
    } else if (charIndexes.any { it in this[lineIndex].indices && callback.invoke(this[lineIndex][it]) }) {
        return Pair(lineIndex, charIndexes.find { it in this[lineIndex].indices && callback.invoke(this[lineIndex][it]) }!!)
    } else if (lineIndex+1 in this.indices && charIndexes.any { it in this[lineIndex+1].indices && callback.invoke(this[lineIndex+1][it]) }) {
        return Pair(lineIndex+1, charIndexes.find { it in this[lineIndex+1].indices && callback.invoke(this[lineIndex+1][it]) }!!)
    }
    return null
}

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .mapIndexed { index, line ->
                NumberRegex.findAll(line)
                    .filter { input.findAdjacentIndexes(index, it.range.first-1..it.range.last+1, Char::isSymbol) != null }
                    .map { it.value.toInt() }
                    .sum()
            }.sum()
    }

    fun part2(input: List<String>): Int {
        return input
            .asSequence()
            .mapIndexed { index, line ->
                NumberRegex.findAll(line)
                    .map { number ->
                        val gearIndex = input.findAdjacentIndexes(index, number.range.first-1..number.range.last+1, Char::isGear)
                        object {
                            val gearIndex = gearIndex
                            val number = number.value
                        }
                    }
                    .toList()
            }.flatten()
            .groupByTo(HashMap(), { it.gearIndex }, { it.number.toInt() })
            .filter { it.key !== null && it.value.size == 2 }
            .map { it.value.first() * it.value.last() }
            .sum()
    }

    val testInput = readInput("day03/Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("day03/Day03")
    part1(input).println()
    part2(input).println()
}
