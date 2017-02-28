package day12

import day12.Operation.Type.*
import parseInput
import splitToLines
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
--- Day 12: Leonardo's Monorail ---

You finally reach the top floor of this building: a garden with a slanted glass ceiling. Looks like there are no more stars to be had.

While sitting on a nearby bench amidst some tiger lilies, you manage to decrypt some of the files you extracted from the servers downstairs.

According to these documents, Easter Bunny HQ isn't just this building - it's a collection of buildings in the nearby area. They're all connected by a local monorail, and there's another building not far from here! Unfortunately, being night, the monorail is currently not operating.

You remotely connect to the monorail control systems and discover that the boot sequence expects a password. The password-checking logic (your puzzle input) is easy to extract, but the code it uses is strange: it's assembunny code designed for the new computer you just assembled. You'll have to execute the code and get the password.

The assembunny code you've extracted operates on four registers (a, b, c, and d) that start at 0 and can hold any integer. However, it seems to make use of only a few instructions:

cpy x y copies x (either an integer or the value of a register) into register y.
inc x increases the value of register x by one.
dec x decreases the value of register x by one.
jnz x y jumps to an instruction y away (positive means forward; negative means backward), but only if x is not zero.
The jnz instruction moves relative to itself: an offset of -1 would continue at the previous instruction, while an offset of 2 would skip over the next instruction.

For example:

cpy 41 a
inc a
inc a
dec a
jnz a 2
dec a
The above code would set register a to 41, increase its value by 2, decrease its value by 1, and then skip the last dec a (because a is not zero, so the jnz a 2 skips it), leaving register a at 42. When you move past the last instruction, the program halts.

After executing the assembunny code in your puzzle input, what value is left in register a?

--- Part Two ---

As you head down the fire escape to the monorail, you notice it didn't start; register c needs to be initialized to the position of the ignition key.

If you instead initialize register c to be 1, what value is now left in register a?

 */

fun main(args: Array<String>) {
    val test = """cpy 41 a
                 |inc a
                 |inc a
                 |dec a
                 |jnz a 2
                 |dec a""".trimMargin()
    val input = parseInput("day12-input.txt")

    val callback = fun (
            operations: List<Operation>,
            registers: Map<String, Int>,
            index: Int
    ): Triple<List<Operation>, Map<String, Int>, Int> {
        val r = HashMap(registers)
        var i = index

        fun safeValue(data: String) = try { data.toInt() } catch (e: Exception) { r[data] ?: 0 }

        val (type, arg) = operations[index]
        when (type) {
            CPY -> r.computeIfPresent(arg[1], { k, v -> safeValue(arg[0]) })
            INC -> r.computeIfPresent(arg[0], { k, v -> v + 1 })
            DEC -> r.computeIfPresent(arg[0], { k, v -> v - 1 })
            JNZ -> if (safeValue(arg[0]) != 0) i += safeValue(arg[1]) - 1
            else -> {}
        }

        return Triple(operations, r, i + 1)
    }

    println(executeOperations(test, callback, mapOf("a" to 0)) == mapOf("a" to 42))
    println(executeOperations(input, callback, mapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)))
    println(executeOperations(input, callback, mapOf("a" to 0, "b" to 0, "c" to 1, "d" to 0)))
}

data class Operation(val type: Operation.Type, val args: List<String>) {
    enum class Type { CPY, INC, DEC, JNZ, TGL }
}

fun executeOperations(
        input: String,
        callback: (List<Operation>, Map<String, Int>, Int) -> Triple<List<Operation>, Map<String, Int>, Int>,
        initial: Map<String, Int>
): Map<String, Int> {
    var operations = parseOperations(input)
    var registers = initial
    var index = 0

    while (index < operations.size) {
        val (o, r, i) = callback(operations, registers, index)
        operations = o
        registers = r
        index = i
    }

    return registers
}

fun parseOperations(input: String) = input.splitToLines()
        .map {
            val args = it.split(" ")
            val type = Operation.Type.valueOf(args[0].toUpperCase())

            Operation(type, args.drop(1).take(2))
        }
