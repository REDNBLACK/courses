package day9

import parseInput
import permutations
import splitToLines

/**
--- Day 9: All in a Single Night ---

Every year, Santa manages to deliver all of his presents in a single night.

This year, however, he has some new locations to visit; his elves have provided him the distances between every pair of locations. He can start and end at any two (different) locations he wants, but he must visit each location exactly once. What is the shortest distance he can travel to achieve this?

For example, given the following distances:

London to Dublin = 464
London to Belfast = 518
Dublin to Belfast = 141
The possible routes are therefore:

Dublin -> London -> Belfast = 982
London -> Dublin -> Belfast = 605
London -> Belfast -> Dublin = 659
Dublin -> Belfast -> London = 659
Belfast -> Dublin -> London = 605
Belfast -> London -> Dublin = 982
The shortest of these is London -> Dublin -> Belfast = 605, and so the answer is 605 in this example.

What is the distance of the shortest route?

--- Part Two ---

The next year, just to show off, Santa decides to take the route with the longest distance instead.

He can still start and end at any two (different) locations he wants, and he still must visit each location exactly once.

For example, given the distances above, the longest route would be 982 via (for example) Dublin -> London -> Belfast.

What is the distance of the longest route?

 */

fun main(args: Array<String>) {
    val test = """
               |London to Dublin = 464
               |London to Belfast = 518
               |Dublin to Belfast = 141
               """.trimMargin()

    println(findDistance(test) == mapOf("first" to 605, "second" to 982))

    val input = parseInput("day9-input.txt")
    println(findDistance(input))
}

fun findDistance(input: String): Map<String, Int?> {
    val travels = parseTravels(input)

    return travels
            .flatMap(Travel::toList)
            .distinct()
            .permutations()
            .fold(setOf<Int>(), { distances, permute ->
                distances + permute.zip(permute.subList(1, permute.size))
                        .map { p -> travels.find { it.from == p.first && it.to == p.second } }
                        .filterNotNull()
                        .sumBy { it.distance }
            })
            .let { mapOf("first" to it.min(), "second" to it.max()) }
}

data class Travel(val from: String, val to: String, val distance: Int) {
    fun toList() = listOf(from, to)
}

private fun parseTravels(input: String) = input.splitToLines()
        .map {
            val (from, _s1, to, _s2, distance) = it.split(" ")

            Travel(from = from, to = to, distance = distance.toInt())
        }
        .flatMap { listOf(it, it.copy(from = it.to, to = it.from)) }
