package day02

import println
import readInput

enum class Cube(val color: String) {
    RED("red"), GREEN("green"), BLUE("blue");
}

typealias Set = Pair<Int, Cube>

class Game(line: String) {
    val id: Int
    private val sets: List<Set>

    init {
        val splitted = line.split(':')
        this.id = splitted.first().split(' ').last().toInt()
        this.sets = splitted.last().split(';').flatMap { set ->
            set.trim().split(',').map { cube ->
                val splittedCube = cube.trim().split(' ')
                Pair(splittedCube.first().toInt(), Cube.entries.first { it.color == splittedCube.last() })
            }
        }
    }

    fun isValid(): Boolean = this.sets.all {
        when (it.second) {
            Cube.RED -> it.first <= 12
            Cube.GREEN -> it.first <= 13
            Cube.BLUE -> it.first <= 14
        }
    }

    fun getRequiredSets(): List<Set> = this.sets.groupBy { it.second }.map { it.value.maxBy { it.first } }

}

fun main() {

    fun part1(input: List<String>): Int {
        return input.map { Game(it) }.filter { it.isValid() }.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        return input.map { Game(it).getRequiredSets().fold(1) { acc, item -> acc * item.first } }.sum()
    }

    val testInput1 = readInput("day02/Day02_test")
    check(part1(testInput1) == 8)

    val testInput2 = readInput("day02/Day02_test")
    check(part2(testInput2) == 2286)

    val input = readInput("day02/Day02")
    part1(input).println() // 2278
    part2(input).println() // 67953
}