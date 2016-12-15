package day1

import day1.Direction.*
import day1.Turn.LEFT
import day1.Turn.RIGHT
import java.io.File
import java.nio.file.Files

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

 */

fun main(args: Array<String>) {
    println(findDestination("R2, L3").countBlocks() == 5)
    println(findDestination("R2, R2, R2").countBlocks() == 2)
    println(findDestination("R5, L5, R5, R3").countBlocks() == 12)

    val input = Thread.currentThread()
            .contextClassLoader
            .getResourceAsStream("day1-input.txt")
            .bufferedReader()
            .use { it.readText() }

    println(findDestination(input).countBlocks())
}

enum class Turn {
    RIGHT, LEFT;

    companion object {
        fun fromString(str: String) = when (str) {
            "L" -> LEFT
            "R" -> RIGHT
            else -> throw RuntimeException("No such direction!")
        }
    }
}
enum class Direction {
    NORTH, EAST, SOUTH, WEST;
}

data class State(val direction: Direction, val x: Int, val y: Int) {
    fun countBlocks() = x + y
}
data class Move(val turn: Turn, val steps: Int)

fun findDestination(input: String): State {
    return parseMoves(input).fold(State(NORTH, 0, 0)) { state, move ->
        when (move.turn) {
            LEFT -> when (state.direction) {
                NORTH -> state.copy(direction = WEST, x = state.x - move.steps)
                SOUTH -> state.copy(direction = EAST, x = state.x + move.steps)
                EAST -> state.copy(direction = NORTH, y = state.y + move.steps)
                WEST -> state.copy(direction = SOUTH, y = state.y - move.steps)
            }
            RIGHT -> when (state.direction) {
                NORTH -> state.copy(direction = EAST, x = state.x + move.steps)
                SOUTH -> state.copy(direction = WEST, x = state.x + move.steps)
                EAST -> state.copy(direction = SOUTH, y = state.y - move.steps)
                WEST -> state.copy(direction = NORTH, y = state.y + move.steps)
            }
        }
    }
}

fun parseMoves(input: String): List<Move> {
    return input.split(",")
            .map(String::trim)
            .map { it ->
                val turn = Turn.fromString(it[0].toString())
                val steps = it[1].toString().toInt()

                Move(turn, steps)
            }
}
