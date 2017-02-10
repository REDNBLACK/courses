package day07

import chunk
import parseInput
import splitToLines

/**
--- Day 7: Internet Protocol Version 7 ---

While snooping around the local network of EBHQ, you compile a list of IP addresses (they're IPv7, of course; IPv6 is much too limited). You'd like to figure out which IPs support TLS (transport-layer snooping).

An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or ABBA. An ABBA is any four-character sequence which consists of a pair of two different characters followed by the reverse of that pair, such as xyyx or abba. However, the IP also must not have an ABBA within any hypernet sequences, which are contained by square brackets.

For example:

abba[mnop]qrst supports TLS (abba outside square brackets).
abcd[bddb]xyyx does not support TLS (bddb is within square brackets, even though xyyx is outside square brackets).
aaaa[qwer]tyui does not support TLS (aaaa is invalid; the interior characters must be different).
ioxxoj[asdfgh]zxcvbn supports TLS (oxxo is outside square brackets, even though it's within a larger string).
How many IPs in your puzzle input support TLS?

--- Part Two ---

You would also like to know which IPs support SSL (super-secret listening).

An IP supports SSL if it has an Area-Broadcast Accessor, or ABA, anywhere in the supernet sequences (outside any square bracketed sections), and a corresponding Byte Allocation Block, or BAB, anywhere in the hypernet sequences. An ABA is any three-character sequence which consists of the same character twice with a different character between them, such as xyx or aba. A corresponding BAB is the same characters but in reversed positions: yxy and bab, respectively.

For example:

aba[bab]xyz supports SSL (aba outside square brackets with corresponding bab within square brackets).
xyx[xyx]xyx does not support SSL (xyx, but no corresponding yxy).
aaa[kek]eke supports SSL (eke in supernet with corresponding kek in hypernet; the aaa sequence is not related, because the interior character must be different).
zazbz[bzb]cdb supports SSL (zaz has no corresponding aza, but zbz has a corresponding bzb, even though zaz and zbz overlap).
How many IPs in your puzzle input support SSL?

 */

fun main(args: Array<String>) {
    val test = """abba[mnop]qrst
                 |abcd[bddb]xyyx
                 |aaaa[qwer]tyui
                 |ioxxoj[asdfgh]zxcvbn""".trimMargin()
    val test2 = """aba[bab]xyz
                  |xyx[xyx]xyx
                  |aaa[kek]eke
                  |zazbz[bzb]cdb""".trimMargin()
    val input = parseInput("day7-input.txt")

    println(findSupportingTLS(test).size == 2)
    println(findSupportingTLS(input).size)
    println(finsSupportingSSL(test2).size == 3)
    println(finsSupportingSSL(input).size)
}

fun findSupportingTLS(input: String) = parseIP(input).filter(IP::supportsTLS)
fun finsSupportingSSL(input: String) = parseIP(input).filter(IP::supportsSSL)

data class IP(val sequences: List<String>, val hypernet: List<String>) {
    fun supportsTLS() = !hypernet.any { it.hasRepeating(4) } && sequences.any { it.hasRepeating(4) }

    fun supportsSSL() = sequences.filter { it.hasRepeating(3) }
            .filter { aba -> hypernet.any { compare(it, aba, 3) } }
            .isNotEmpty()

    private fun String.hasRepeating(length: Int) = chunk(length).any {
        it == it.reversed() && it.toCharArray().distinct().size > 1
    }

    private fun compare(str1: String, str2: String, length: Int): Boolean {
        val first = str1.chunk(length)
        val second = str2.chunk(length)
                .map { if (it[0] == it[2]) it[1].toString() + it[0] + it[1] else "" }

        return first.any { it in second }
    }
}

private fun parseIP(input: String) = input.splitToLines()
        .map {
            val pattern = Regex("""\[([^\]]+)\]""")
            val hypernets = pattern.findAll(it).map { it.groupValues[1] }.toList()
            val sequences = pattern.split(it)

            IP(sequences, hypernets)
        }
