package day01

import println
import readInput

/**
 * Get first and last digits from string
 */
fun String.toDigits(): Int {
    val numbers: String = this.filter { it.isDigit() }
    return numbers.first().digitToInt() * 10 + numbers.last().digitToInt()
}

/**
 * String replace from pairs
 */
fun String.replace(vararg pairs: Pair<String, String>): String =
        this.fold("") { acc, item ->
            var result = acc + item
            pairs.forEach { result = result.replace(it.first, it.second + item) }
            result
        }

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { it.toDigits() }
    }

    fun part2(input: List<String>): Int {
        return input
                .sumOf {
                    it.replace(
                            "one" to "1",
                            "two" to "2",
                            "three" to "3",
                            "four" to "4",
                            "five" to "5",
                            "six" to "6",
                            "seven" to "7",
                            "eight" to "8",
                            "nine" to "9"
                    ).toDigits()
                }
    }

    val testInput1 = readInput("day01/Day01_test1")
    check(part1(testInput1) == 142)

    val testInput2 = readInput("day01/Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("day01/Day01")
    part1(input).println() // 55477
    part2(input).println() // 54431
}
