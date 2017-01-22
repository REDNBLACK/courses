package day1

import parseInput

/**
--- Day 1: Not Quite Lisp ---

Santa was hoping for a white Christmas, but his weather machine's "snow" function is powered by stars, and he's fresh out! To save Christmas, he needs you to collect fifty stars by December 25th.

Collect stars by helping Santa solve puzzles. Two puzzles will be made available on each day in the advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!

Here's an easy puzzle to warm you up.

Santa is trying to deliver presents in a large apartment building, but he can't find the right floor - the directions he got are a little confusing. He starts on the ground floor (floor 0) and then follows the instructions one character at a time.

An opening parenthesis, (, means he should go up one floor, and a closing parenthesis, ), means he should go down one floor.

The apartment building is very tall, and the basement is very deep; he will never find the top or bottom floors.

For example:

(()) and ()() both result in floor 0.
((( and (()(()( both result in floor 3.
))((((( also results in floor 3.
()) and ))( both result in floor -1 (the first basement level).
))) and )())()) both result in floor -3.
To what floor do the instructions take Santa?

Your puzzle answer was 138.

The first half of this puzzle is complete! It provides one gold star: *

--- Part Two ---

Now, given the same instructions, find the position of the first character that causes him to enter the basement (floor -1). The first character in the instructions has position 1, the second character has position 2, and so on.

For example:

) causes him to enter the basement at character position 1.
()()) causes him to enter the basement at character position 5.
What is the position of the character that causes Santa to first enter the basement?

 */

fun main(args: Array<String>) {
    val test = hashMapOf(
            "(())" to 0,
            "()()" to 0,
            "(((" to 3,
            "(()(()(" to 3,
            "())" to -1,
            "))(" to -1,
            ")))" to -3,
            ")())())" to -3
    )

    println(test.filter { findFloor(it.key) == it.value }.count() == test.size)
    println(findFloor(parseInput("day1-input.txt")))
}

fun findFloor(input: String): Int {
    val tokens = hashMapOf('(' to 1, ')' to -1)
    tailrec fun loop(pos: Int, counter: Int): Int {
        if (pos == input.length) return counter

        return loop(pos + 1, counter + tokens.getOrDefault(input[pos], 0))
    }

    return loop(0, 0)
}
