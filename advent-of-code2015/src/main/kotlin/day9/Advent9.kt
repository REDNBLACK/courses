package day9

import array2d
import day6.Operation.Type.*
import parseInput

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

 */

fun main(args: Array<String>) {
    val test = """
            |London to Dublin = 464
            |London to Belfast = 518
            |Dublin to Belfast = 141
            """.trimMargin()

    println(findShortestDistance(test))

    val input = parseInput("day9-input.txt")
    println(findShortestDistance(input))
}

fun findShortestDistance(input: String): Chain? {
    val travels = parseTravels(input)

    return travels
            .fold(listOf<Chain>(), { chains, travel ->
                tailrec fun loop(chain: Chain): Chain {
                    val found = travels.find { travel.to == it.from && it.to !in chain.cities } ?: return chain

                    return loop(chain.copy(chain.cities + found.to, chain.totalDistance + found.distance))
                }

                chains.plus(loop(Chain(listOf(travel.from, travel.to), travel.distance.toLong())))
            })
            .minBy { it.totalDistance }
}


data class Chain(val cities: List<String>, val totalDistance: Long)
data class Travel(val from: String, val to: String, val distance: Int)

private fun parseTravels(input: String) = input
        .split("\n")
        .map(String::trim)
        .filter(String::isNotEmpty)
        .map {
            val (from, _s1, to, _s2, distance) = it.split(" ")

            Travel(from = from, to = to, distance = distance.toInt())
        }
        .flatMap { listOf(it, it.copy(from = it.to, to = it.from)) }
