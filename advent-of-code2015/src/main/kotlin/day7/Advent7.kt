package day7

import day7.Operation.Type.*
import parseInput

/**
This year, Santa brought little Bobby Tables a set of wires and bitwise logic gates! Unfortunately, little Bobby is a little under the recommended age range, and he needs help assembling the circuit.

Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal (a number from 0 to 65535). A signal is provided to each wire by a gate, another wire, or some specific value. Each wire can only get a signal from one source, but can provide its signal to multiple destinations. A gate provides no signal until all of its inputs have a signal.

The included instructions booklet describes how to connect the parts together: x AND y -> z means to connect wires x and y to an AND gate, and then connect its output to wire z.

For example:

123 -> x means that the signal 123 is provided to wire x.
x AND y -> z means that the bitwise AND of wire x and wire y is provided to wire z.
p LSHIFT 2 -> q means that the value from wire p is left-shifted by 2 and then provided to wire q.
NOT e -> f means that the bitwise complement of the value from wire e is provided to wire f.
Other possible gates include OR (bitwise OR) and RSHIFT (right-shift). If, for some reason, you'd like to emulate the circuit instead, almost all programming languages (for example, C, JavaScript, or Python) provide operators for these gates.

For example, here is a simple circuit:

123 -> x
456 -> y
x AND y -> d
x OR y -> e
x LSHIFT 2 -> f
y RSHIFT 2 -> g
NOT x -> h
NOT y -> i
After it is run, these are the signals on the wires:

d: 72
e: 507
f: 492
g: 114
h: 65412
i: 65079
x: 123
y: 456
In little Bobby's kit's instructions booklet (provided as your puzzle input), what signal is ultimately provided to wire a?

 */

fun main(args: Array<String>) {
    val test = """123 -> x
                  |456 -> y
                  |x AND y -> d
                  |x OR y -> e
                  |x LSHIFT 2 -> f
                  |y RSHIFT 2 -> g
                  |NOT x -> h
                  |NOT y -> i
               """.trimMargin()

    executeOperations(test)

    val input = parseInput("day7-input.txt")
    executeOperations(input)
}

fun executeOperations(input: String) {
    val pipe = hashMapOf<String, Int>()

    fun String.toSafeInt(): Int {
        val n = 65536
        return ((this.toInt() % n) + n) % n
    }

    fun getRegisterValue(data: String): Int {
        return try { data.toSafeInt() } catch (e: NumberFormatException) { pipe.getOrDefault(data, 0) }
    }

    fun Int.not(bits: Int = 16) = 1.shl(bits) - 1 - this

    for ((type, args) in parseOperations(input)) {
        when (type) {
            WRITE -> {
                val (value, to) = args

                pipe.put(to, getRegisterValue(value))
            }
            AND -> {
                val (from1, from2, to) = args

                pipe.put(to, getRegisterValue(from1).and(getRegisterValue(from2)))
            }
            OR -> {
                val (from1, from2, to) = args

                pipe.put(to, getRegisterValue(from1).or(getRegisterValue(from2)))
            }
            NOT -> {
                val (from, to) = args

                pipe.put(to, getRegisterValue(from).not())
            }
            LSHIFT -> {
                val (from, times, to) = args

                pipe.put(to, getRegisterValue(from).shl(times.toInt()))
            }
            RSHIFT -> {
                val (from, times, to) = args

                pipe.put(to, getRegisterValue(from).shr(times.toInt()))
            }
        }
    }

    println(pipe)
}

data class Operation(val type: Type, val args: List<String>) {
    enum class Type { WRITE, AND, OR, NOT, LSHIFT, RSHIFT }
}

private fun parseOperations(input: String) = input.split("\n")
        .map(String::trim)
        .filter(String::isNotEmpty)
        .map {
            val type = when {
                it.matches(Regex("""^[\d\w]+ -> \w+$""")) -> WRITE
                "AND" in it -> AND
                "OR" in it -> OR
                "NOT" in it -> NOT
                "LSHIFT" in it -> LSHIFT
                "RSHIFT" in it -> RSHIFT
                else -> throw IllegalArgumentException(it)
            }

            val args = Regex("""[\da-z]+""").findAll(it).map { it.groupValues[0] }.toList()

            Operation(type, args)
        }
