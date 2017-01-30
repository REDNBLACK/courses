package day13

import parseInput
import permutations

/**
--- Day 13: Knights of the Dinner Table ---

In years past, the holiday feast with your family hasn't gone so well. Not everyone gets along! This year, you resolve, will be different. You're going to find the optimal seating arrangement and avoid all those awkward conversations.

You start by writing up a list of everyone invited and the amount their happiness would increase or decrease if they were to find themselves sitting next to each other person. You have a circular table that will be just big enough to fit everyone comfortably, and so each person will have exactly two neighbors.

For example, suppose you have only four attendees planned, and you calculate their potential happiness as follows:

Alice would gain 54 happiness units by sitting next to Bob.
Alice would lose 79 happiness units by sitting next to Carol.
Alice would lose 2 happiness units by sitting next to David.
Bob would gain 83 happiness units by sitting next to Alice.
Bob would lose 7 happiness units by sitting next to Carol.
Bob would lose 63 happiness units by sitting next to David.
Carol would lose 62 happiness units by sitting next to Alice.
Carol would gain 60 happiness units by sitting next to Bob.
Carol would gain 55 happiness units by sitting next to David.
David would gain 46 happiness units by sitting next to Alice.
David would lose 7 happiness units by sitting next to Bob.
David would gain 41 happiness units by sitting next to Carol.
Then, if you seat Alice next to David, Alice would lose 2 happiness units (because David talks so much), but David would gain 46 happiness units (because Alice is such a good listener), for a total change of 44.

If you continue around the table, you could then seat Bob next to Alice (Bob gains 83, Alice gains 54). Finally, seat Carol, who sits next to Bob (Carol gains 60, Bob loses 7) and David (Carol gains 55, David gains 41). The arrangement looks like this:

+41 +46
+55   David    -2
Carol       Alice
+60    Bob    +54
-7  +83
After trying every other seating arrangement in this hypothetical scenario, you find that this one is the most optimal, with a total change in happiness of 330.

What is the total change in happiness for the optimal seating arrangement of the actual guest list?

--- Part Two ---

In all the commotion, you realize that you forgot to seat yourself. At this point, you're pretty apathetic toward the whole thing, and your happiness wouldn't really go up or down regardless of who you sit next to. You assume everyone else would be just as ambivalent about sitting next to you, too.

So, add yourself to the list, and give all happiness relationships that involve you a score of 0.

What is the total change in happiness for the optimal seating arrangement that actually includes yourself?

 */

fun main(args: Array<String>) {
    val test = """
               |Alice would gain 54 happiness units by sitting next to Bob.
               |Alice would lose 79 happiness units by sitting next to Carol.
               |Alice would lose 2 happiness units by sitting next to David.
               |Bob would gain 83 happiness units by sitting next to Alice.
               |Bob would lose 7 happiness units by sitting next to Carol.
               |Bob would lose 63 happiness units by sitting next to David.
               |Carol would lose 62 happiness units by sitting next to Alice.
               |Carol would gain 60 happiness units by sitting next to Bob.
               |Carol would gain 55 happiness units by sitting next to David.
               |David would gain 46 happiness units by sitting next to Alice.
               |David would lose 7 happiness units by sitting next to Bob.
               |David would gain 41 happiness units by sitting next to Carol.
               """.trimMargin()
    val input = parseInput("day13-input.txt")

    println(findBestCombination(test) == 330)
    println(findBestCombination(input))
    println(findBestCombination(input, "Me"))
}

fun findBestCombination(input: String, append: String? = null): Int? {
    val units = parseHappinessUnits(input)
    val people = units.map { it.who }
            .distinct()
            .let { if (append != null) it.plus(append) else it }

    return people
            .permutations()
            .map { people ->
                people.circularCombinations()
                        .map {
                            val (prev, current, next) = it

                            sequenceOf(
                                    units.find { it.who == current && it.toWhom == prev }?.points ?: 0,
                                    units.find { it.who == current && it.toWhom == next }?.points ?: 0
                            )
                        }
                        .sumBy { it.sum() }
            }
            .max()
}

data class HappinessUnit(val who: String, val toWhom: String, val points: Int)

private fun <T> List<T>.circularCombinations() = (0..size - 1)
        .map { i ->
            val prev = if (i - 1 < 0) size - 1 else i - 1
            val current = i
            val next = if (i + 1 == size) 0 else i + 1

            Triple(this[prev], this[current], this[next])
        }

private fun parseHappinessUnits(input: String) = input.split("\n")
        .map(String::trim)
        .filter(String::isNotEmpty)
        .map {
            val regex = Regex("""(\w+) would (lose|gain) (\d+) happiness units by sitting next to (\w+)""")
            val (who, type, points, toWhom) = regex.findAll(it)
                    .toList()
                    .flatMap { it.groupValues.drop(1) }

            HappinessUnit(who = who, toWhom = toWhom, points = points.toInt().let { if (type == "lose") -it else it })
        }
