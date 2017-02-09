package day03

import chunk
import parseInput
import splitToLines

/**
--- Day 3: Squares With Three Sides ---

--- Part Two ---

Now that you've helpfully marked up their design documents, it occurs to you that triangles are specified in groups of three vertically. Each set of three numbers in a column specifies a triangle. Rows are unrelated.

For example, given the following specification, numbers with the same hundreds digit would be part of the same triangle:

101 301 501
102 302 502
103 303 503
201 401 601
202 402 602
203 403 603
In your puzzle input, and instead reading by columns, how many of the listed triangles are possible?

 */

fun main(args: Array<String>) {
    val test = """101 301 501
                 |102 302 502
                 |103 303 503
                 |201 401 601
                 |202 402 602
                 |203 403 603""".trimMargin()
    val input = parseInput("day3-input.txt")

    println(findPossibleVertical(test).size == 6)
    println(findPossibleVertical(input).size)
}

fun findPossibleVertical(input: String) = parseTrianglesVertical(input).filter { it.isPossible() }

private fun parseTrianglesVertical(input: String): List<Triangle> {
    val lines = input.splitToLines()
            .map {
                it.splitToLines(" ").map(String::toInt)
            }

    return lines.map { it[0] }
            .plus(lines.map { it[1] })
            .plus(lines.map { it[2] })
            .chunk(3)
            .map {
                val (x, y, z) = it

                Triangle(x, y, z)
            }
}
