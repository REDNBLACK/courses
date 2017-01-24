package day10

import parseInput

/**
--- Day 10: Elves Look, Elves Say ---

Today, the Elves are playing a game called look-and-say. They take turns making sequences by reading aloud the previous sequence and using that reading as the next sequence. For example, 211 is read as "one two, two ones", which becomes 1221 (1 2, 2 1s).

Look-and-say sequences are generated iteratively, using the previous value as input for the next step. For each step, take the previous value, and replace each run of digits (like 111) with the number of digits (3) followed by the digit itself (1).

For example:

1 becomes 11 (1 copy of digit 1).
11 becomes 21 (2 copies of digit 1).
21 becomes 1211 (one 2 followed by one 1).
1211 becomes 111221 (one 1, one 2, and two 1s).
111221 becomes 312211 (three 1s, two 2s, and one 1).
Starting with the digits in your puzzle input, apply this process 40 times. What is the length of the result?

 */

fun main(args: Array<String>) {
    println("1".transform() == "11")
    println("11".transform() == "21")
    println("21".transform() == "1211")
    println("1211".transform() == "111221")
    println("111221".transform() == "312211")

    println("1113222113".transform(40).length)
    println("1113222113".transform(50).length)
}

fun String.transform(times: Int = 1) = (1..times).fold(this, { str, _i -> println(_i); str.transform() })

private fun String.transform(): String {
    tailrec fun loop(curChar: Char, pos: Int, acc: List<List<Char>> = listOf(listOf())): String {
        if (pos == this.length) return acc.map { "${it.size}${it.last()}" }.joinToString("")
        if (this[pos] != curChar) return loop(this[pos], pos + 1, acc.plusElement(listOf(this[pos])))

        return loop(curChar, pos + 1, acc.subList(0, acc.size - 1).plusElement(acc.last().plusElement(this[pos])))
    }

    return loop(this[0], 0)
}
