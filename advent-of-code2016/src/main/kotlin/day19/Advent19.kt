package day19

import java.util.*

/**
--- Day 19: An Elephant Named Joseph ---

The Elves contact you over a highly secure emergency channel. Back at the North Pole, the Elves are busy misunderstanding White Elephant parties.

Each Elf brings a present. They all sit in a circle, numbered starting with position 1. Then, starting with the first Elf, they take turns stealing all the presents from the Elf to their left. An Elf with no presents is removed from the circle and does not take turns.

For example, with five Elves (numbered 1 to 5):

1
5   2
4 3
Elf 1 takes Elf 2's present.
Elf 2 has no presents and is skipped.
Elf 3 takes Elf 4's present.
Elf 4 has no presents and is also skipped.
Elf 5 takes Elf 1's two presents.
Neither Elf 1 nor Elf 2 have any presents, so both are skipped.
Elf 3 takes Elf 5's three presents.
So, with five Elves, the Elf that sits starting in position 3 gets all the presents.

With the number of Elves given in your puzzle input, which Elf gets all the presents?
 */

fun main(args: Array<String>) {
    println(findWhoGetsAllPresents(5) == 3 to 5L)
}

fun findWhoGetsAllPresents(elvesCount: Int): Pair<Int, Long?> {
    val elves = LongArray(elvesCount) { 1L }
    val deque = ArrayDeque((0..elves.size - 1).toList())

    while (deque.size >= 2) {
        val i = deque.poll()
        val j = deque.poll()

        val currElf = elves[i]
        val nextElf = elves[j]

        if (currElf > 0 && nextElf > 0) {
            elves[i] = currElf + nextElf
            elves[j] = 0
            deque.remove(j)
        }

        deque.addLast(i)
    }

    val maxPresents = elves.max()

    return elves.indexOfFirst { it == maxPresents } + 1 to maxPresents
}
