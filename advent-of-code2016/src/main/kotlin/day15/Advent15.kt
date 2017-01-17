package day15

import toHex
import toMD5

/**
--- Day 15: Timing is Everything ---

The halls open into an interior plaza containing a large kinetic sculpture. The sculpture is in a sealed enclosure and seems to involve a set of identical spherical capsules that are carried to the top and allowed to bounce through the maze of spinning pieces.

Part of the sculpture is even interactive! When a button is pressed, a capsule is dropped and tries to fall through slots in a set of rotating discs to finally go through a little hole at the bottom and come out of the sculpture. If any of the slots aren't aligned with the capsule as it passes, the capsule bounces off the disc and soars away. You feel compelled to get one of those capsules.

The discs pause their motion each second and come in different sizes; they seem to each have a fixed number of positions at which they stop. You decide to call the position with the slot 0, and count up for each position it reaches next.

Furthermore, the discs are spaced out so that after you push the button, one second elapses before the first disc is reached, and one second elapses as the capsule passes from one disc to the one below it. So, if you push the button at time=100, then the capsule reaches the top disc at time=101, the second disc at time=102, the third disc at time=103, and so on.

The button will only drop a capsule at an integer time - no fractional seconds allowed.

For example, at time=0, suppose you see the following arrangement:

Disc #1 has 5 positions; at time=0, it is at position 4.
Disc #2 has 2 positions; at time=0, it is at position 1.
If you press the button exactly at time=0, the capsule would start to fall; it would reach the first disc at time=1. Since the first disc was at position 4 at time=0, by time=1 it has ticked one position forward. As a five-position disc, the next position is 0, and the capsule falls through the slot.

Then, at time=2, the capsule reaches the second disc. The second disc has ticked forward two positions at this point: it started at position 1, then continued to position 0, and finally ended up at position 1 again. Because there's only a slot at position 0, the capsule bounces away.

If, however, you wait until time=5 to push the button, then when the capsule reaches each disc, the first disc will have ticked forward 5+1 = 6 times (to position 0), and the second disc will have ticked forward 5+2 = 7 times (also to position 0). In this case, the capsule would fall through the discs and come out of the machine.

However, your situation has more than two discs; you've noted their positions in your puzzle input. What is the first time you can press the button to get a capsule?

 */

fun main(args: Array<String>) {
    val hash1 = fun (salt: String): (Int) -> (String) = { (salt + it).toMD5().toHex() }

    println(findLast(hash1("abc")) == 22728)
    println(findLast(hash1("jlmsuwbz")))

    val hash2 = fun (salt: String): (Int) -> (String) = { it ->
        (0..2015).fold((salt + it).toMD5().toHex().toLowerCase(), { result, i ->
            result.toMD5().toHex().toLowerCase()
        })
    }

    println(findLast(hash2("abc")) == 22551)
    println(findLast(hash2("jlmsuwbz")))
}

fun findLast(hashing: (Int) -> String, lastIndex: Int = 64): Int? {
    fun String.findRepeatingChar(times: Int) = (0..length - times)
            .map { i -> substring(i, i + times) }
            .find { it.groupBy { it }.size == 1 }
            ?.get(0)


    val hashes = (0..999).map { hashing(it) }.toMutableList()

    tailrec fun findAll(i: Int = 0, collector: List<Int> = listOf()): List<Int> {
        if (collector.size == lastIndex) return collector

        val current = hashes[i % 1000].findRepeatingChar(3).toString().repeat(5)
        hashes[i % 1000] = hashing(i + 1000)

        if (hashes.none { it.contains(current) }) return findAll(i + 1, collector)

        return findAll(i + 1, collector + i)
    }

    return findAll().getOrNull(lastIndex - 1)
}
