package day06

import array2d
import parseInput
import splitToLines

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

--- Part Two ---

Of course, that would be the message - if you hadn't agreed to use a modified repetition code instead.

In this modified code, the sender instead transmits what looks like random data, but for each character, the character they actually want to send is slightly less likely than the others. Even after signal-jamming noise, you can look at the letter distributions in each column and choose the least common letter to reconstruct the original message.

In the above example, the least common character in the first column is a; in the second, d, and so on. Repeating this process for the remaining characters produces the original message, advent.

Given the recording in your puzzle input and this new decoding methodology, what is the original message that Santa is trying to send?

 */

fun main(args: Array<String>) {
    val test = """eedadn
                |drvtee
                |eandsr
                |raavrd
                |atevrs
                |tsrnev
                |sdttsa
                |rasrtv
                |nssdts
                |ntnada
                |svetve
                |tesnvt
                |vntsnd
                |vrdear
                |dvrsen
                |enarar""".trimMargin()
    val input = parseInput("day6-input.txt")

    println(findMostFrequentChars(test) == "easter")
    println(findMostFrequentChars(input))
    println(findLeastFrequentChars(input))
}

data class Message(val payload: String) {
    fun mostFrequentChar() = payload.charByFunc { it.maxBy { it.value } }
    fun leastFrequentChar() = payload.charByFunc { it.minBy { it.value } }

    private fun String.charByFunc(func: (Map<Char, Int>) -> Map.Entry<Char, Int>?): Char? {
        val chars = toCharArray().groupBy { it }.mapValues { it.value.count() }

        return func(chars)?.key
    }
}

fun findMostFrequentChars(input: String) = parseMessages(input).map { it.mostFrequentChar() }.joinToString("")

fun findLeastFrequentChars(input: String) = parseMessages(input).map { it.leastFrequentChar() }.joinToString("")

private fun parseMessages(input: String): List<Message> {
    val lines = input.splitToLines().map(String::toList)

    val rotate = fun (): Array<Array<Char>> {
        val result = array2d(lines.first().size, lines.size) { '0' }
        for ((x, line) in lines.withIndex()) {
            for ((y, char) in line.withIndex()) {
                result[y][x] = char
            }
        }

        return result
    }

    return rotate().map { it.joinToString("") }.map(::Message)
}
