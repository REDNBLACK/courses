package day6

import parseInput
import array2d

/**
--- Day 6: Signals and Noise ---

Something is jamming your communications with Santa. Fortunately, your signal is only partially jammed, and protocol in situations like this is to switch to a simple repetition code to get the message through.

In this model, the same message is sent repeatedly. You've recorded the repeating message signal (your puzzle input), but the data seems quite corrupted - almost too badly to recover. Almost.

All you need to do is figure out which character is most frequent for each position. For example, suppose you had recorded the following messages:

eedadn
drvtee
eandsr
raavrd
atevrs
tsrnev
sdttsa
rasrtv
nssdts
ntnada
svetve
tesnvt
vntsnd
vrdear
dvrsen
enarar
The most common character in the first column is e; in the second, a; in the third, s, and so on. Combining these characters returns the error-corrected message, easter.

Given the recording in your puzzle input, what is the error-corrected version of the message being sent?

 */

fun main(args: Array<String>) {
    val test = """
eedadn
drvtee
eandsr
raavrd
atevrs
tsrnev
sdttsa
rasrtv
nssdts
ntnada
svetve
tesnvt
vntsnd
vrdear
dvrsen
enarar"""

    println(findMostFrequentChars(test).joinToString("") == "easter")

    val input = parseInput("day6-input.txt")

    println(findMostFrequentChars(input).joinToString(""))
}

data class Message(val payload: String) {
    fun mostFrequentChar(): Char? {
        return payload.toCharArray()
                .groupBy { it }
                .mapValues { it.value.count() }
                .maxBy { it.value }
                ?.key
    }
}

fun findMostFrequentChars(input: String): List<Char?> {
    return parseMessages(input).map { it.mostFrequentChar() }
}

fun parseMessages(input: String): List<Message> {
    val lines = input.split("\n")
            .map(String::trim)
            .filter(String::isNotEmpty)
            .map { it.toCharArray().toList() }

    val result = array2d(lines.first().size, lines.size) { '0' }
    for ((x, line) in lines.withIndex()) {
        for ((y, char) in line.withIndex()) {
            result[y][x] = char
        }
    }

    return result.map { it.joinToString("") }.map(::Message)
}
