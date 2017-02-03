package day3

import parseInput

/**
--- Day 3: Perfectly Spherical Houses in a Vacuum ---

Santa is delivering presents to an infinite two-dimensional grid of houses.

He begins by delivering a present to the house at his starting location, and then an elf at the North Pole calls him via radio and tells him where to move next. Moves are always exactly one house to the north (^), south (v), east (>), or west (<). After each move, he delivers another present to the house at his new location.

However, the elf back at the north pole has had a little too much eggnog, and so his directions are a little off, and Santa ends up visiting some houses more than once. How many houses receive at least one present?

For example:

> delivers presents to 2 houses: one at the starting location, and one to the east.
^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.

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
    println(countHouses(">") == 2)
    println(countHouses("^>v<") == 4)
    println(countHouses("^v^v^v^v^v") == 2)
    println(countHousesSecond("^v") == 3)
    println(countHousesSecond("^>v<") == 3)
    println(countHousesSecond("^v^v^v^v^v") == 11)

    val input = parseInput("day3-input.txt")
    println(countHouses(input))
    println(countHousesSecond(input))
}

fun countHouses(input: String) = executeMoves(moves = parseMoves(input)).size

fun countHousesSecond(input: String) = parseMovesSecond(input)
        .flatMap { executeMoves(moves = it) }
        .distinct()
        .count()

private tailrec fun executeMoves(pos: Int = 0, acc: List<State> = listOf(State(0, 0)), moves: List<Move>): List<State> {
    if (pos == moves.size) return acc.distinct()

    val m = moves[pos]
    val s = acc.last()
    return executeMoves(pos + 1, acc.plus(s.copy(x = s.x + m.x, y = s.y + m.y)), moves)
}

data class State(val x: Int, val y: Int)

enum class Move(val x: Int, val y: Int) {
    EAST(0, 1),
    WEST(0, -1),
    SOUTH(-1, 0),
    NORTH(1, 0)
}

private fun parseMoves(input: String) = input.trim()
        .map {
            when (it) {
                '>' -> Move.EAST
                '<' -> Move.WEST
                '^' -> Move.NORTH
                'v' -> Move.SOUTH
                else -> throw IllegalArgumentException()
            }
        }

private fun parseMovesSecond(input: String): List<List<Move>> {
    fun parse(indexCompare: (Int) -> Boolean) = input.toCharArray()
            .filterIndexed { i, c -> indexCompare(i) }
            .joinToString("")
            .let(::parseMoves)

    return listOf(parse { it % 2 == 0 }, parse { it % 2 != 0 })
}
