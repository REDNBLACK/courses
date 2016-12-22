package day3

import chunk
import parseInput

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
    println(findPossibleVertical("""101 301 501
102 302 502
103 303 503
201 401 601
202 402 602
203 403 603""").size)

    val input = parseInput("day3-input.txt")

    println(findPossibleVertical(input).size)
}

fun findPossibleVertical(input: String) = parseTrianglesVertical(input).filter { it.isPossible() }

fun parseTrianglesVertical(input: String): List<Triangle> {
    val lines = input.split("\n")
            .map(String::trim)
            .filter(String::isNotEmpty)
            .map {
                it.split(" ").map(String::trim).filter(String::isNotEmpty).map(String::toInt)
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
