package day7

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

//    println(Generated().getMembers())

    println(generateClass(test))
    println(generateClass(parseInput("day7-input.txt")))
}

fun generateClass(input: String): String {
    return parseOperations(input)
            .plus("fun getMembers() = Generated::class.declaredMemberProperties.map { it.name to it.get(this) }")
            .map { " ".repeat(4) + it }
            .joinToString(
                    separator = System.lineSeparator(),
                    prefix = "class Generated {${System.lineSeparator()}",
                    postfix = "${System.lineSeparator()}}"
            )
}

private fun parseOperations(input: String) = input.split("\n")
        .map(String::trim)
        .filter(String::isNotEmpty)
        .map {
            fun String.safeRename() = if (this in listOf("as", "is", "in", "if", "do")) "reserved_" + this else this
            val template = "val %s: Int by lazy { %s }"
            val args = Regex("""[\da-z]+""").findAll(it).map { it.groupValues[0] }.toList().map(String::safeRename)
            val src = when {
                it.matches(Regex("""^[\d\w]+ -> \w+$""")) -> {
                    val (value, to) = args

                    template.format(to, value)
                }
                "AND" in it -> {
                    val (from1, from2, to) = args

                    template.format(to, "$from1 and $from2")
                }
                "OR" in it -> {
                    val (from1, from2, to) = args

                    template.format(to, "$from1 or $from2")
                }
                "NOT" in it -> {
                    val (from, to) = args

                    template.format(to, "1.shl(16) - 1 - $from")
                }
                "LSHIFT" in it -> {
                    val (from, times, to) = args

                    template.format(to, "$from shl $times")
                }
                "RSHIFT" in it -> {
                    val (from, times, to) = args

                    template.format(to, "$from shr $times")
                }
                else -> throw IllegalArgumentException(it)
            }

            src
        }

