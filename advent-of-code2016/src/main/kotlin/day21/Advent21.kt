package day21

import day21.Operation.Type.*
import move
import parseInput
import reverseSubstring
import rotate
import splitToLines
import swap

/**
--- Day 21: Scrambled Letters and Hash ---

The computer system you're breaking into uses a weird scrambling function to store its passwords. It shouldn't be much trouble to create your own scrambled password so you can add it to the system; you just have to implement the scrambler.

The scrambling function is a series of operations (the exact list is provided in your puzzle input). Starting with the password to be scrambled, apply each operation in succession to the string. The individual operations behave as follows:

swap position X with position Y means that the letters at indexes X and Y (counting from 0) should be swapped.
swap letter X with letter Y means that the letters X and Y should be swapped (regardless of where they appear in the string).
rotate left/right X steps means that the whole string should be rotated; for example, one right rotation would turn abcd into dabc.
rotate based on position of letter X means that the whole string should be rotated to the right based on the index of letter X (counting from 0) as determined before this instruction does any rotations. Once the index is determined, rotate the string to the right one time, plus a number of times equal to that index, plus one additional time if the index was at least 4.
reverse positions X through Y means that the span of letters at indexes X through Y (including the letters at X and Y) should be reversed in order.
move position X to position Y means that the letter which is at index X should be removed from the string, then inserted such that it ends up at index Y.
For example, suppose you start with abcde and perform the following operations:

swap position 4 with position 0 swaps the first and last letters, producing the input for the next step, ebcda.
swap letter d with letter b swaps the positions of d and b: edcba.
reverse positions 0 through 4 causes the entire string to be reversed, producing abcde.
rotate left 1 step shifts all letters left one position, causing the first letter to wrap to the end of the string: bcdea.
move position 1 to position 4 removes the letter at position 1 (c), then inserts it at position 4 (the end of the string): bdeac.
move position 3 to position 0 removes the letter at position 3 (a), then inserts it at position 0 (the front of the string): abdec.
rotate based on position of letter b finds the index of letter b (1), then rotates the string right once plus a number of times equal to that index (2): ecabd.
rotate based on position of letter d finds the index of letter d (4), then rotates the string right once, plus a number of times equal to that index, plus an additional time because the index was at least 4, for a total of 6 right rotations: decab.
After these steps, the resulting scrambled password is decab.

Now, you just need to generate a new scrambled password and you can access the system. Given the list of scrambling operations in your puzzle input, what is the result of scrambling abcdefgh?

--- Part Two ---

You scrambled the password correctly, but you discover that you can't actually modify the password file on the system. You'll need to un-scramble one of the existing passwords by reversing the scrambling process.

What is the un-scrambled version of the scrambled password fbgdceah?

 */

fun main(args: Array<String>) {
    val test = """|swap position 4 with position 0
                  |swap letter d with letter b
                  |reverse positions 0 through 4
                  |rotate left 1 step
                  |move position 1 to position 4
                  |move position 3 to position 0
                  |rotate based on position of letter b
                  |rotate based on position of letter d
                  |""".trimMargin()
    val input = parseInput("day21-input.txt")

    println(scramble("abcde", test) == "decab")
    println(scramble("abcdefgh", input))
    println(scramble("fbgdceah", input, true))
}

fun scramble(string: String, operationsInput: String, reversed: Boolean = false) = parseOperations(operationsInput)
        .let { if (reversed) it.reversed() else it }
        .fold(string, { s, o ->
            when (o.type) {
                SWAP_POS -> {
                    val (from, to) = o.argsAsInt()
                    s.swap(from, to)
                }
                SWAP_LETTER -> {
                    val (from, to) = o.argsAsString().map { s.indexOf(it) }
                    s.swap(from, to)
                }
                ROTATE -> {
                    val times = o.argsAsInt().first()
                    s.rotate(times * (if (reversed) -1 else 1))
                }
                ROTATE_BASED -> {
                    val i = o.argsAsString().first().let { s.indexOf(it) }
                    val times = when (reversed) {
                        true -> i / 2 + (if (i % 2 == 1 || i == 0) 1 else 5)
                        false -> -(1 + i + (if (i >= 4) 1 else 0))
                    }
                    s.rotate(times)
                }
                REVERSE -> {
                    val (from, to) = o.argsAsInt()
                    s.reverseSubstring(from, to)
                }
                MOVE -> {
                    val (from, to) = o.argsAsInt().let { if (reversed) it.reversed() else it }
                    s.move(from, to)
                }
            }
        })

data class Operation(val type: Type, val args: List<Any>) {
    enum class Type { SWAP_POS, SWAP_LETTER, ROTATE, ROTATE_BASED, REVERSE, MOVE }

    fun argsAsInt() = args.map { it as Int }
    fun argsAsString() = args.map { it as String }
}

private fun parseOperations(input: String) = input.splitToLines()
        .map {
            fun String.indexes() = Regex("""(\d+)""").findAll(this)
                    .map { it.groupValues.drop(1) }
                    .toList()
                    .flatMap { it.map(String::toInt) }

            fun String.letters() = split(" ").filter { it.length == 1 }

            when {
                it.startsWith("swap position") -> Operation(SWAP_POS, it.indexes())
                it.startsWith("swap letter") -> Operation(SWAP_LETTER, it.letters())
                it.startsWith("reverse") -> Operation(REVERSE, it.indexes())
                it.startsWith("rotate based") -> Operation(ROTATE_BASED, it.letters())
                it.startsWith("rotate") -> Operation(
                        ROTATE,
                        Regex("""rotate (left|right) (\d+) step""")
                        .findAll(it)
                        .map { it.groupValues.drop(1) }
                        .toList()
                        .flatMap { it }
                        .let { listOf(if (it.first() == "left") it.last().toInt() else -it.last().toInt()) }
                )
                it.startsWith("move") -> Operation(MOVE, it.indexes())
                else -> throw IllegalArgumentException(it)
            }
        }
