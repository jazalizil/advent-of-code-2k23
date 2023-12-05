package day05

import println
import readInput

data class Mapper(
    val destinationRange: IntRange,
    val sourceRange: IntRange
)

fun String.getSeeds(): Set<Int> {
    val split = this.split(": ", " ")
    return split.takeLast(split.size-1).mapTo(HashSet()) { it.toInt() }
}

fun String.getMapper(): Mapper {
    val data = this.split(" ").map { it.trim().toInt() }
    return Mapper(
        destinationRange = data.first()..data.first() + data.last(),
        sourceRange = data[1]..data[1] + data.last()
    )
}

fun main() {
    fun part1(input: List<String>): Int {
        var base = input.first().getSeeds();
        val seedToLocation = mutableMapOf<Int, Int>();
        for (i in 1..99) {
            seedToLocation[i] = i
        }
        for (line in 1..<input.size) {
            while (input[line].isNotEmpty() && !input[line].contains(":")) {
                val mapper = input[line].getMapper()
                for (key in mapper.sourceRange) {
                    val index = seedToLocation.replace(key, mapper.destinationRange.elementAt(key))
                    index
                }
            }
        }
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == 35)

    val input = readInput("day05/Day05")
    part1(input).println()
    part2(input).println()
}
