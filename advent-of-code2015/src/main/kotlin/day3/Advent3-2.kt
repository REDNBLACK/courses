package day3

import parseInput

/**
--- Part Two ---

The next year, to speed up the process, Santa creates a robot version of himself, Robo-Santa, to deliver presents with him.

Santa and Robo-Santa start at the same location (delivering two presents to the same starting house), then take turns moving based on instructions from the elf, who is eggnoggedly reading from the same script as the previous year.

This year, how many houses receive at least one present?

For example:

^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
^>v< now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.
 */

fun main(args: Array<String>) {
    println(countHousesSecond("^v") == 3)
    println(countHousesSecond("^>v<") == 3)
    println(countHousesSecond("^v^v^v^v^v") == 11)

    val input = parseInput("day3-input.txt")
    println(countHousesSecond(input))
}

fun countHousesSecond(input: String) = parseMovesSecond(input)
        .flatMap { executeMoves(moves = it) }
        .distinct()
        .count()

private fun parseMovesSecond(input: String): List<List<Move>> {
    fun parse(indexCompare: (Int) -> Boolean) = input.toCharArray()
            .filterIndexed { i, c -> indexCompare(i) }
            .joinToString("")
            .let(::parseMoves)

    return listOf(parse { it % 2 == 0 }, parse { it % 2 != 0 })
}
