package day01

import day01.State.Direction.*
import parseInput
import splitToLines

/**
 * --- Day 1: No Time for a Taxicab ---

Santa's sleigh uses a very high-precision clock to guide its movements, and the clock's oscillator is regulated by stars. Unfortunately, the stars have been stolen... by the Easter Bunny. To save Christmas, Santa needs you to retrieve all fifty stars by December 25th.

Collect stars by solving puzzles. Two puzzles will be made available on each day in the advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!

You're airdropped near Easter Bunny Headquarters in a city somewhere. "Near", unfortunately, is as close as you can get - the instructions on the Easter Bunny Recruiting Document the Elves intercepted start here, and nobody had time to work them out further.

The Document indicates that you should start at the given coordinates (where you just landed) and face North. Then, follow the provided sequence: either value left (L) or right (R) 90 degrees, then walk forward the given number of blocks, ending at a new intersection.

There's no time to follow such ridiculous instructions on foot, though, so you take a moment and work out the destination. Given that you can only walk on the street grid of the city, how far is the shortest path to the destination?

For example:

Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
R5, L5, R5, R3 leaves you 12 blocks away.
How many blocks away is Easter Bunny HQ?

--- Part Two ---

Then, you notice the instructions continue on the back of the Recruiting Document. Easter Bunny HQ is actually at the first location you visit twice.

For example, if your instructions are R8, R4, R4, R8, the first location you visit twice is 4 blocks away, due East.

How many blocks away is the first location you visit twice?

 */

fun main(args: Array<String>) {
    println(findDestination("R2, L3").countBlocks() == 5)
    println(findDestination("R2, R2, R2").countBlocks() == 2)
    println(findDestination("R5, L5, R5, R3").countBlocks() == 12)
    println(findDestination("R8, R4, R4, R8").countBlocks())

    val input = parseInput("day1-input.txt")
    println(findDestination(input).countBlocks())
    println(findDestination(input).countBlocks())
}

data class State(val direction: Direction, val x: Int, val y: Int) {
    enum class Direction(val value: Int) {
        NORTH(0), EAST(1), SOUTH(2), WEST(3);
    }

    fun countBlocks() = Math.abs(x) + Math.abs(y)
}
data class Move(val turn: Turn, val steps: Int) {
    enum class Turn(val value: Int) { R(1), L(-1) }
}

fun findDestination(input: String): State {
    return parseMoves(input).fold(State(NORTH, 0, 0)) { state, move ->
        val (turn, steps) = move
        val direction = State.Direction.values()[(state.direction.value + turn.value + 4) % 4]
        when (direction) {
            NORTH -> state.copy(direction, x = state.x - steps)
            SOUTH -> state.copy(direction, x = state.x + steps)
            EAST -> state.copy(direction, y = state.y + steps)
            WEST -> state.copy(direction, y = state.y - steps)
        }
    }
}

private fun parseMoves(input: String) = input.splitToLines(",")
        .map { it ->
            val turn = Move.Turn.valueOf(it[0].toString())
            val steps = it.drop(1).toInt()

            Move(turn, steps)
        }
