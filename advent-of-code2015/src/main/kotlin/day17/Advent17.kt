package day17

import combinations
import parseInput

/**
--- Day 17: No Such Thing as Too Much ---

The elves bought too much eggnog again - 150 liters this time. To fit it all into your refrigerator, you'll need to move it into smaller containers. You take an inventory of the capacities of the available containers.

For example, suppose you have containers of size 20, 15, 10, 5, and 5 liters. If you need to store 25 liters, there are four ways to do it:

15 and 10
20 and 5 (the first 5)
20 and 5 (the second 5)
15, 5, and 5
Filling all containers entirely, how many different combinations of containers can exactly fit all 150 liters of eggnog?

--- Part Two ---

While playing with all the containers in the kitchen, another load of eggnog arrives! The shipping and receiving department is requesting as many containers as you can spare.

Find the minimum number of containers that can exactly fit all 150 liters of eggnog. How many different ways can you fill that number of containers and still hold exactly 150 litres?

In the example above, the minimum number of containers was two. There were three ways to use that many containers, and so the answer there would be 3.

 */

fun main(args: Array<String>) {
    val test = """
               |5
               |5
               |10
               |15
               |20
               """.trimMargin()
    val input = parseInput("day17-input.txt")

    println(findCombinations(test, 25))
    println(findCombinations(input, 150))
}

fun findCombinations(input: String, liters: Int): Map<String, Int?> {
    val containers = parseContainers(input)

    val ways = (1..containers.size)
            .map { i ->
                containers.combinations(i)
                        .filter { it.sum() == liters }
                        .count()
            }
            .filter { it != 0 }

    return mapOf("total" to ways.sum(), "minimal" to ways.dropLast(1).min())
}

private fun parseContainers(input: String) = input
        .split("\n")
        .map(String::trim)
        .filter(String::isNotEmpty)
        .map(String::toInt)
