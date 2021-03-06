package day12

import day12.Operation.Type.*
import parseInput
import splitToLines

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

    println(executeOperations(test, mapOf("a" to 0)) == mapOf("a" to 42))
    println(executeOperations(input, mapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)))
    println(executeOperations(input, mapOf("a" to 0, "b" to 0, "c" to 1, "d" to 0)))
}

data class Operation(val type: Operation.Type, val args: List<String>) {
    enum class Type { CPY, INC, DEC, JNZ, TGL }
}

fun executeOperations(input: String, initial: Map<String, Int>): Map<String, Int> {
    val operations = parseOperations(input)
    val registers = initial.values.toIntArray()
    var index = 0

    fun safeIndex(data: String) = when (data[0]) {
        'a' -> 0
        'b' -> 1
        'c' -> 2
        'd' -> 3
        else -> null
    }

    fun safeValue(data: String) = when (data[0]) {
        'a' -> registers[0]
        'b' -> registers[1]
        'c' -> registers[2]
        'd' -> registers[3]
        else -> data.toInt()
    }

    while (index < operations.size) {
        val (type, arg) = operations[index]
        when (type) {
            CPY -> safeIndex(arg[1])?.let { registers[it] = safeValue(arg[0]) }
            INC -> safeIndex(arg[0])?.let { registers[it] += 1 }
            DEC ->  safeIndex(arg[0])?.let { registers[it] -= 1 }
            JNZ -> if (safeValue(arg[0]) != 0) index += safeValue(arg[1]) - 1
            TGL -> {
                val changeIndex = index + safeValue(arg[0])

                if (changeIndex < operations.size) {
                    val changeOperation = operations[changeIndex]
                    val newType = when (changeOperation.type) {
                        CPY -> JNZ
                        JNZ -> CPY
                        TGL -> INC
                        INC -> DEC
                        DEC -> INC
                    }

                    operations[changeIndex] = changeOperation.copy(type = newType)
                }
            }
        }

        index++
    }

    return initial.keys.toTypedArray().zip(registers.toTypedArray()).toMap()
}

private fun parseOperations(input: String) = input.splitToLines()
        .map {
            val args = it.split(" ")
            val type = Operation.Type.valueOf(args[0].toUpperCase())

            Operation(type, args.drop(1).take(2))
        }
        .toMutableList()
