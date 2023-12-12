package day05

import kotlinx.coroutines.flow.*
import mapAsync
import println
import readInput

class Mapper(
    val range: UIntRange,
    val startValue: UInt,
) {
    fun getResult(fromValue: UInt): UInt {
        return this.startValue + fromValue - this.range.first
    }
}

fun String.getSeeds(): List<UInt> {
    val split = this.split(": ", " ")
    return split.takeLast(split.size-1).map { it.toUInt() }
}

fun String.getSeedRanges(): List<UIntRange> {
    val split = this.split(": ", " ")
    return split.takeLast(split.size-1).chunked(2).map { it[0].toUInt()..<(it[0].toUInt())+it[1].toUInt() }
}

fun String.getMapper(): Mapper {
    val data = this.split(" ").map { it.trim().toUInt() }
    return Mapper(
        range = data[1]..data[1] + data.last(),
        startValue = data[0]
    )
}

fun List<String>.mapSeedInRangeRecursive(from: UInt): UInt {
    if (this.isEmpty()) {
        return from
    }
    if (this.first().isEmpty()||this.first().contains(":")) {
        return this.takeLast(this.size-1).mapSeedInRangeRecursive(from)
    }
    val mapper = this.first().getMapper()
    if (from !in mapper.range) {
        return this.takeLast(this.size-1).mapSeedInRangeRecursive(from)
    }
    val nextTransformIndex = this.indexOfFirst { it.contains(":") }
    if (nextTransformIndex > 0)
        return this.takeLast(this.size-nextTransformIndex).mapSeedInRangeRecursive(mapper.getResult(from))
    return mapper.getResult(from)
}

val UIntRange.min
    get() = minOf(start, endInclusive)

val UIntRange.max
    get() = maxOf(start, endInclusive)

val UIntRange.size
    get() = max - min
infix fun UIntRange.intersect(other: UIntRange) : UIntRange? =
    if (min <= other.max && other.min <= max)
        maxOf(min, other.min).rangeTo(minOf(max, other.max))
    else
        null

fun List<String>.mapSeedInRangeIterative(seeds: UIntRange) : UInt {
    var finals = listOf<UInt>()
    var lines = this
    while (lines.isNotEmpty()) {
        if (lines.first().isEmpty() || lines.first().contains(":")) {
            lines = lines.takeLast(lines.size - 1)
            continue;
        }
        val mapper = lines.first().getMapper()
        val nextIndex = lines.indexOfFirst { it.contains(":") }
        val inter = seeds.intersect(mapper.range)
        if (inter == null) {
            lines = lines.takeLast(lines.size - 1)
            continue
        }
        finals = inter.mapIndexed { index, it -> mapper.getResult(if (index in finals.indices) finals[index] else it) }
        if (nextIndex < 0) {
            break;
        }
        lines = lines.takeLast(lines.size - nextIndex)
    }
    return finals.min()
}



suspend fun main() {
    fun part1(input: List<String>): UInt {
        val seeds = input.first().getSeeds()
        return seeds.minOf { input.takeLast(input.size-1).mapSeedInRangeRecursive(it) }
    }

    suspend fun part2(input: List<String>): UInt {
        val seeds = input.first().getSeedRanges()
        return seeds.asFlow().toList().mapAsync { range ->
            "processing ${range.count()} values in range $range".println()
            val final = range.minOf {  input.takeLast(input.size - 1).mapSeedInRangeRecursive(it) }
            "done process range $range with $final".println()
            final
        }.min()
    }

    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == 35U)
    check(part2(testInput) == 46U)

    val input = readInput("day05/Day05")
    part1(input).println()
    part2(input).println()
}