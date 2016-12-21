package day12

import day12.Operation.Type.*
import parseInput

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

 */

fun main(args: Array<String>) {
    val test = """cpy 41 a
inc a
inc a
dec a
jnz a 2
dec a
"""

    println(executeOperations(test))

    val input = parseInput("day12-input.txt")
    println(executeOperations(input))
}

data class Operation(val type: Operation.Type, val register: String, val value: String) {
    enum class Type {
        CPY, INC, DEC, JNZ;
    }

    companion object {
        fun fromString(str: String): Operation {
            val args = str.split(" ")
            val type = Type.valueOf(args[0].toUpperCase())
            val register = if (type == CPY) args[2] else args[1]
            val value = if (type == CPY) args[1] else args.getOrElse(2, { "1" })

            return Operation(type, register, value)
        }
    }
}

fun executeOperations(input: String): Map<String, Int> {
    val operations = parseOperations(input)
    val registers = mutableMapOf<String, Int>()

    fun getRegisterValue(data: String): Int {
        return try { data.toInt() } catch (e: NumberFormatException) { registers.getOrElse(data, { 0 }) }
    }

    var index = 0
    while (true) {
        if (index >= operations.size) break

        val operation = operations[index]
        when (operation.type) {
            CPY -> {
                registers.put(operation.register, getRegisterValue(operation.value))
            }
            DEC, INC -> {
                val oldValue = getRegisterValue(operation.register)
                val curValue = if (operation.type == DEC) -operation.value.toInt() else operation.value.toInt()

                registers.put(operation.register, oldValue + curValue)
            }
            JNZ -> {
                if (getRegisterValue(operation.register) != 0) {
                    index += getRegisterValue(operation.value) - 1
                }
            }
        }

        index++
    }

    return registers
}

fun parseOperations(input: String): List<Operation> {
    return input.split("\n")
            .map(String::trim)
            .filter(String::isNotEmpty)
            .map { Operation.fromString(it) }
}
