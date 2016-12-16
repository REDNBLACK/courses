package day7

import parseInput

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

 */

fun main(args: Array<String>) {
    val test = """abba[mnop]qrst
abcd[bddb]xyyx
aaaa[qwer]tyui
ioxxoj[asdfgh]zxcvbn
"""

    println(findSupportingTLS(test))

    val input = parseInput("day7-input.txt")
    println(findSupportingTLS(input).size)
}

data class IP(val sequences: List<String>, val hypernet: List<String>) {
    fun supportsTLS(): Boolean {
        if (hypernet.any { it.hasAbba() }) return false

        return sequences.any { it.hasAbba() }
    }

    private fun String.hasAbba(): Boolean {
        if (this.length < 4) return false

        return (0..this.length - 4).any {
            val part = this.substring(it, it + 4)
            part == part.reversed() && part.groupBy { it }.size > 1
        }
    }

    companion object {
        val pattern = """\[([^\]]+)\]""".toRegex()

        fun fromString(str: String): IP {
            val hypernets = pattern.findAll(str).map { it.groupValues[1] }.toList()
            val sequences = pattern.split(str)

            return IP(sequences, hypernets)
        }
    }
}

fun findSupportingTLS(input: String) = parseIP(input).filter(IP::supportsTLS)

fun parseIP(input: String): List<IP> {
    return input.split("\n")
            .map(String::trim)
            .filter(String::isNotEmpty)
            .map { IP.fromString(it) }
}
