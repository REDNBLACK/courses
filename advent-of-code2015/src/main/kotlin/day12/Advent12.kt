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

--- Part Two ---

Uh oh - the Accounting-Elves have realized that they double-counted everything red.

Ignore any object (and all of its children) which has any property with the value "red". Do this only for objects ({...}), not arrays ([...]).

[1,2,3] still has a sum of 6.
[1,{"c":"red","b":2},3] now has a sum of 4, because the middle object is ignored.
{"d":"red","e":[1,2,3,4],"f":5} now has a sum of 0, because the entire structure is ignored.
[1,"red",5] has a sum of 6, because "red" in an array has no effect.

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
    println(calculateSumWithoutRed(input))
}

fun calculateSum(input: String) = Regex("""(-?\d+)""")
        .findAll(input)
        .map { it.groupValues[1] }
        .map(String::toInt)
        .sum()

fun calculateSumWithoutRed(input: String): Int {
    val data = StringBuilder(input)

    fun StringBuilder.findJsonDictBound(startPos: Int, endPos: Int): Int {
        var counter = 0
        var index = startPos
        val map = mapOf('{' to 1, '}' to -1)

        while (counter != endPos) {
            index -= endPos
            counter += map[this[index]] ?: 0
        }

        return index
    }

    while (true) {
        val pos = data.indexOf(""":"red"""")
        if (pos == -1) break
        val start = data.findJsonDictBound(pos, 1)
        val end = data.findJsonDictBound(pos, -1)

        with (data) {
            val add = StringBuilder(subSequence(0, start))
                    .append('0')
                    .append(subSequence(end + 1, data.length))
            setLength(0)
            append(add)
        }
    }

    return calculateSum(data.toString())
}

