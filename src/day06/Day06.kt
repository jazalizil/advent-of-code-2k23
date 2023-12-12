package day06

import println
import readInput

data class Record(val time: Long, val distance: Long);

fun List<String>.getRecords() : Set<Record> {
    val times = this[0].split(":", " ").drop(1).filter { it.isNotEmpty() }.map { it.toLong() }
    val distances = this[1].split(":", " ").drop(1).filter { it.isNotEmpty() }.map { it.toLong() }

    return times.mapIndexedTo(HashSet()) { index, time -> Record(time=time, distance=distances[index])}
}

fun List<String>.getRecord(): Record {
    val time = this[0].split(":").last().filter { it.isDigit() }.toLong()
    val distance = this[1].split(":").last().filter { it.isDigit() }.toLong()

    return Record(time, distance)
}

fun main() {
    fun part1(input: List<String>): Int {
        val records = input.getRecords()

        return records.map { record ->
            (1..<record.time).count { it * (record.time - it) > record.distance }
        }.fold(1) { acc, curr -> acc * curr }
    }

    fun part2(input: List<String>): Int {
        val record = input.getRecord()
        return (1..<record.time).count { it * (record.time - it) > record.distance }
    }

    val testInput = readInput("day06/Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("day06/Day06")
    part1(input).println()
    part2(input).println()
}