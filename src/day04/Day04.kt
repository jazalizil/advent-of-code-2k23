package day04

import println
import readInput
import kotlin.math.pow

fun String.getNumbers(): Set<Int> = this.split(' ').filter { it.isNotEmpty() }.mapTo(HashSet()) { it.toInt() }

fun String.getCommonNumbers(): Set<Int> {
    val (_, card, winning) = this.split(": ", " | ")
    return card.getNumbers().intersect(winning.getNumbers())
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val common = line.getCommonNumbers()
            if (common.isEmpty()) 0 else 2.0.pow(common.size - 1.0).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val instances = IntArray(input.size) { 1 }
        input.forEachIndexed { index, line ->
            line.getCommonNumbers().indices.forEach { instances[index + it + 1] += instances[index] }
        }
        return instances.sum()
    }

    val testInput = readInput("day04/Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("day04/Day04")
    part1(input).println()
    part2(input).println()
}
