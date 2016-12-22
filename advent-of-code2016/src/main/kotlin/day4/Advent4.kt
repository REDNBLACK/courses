package day4

import parseInput
import java.util.*

/**
--- Day 4: Security Through Obscurity ---

Finally, you come across an information kiosk with a list of rooms. Of course, the list is encrypted and full of decoy data, but the instructions to decode the list are barely hidden nearby. Better remove the decoy data first.

Each room consists of an encrypted name (lowercase letters separated by dashes) followed by a dash, a sector ID, and a checksum in square brackets.

A room is real (not a decoy) if the checksum is the five most common letters in the encrypted name, in order, with ties broken by alphabetization. For example:

aaaaa-bbb-z-y-x-123[abxyz] is a real room because the most common letters are a (5), b (3), and then a tie between x, y, and z, which are listed alphabetically.
a-b-c-d-e-f-g-h-987[abcde] is a real room because although the letters are all tied (1 of each), the first five are listed alphabetically.
not-a-real-room-404[oarel] is a real room.
totally-real-room-200[decoy] is not.
Of the real rooms from the list above, the sum of their sector IDs is 1514.

What is the sum of the sector IDs of the real rooms?

--- Part Two ---

With all the decoy data out of the way, it's time to decrypt this list and get moving.

The room names are encrypted by a state-of-the-art shift cipher, which is nearly unbreakable without the right software. However, the information kiosk designers at Easter Bunny HQ were not expecting to deal with a master cryptographer like yourself.

To decrypt a room name, rotate each letter forward through the alphabet a number of times equal to the room's sector ID. A becomes B, B becomes C, Z becomes A, and so on. Dashes become spaces.

For example, the real name for qzmt-zixmtkozy-ivhz-343 is very encrypted name.

What is the sector ID of the room where North Pole objects are stored?

 */

fun main(args: Array<String>) {
    val test = """aaaaa-bbb-z-y-x-123[abxyz]
a-b-c-d-e-f-g-h-987[abcde]
not-a-real-room-404[oarel]
totally-real-room-200[decoy]
"""

    println(findReal(test).sumBy(Room::sectorId) == 1514)

    val input = parseInput("day4-input.txt")
    println(findReal(input).sumBy(Room::sectorId))

    println("qzmt-zixmtkozy-ivhz".decrypt(343) == "very encrypted name")

    println(parseRooms(input).filter { it.name.decrypt(it.sectorId).contains("northpole") })
}

data class Room(val name: String, val sectorId: Int, val checksum: String) {
    fun isValid() = name.filter { it != '-' }
            .groupBy { it }
            .map { -it.value.size to it.key }
            .sortedWith(Comparator.comparing(Pair<Int, Char>::first).thenComparing(Pair<Int, Char>::second))
            .take(5)
            .map { it.second }
            .joinToString("") == this.checksum

    companion object {
        val pattern = """(.+)-(\d+)\[(.+)\]""".toRegex()

        fun fromString(input: String): Room {
            val (name, sectorId, checksum) = pattern.matchEntire(input)?.groupValues!!.drop(1)

            return Room(name, sectorId.toInt(), checksum)
        }
    }
}

fun String.decrypt(times: Int) = toCharArray().map { it.shift(times) }.joinToString("")

fun Char.shift(times: Int): Char {
    val alphabet = ('a'..'z').toList()

    return when (this) {
        '-' -> ' '
        in alphabet -> alphabet[(alphabet.indexOf(this) + times) % alphabet.size]
        else -> this
    }
}

fun findReal(input: String) = parseRooms(input).filter(Room::isValid)

fun parseRooms(input: String): List<Room> {
    return input.split("\n")
            .map(String::trim)
            .filter(String::isNotEmpty)
            .map { Room.fromString(it) }
}
