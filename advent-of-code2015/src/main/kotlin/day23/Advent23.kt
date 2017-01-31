package day23

import day23.Operation.Type.*
import parseInput
import splitToLines
import java.util.*

/**
--- Day 23: Opening the Turing Lock ---

Little Jane Marie just got her very first computer for Christmas from some unknown benefactor. It comes with instructions and an example program, but the computer itself seems to be malfunctioning. She's curious what the program does, and would like you to help her run it.

The manual explains that the computer supports two registers and six instructions (truly, it goes on to remind the reader, a state-of-the-art technology). The registers are named a and b, can hold any non-negative integer, and begin with a value of 0. The instructions are as follows:

hlf r sets register r to half its current value, then continues with the next instruction.
tpl r sets register r to triple its current value, then continues with the next instruction.
inc r increments register r, adding 1 to it, then continues with the next instruction.
jmp offset is a jump; it continues with the instruction offset away relative to itself.
jie r, offset is like jmp, but only jumps if register r is even ("jump if even").
jio r, offset is like jmp, but only jumps if register r is 1 ("jump if one", not odd).
All three jump instructions work with an offset relative to that instruction. The offset is always written with a prefix + or - to indicate the direction of the jump (forward or backward, respectively). For example, jmp +1 would simply continue with the next instruction, while jmp +0 would continuously jump back to itself forever.

The program exits when it tries to run an instruction beyond the ones defined.

For example, this program sets a to 2, because the jio instruction causes it to skip the tpl instruction:

inc a
jio a, +2
tpl a
inc a
What is the value in register b when the program in your puzzle input is finished executing?


--- Part Two ---

The unknown benefactor is very thankful for releasi-- er, helping little Jane Marie with her computer. Definitely not to distract you, what is the value in register b after the program is finished executing if register a starts as 1 instead?
 */

fun main(args: Array<String>) {
    val test = """inc a
                 |jio a, +2
                 |tpl a
                 |inc a""".trimMargin()
    val input = parseInput("day23-input.txt")

    println(executeOperations(test, hashMapOf()))

    println(executeOperations(input, hashMapOf()))
    println(executeOperations(input, hashMapOf("a" to 1L)))
}

data class Operation(val type: Operation.Type, val args: List<String>) {
    enum class Type { HLF, TPL, INC, JMP, JIE, JIO }
}

fun executeOperations(input: String, registers: HashMap<String, Long>): Map<String, Long> {
    val operations = parseOperations(input)

    fun getRegisterValue(key: String) = registers.getOrElse(key, { 0 })

    var index = 0
    while (index < operations.size) {
        val operation = operations[index]

        when (operation.type) {
            HLF -> {
                val register = operation.args[0]

                registers.computeIfPresent(register, { k, v -> v / 2 })
            }
            TPL -> {
                val register = operation.args[0]

                registers.computeIfPresent(register, { k, v -> v * 3 })
            }
            INC -> {
                val register = operation.args[0]

                registers.put(register, registers.getOrElse(register, { 0 }) + 1)
            }
            JMP -> {
                index += operation.args[0].toInt() - 1
            }
            JIE -> {
                val (register, value) = operation.args

                if (getRegisterValue(register) % 2 == 0L) {
                    index += value.toInt() - 1
                }
            }
            JIO -> {
                val (register, value) = operation.args

                if (getRegisterValue(register) == 1L) {
                    index += value.toInt() - 1
                }
            }
        }

        index++
    }

    return registers
}

private fun parseOperations(input: String) = input.splitToLines()
        .map {
            val args = it.split(" ")
            val type = Operation.Type.valueOf(args[0].toUpperCase())

            Operation(type, args.drop(1).map { it.replace(",", "").replace("+", "") })
        }
