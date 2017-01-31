package day12

import parseInput

/**
--- Day 12: JSAbacusFramework.io ---

Santa's Accounting-Elves need help balancing the books after a recent order. Unfortunately, their accounting software uses a peculiar storage format. That's where you come in.

They have a JSON document which contains a variety of things: arrays ([1,2,3]), objects ({"a":1, "b":2}), numbers, and strings. Your first job is to simply find all of the numbers throughout the document and add them together.

For example:

[1,2,3] and {"a":2,"b":4} both have a sum of 6.
[[[3]]] and {"a":{"b":4},"c":-1} both have a sum of 3.
{"a":[-1,1]} and [-1,{"a":1}] both have a sum of 0.
[] and {} both have a sum of 0.
You will not encounter any strings containing numbers.

What is the sum of all numbers in the document?

 */

fun main(args: Array<String>) {
    println(calculateSum("[1,2,3]") == 6)
    println(calculateSum("""{"a":2,"b":4}""") == 6)
    println(calculateSum("[[[3]]]") == 3)
    println(calculateSum("""{"a":{"b":4},"c":-1}""") == 3)
    println(calculateSum("""{"a":[-1,1]}""") == 0)
    println(calculateSum("""[-1,{"a":1}]""") == 0)
    println(calculateSum("""[]""") == 0)
    println(calculateSum("""{}""") == 0)

    val input = parseInput("day12-input.txt")
    println(calculateSum(input))
}

fun calculateSum(input: String) = input.split("\n")
        .map(String::trim)
        .filter(String::isNotEmpty)
        .sumBy {
            Regex("""(-?\d+)""").findAll(it).map { it.groupValues[1] }.map(String::toInt).sum()
        }

