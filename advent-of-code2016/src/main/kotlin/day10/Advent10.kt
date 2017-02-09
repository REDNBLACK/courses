package day10

import array2d
import day08.Operation.Type.*
import day10.Operation.Direction
import day10.Operation.Direction.BOT
import day10.Operation.Direction.OUTPUT
import day10.Operation.Target
import day10.Operation.Type.HIGH
import day10.Operation.Type.LOW
import parseInput
import splitToLines
import java.util.*

/**
--- Day 10: Balance Bots ---

You come upon a factory in which many robots are zooming around handing small microchips to each other.

Upon closer examination, you notice that each bot only proceeds when it has two microchips, and once it does, it gives each one to a different bot or puts it in a marked "output" bin. Sometimes, bots take microchips from "input" bins, too.

Inspecting one of the microchips, it seems like they each contain a single number; the bots must use some logic to decide what to do with each chip. You access the local control computer and download the bots' instructions (your puzzle input).

Some of the instructions specify that a specific-valued microchip should be given to a specific bot; the rest of the instructions indicate what a given bot should do with its lower-value or higher-value chip.

For example, consider the following instructions:

value 5 goes to bot 2
bot 2 gives low to bot 1 and high to bot 0
value 3 goes to bot 1
bot 1 gives low to output 1 and high to bot 0
bot 0 gives low to output 2 and high to output 0
value 2 goes to bot 2
Initially, bot 1 starts with a value-3 chip, and bot 2 starts with a value-2 chip and a value-5 chip.
Because bot 2 has two microchips, it gives its lower one (2) to bot 1 and its higher one (5) to bot 0.
Then, bot 1 has two microchips; it puts the value-2 chip in output 1 and gives the value-3 chip to bot 0.
Finally, bot 0 has two microchips; it puts the 3 in output 2 and the 5 in output 0.
In the end, output bin 0 contains a value-5 microchip, output bin 1 contains a value-2 microchip, and output bin 2 contains a value-3 microchip. In this configuration, bot number 2 is responsible for comparing value-5 microchips with value-2 microchips.

Based on your instructions, what is the number of the bot that is responsible for comparing value-61 microchips with value-17 microchips?

 */

fun main(args: Array<String>) {
    val test = """
                 |value 5 goes to bot 2
                 |bot 2 gives low to bot 1 and high to bot 0
                 |value 3 goes to bot 1
                 |bot 1 gives low to output 1 and high to bot 0
                 |bot 0 gives low to output 2 and high to output 0
                 |value 2 goes to bot 2
               """.trimMargin()

//    println(execute(test))
    println(execute(parseInput("day10-input.txt")))
}

fun execute(input: String) {
    val bots = parseValueAssignments(input)
            .groupBy { it.botNumber }
            .map { it.key to Bot(it.key, it.value.map { it.value }) }
            .toMap(HashMap())
    println(bots)
    val bins = HashMap<Int, Bin>()
    val operations = parseOperations(input)

    for ((botNumber, targets) in operations) {
        val bot = bots.getOrElse(botNumber, { null }) ?: continue

        for ((entityNumber, direction, type) in targets) {
            val value = when (type) { LOW -> bot.low(); HIGH -> bot.high() }

            when (direction) {
                OUTPUT -> bins.compute(entityNumber, { k, v -> (v ?: Bin(k)).addChip(value) })
                BOT -> bots.compute(entityNumber, { k, v -> (v ?: Bot(k)).addChip(value) })
            }
        }

        bots.put(botNumber, bot.clearChips())
    }

    bots.values.forEach { println(it) }
    bins.values.forEach { println(it) }
}

data class Bot(val number: Int, val chips: List<Int> = listOf()) {
    fun low() = chips.min() ?: throw RuntimeException()
    fun high() = chips.max() ?: throw RuntimeException()
    fun addChip(value: Int) = copy(chips = chips.plus(value))
    fun clearChips() = copy(chips = listOf())
}
data class Bin(val number: Int, val chips: List<Int> = listOf()) {
    fun addChip(value: Int) = copy(chips = chips.plus(value))
}
data class Operation(val botNumber: Int, val targets: List<Target>) {
    enum class Direction { OUTPUT, BOT }
    enum class Type { LOW, HIGH }
    data class Target(val entityNumber: Int, val direction: Direction, val type: Type)
}
data class ValueAssign(val botNumber: Int, val value: Int)

private fun parseOperations(input: String) = input.splitToLines()
        .filter { it.startsWith("bot") }
        .sortedDescending()
        .map {
            val (botNumber, lowDirection, lowNumber, highDirection, highNumber) =
                    Regex("""bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)""")
                    .findAll(it)
                    .map { it.groupValues.drop(1).toList() }
                    .toList()
                    .flatMap { it }

            Operation(
                    botNumber = botNumber.toInt(),
                    targets = listOf(
                            Target(lowNumber.toInt(), Direction.valueOf(lowDirection.toUpperCase()), LOW),
                            Target(highNumber.toInt(), Direction.valueOf(highDirection.toUpperCase()), HIGH)
                    )
            )
        }

private fun parseValueAssignments(input: String) = input.splitToLines()
        .filter { it.startsWith("value") }
        .map {
            val (value, botNumber) = Regex("""(\d+)""")
                    .findAll(it)
                    .map { it.groupValues[1] }
                    .map(String::toInt)
                    .toList()

            ValueAssign(botNumber, value)
        }
