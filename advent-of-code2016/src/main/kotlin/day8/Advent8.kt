package day8

import array2d
import day8.Operation.Type.*
import parseInput

/**
You come across a door implementing what you can only assume is an implementation of two-factor authentication after a long game of requirements telephone.

To get past the door, you first swipe a keycard (no problem; there was one on a nearby desk). Then, it displays a code on a little screen, and you type that code on a keypad. Then, presumably, the door unlocks.

Unfortunately, the screen has been smashed. After a few minutes, you've taken everything apart and figured out how it works. Now you just have to work out what the screen would have displayed.

The magnetic strip on the card you swiped encodes a series of instructions for the screen; these instructions are your puzzle input. The screen is 50 pixels wide and 6 pixels tall, all of which start off, and is capable of three somewhat peculiar operations:

rect AxB turns on all of the pixels in a rectangle at the top-left of the screen which is A wide and B tall.
rotate row y=A by B shifts all of the pixels in row A (0 is the top row) right by B pixels. Pixels that would fall off the right end appear at the left end of the row.
rotate column x=A by B shifts all of the pixels in column A (0 is the left column) down by B pixels. Pixels that would fall off the bottom appear at the top of the column.
For example, here is a simple sequence on a smaller screen:

rect 3x2 creates a small rectangle in the top-left corner:

###....
###....
.......
rotate column x=1 by 1 rotates the second column down by one pixel:

#.#....
###....
.#.....
rotate row y=0 by 4 rotates the top row right by four pixels:

....#.#
###....
.#.....
rotate column x=1 by 1 again rotates the second column down by one pixel, causing the bottom pixel to wrap back to the top:

.#..#.#
#.#....
.#.....
As you can see, this display technology is extremely powerful, and will soon dominate the tiny-code-displaying-screen market. That's what the advertisement on the back of the display tries to convince you, anyway.

There seems to be an intermediate check of the voltage used by the display: after you swipe your card, if the screen did work, how many pixels should be lit?

 */

fun main(args: Array<String>) {
    val test = """rect 3x2
                 |rotate column x=1 by 1
                 |rotate row y=0 by 4
                 |rotate column x=1 by 1""".trimMargin()
    val resultMatrix1 = execute(test, array2d(3, 7) { false })
    resultMatrix1.drawMatrix()
    println(resultMatrix1.countFilled() == 6)

    val input = parseInput("day8-input.txt")
    val resultMatrix2 = execute(input, array2d(6, 50) { false })
    resultMatrix2.drawMatrix()
    println(resultMatrix2.countFilled())
}

data class Operation(val type: Operation.Type, val payload1: Int, val payload2: Int) {
    enum class Type { CREATE, ROTATE_ROW, ROTATE_COLUMN }
}

fun execute(input: String, matrix: Array<Array<Boolean>>): Array<Array<Boolean>> {
    val operations = parseOperations(input)

    for ((type, p1, p2) in operations) {
        when (type) {
            CREATE -> {
                for (w in 0 until p1) {
                    for (h in 0 until p2) {
                        matrix[h][w] = true
                    }
                }
            }
            ROTATE_COLUMN -> {
                repeat(p2, { matrix.shiftDown(p1) })
            }
            ROTATE_ROW -> {
                repeat(p2, { matrix.shiftRight(p1) })
            }
        }
    }

    return matrix
}

private fun Array<Array<Boolean>>.drawMatrix() {
    println(map { it.map { if (it) '█' else '▒' }.joinToString("") }.joinToString(System.lineSeparator()))
}

private fun Array<Array<Boolean>>.countFilled() = sumBy { it.sumBy { if (it) 1 else 0 } }

private fun <T> Array<Array<T>>.shiftDown(i: Int) {
    val size = this.size - 1
    val temp = this[size][i]

    for (k in size downTo 1) {
        this[k][i] = this[k - 1][i]
    }

    this[0][1] = temp
}

private fun <T> Array<Array<T>>.shiftRight(i: Int) {
    val size = this[i].size - 1
    val temp = this[i][size]

    for (k in size downTo 1) {
        this[i][k] = this[i][k - 1]
    }

    this[i][0] = temp
}

private fun parseOperations(input: String) = input.split("\n")
        .map(String::trim)
        .filter(String::isNotEmpty)
        .map {
            val (payload1, payload2) = Regex("""(\d+)""")
                    .findAll(it)
                    .map { it.value }
                    .map(String::toInt)
                    .toList()

            val type = when {
                "rect" in it -> CREATE
                "rotate row" in it -> ROTATE_ROW
                "rotate column" in it -> ROTATE_COLUMN
                else -> throw IllegalArgumentException()
            }

            Operation(type = type, payload1 = payload1, payload2 = payload2)
        }
