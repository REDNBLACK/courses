import java.math.BigInteger
import java.security.MessageDigest

fun parseInput(file: String): String {
    return Thread.currentThread()
            .contextClassLoader
            .getResourceAsStream(file)
            .bufferedReader()
            .use { it.readText() }
            .trim()
}

fun <T> List<T>.chunk(size: Int): List<List<T>> {
    return (0..lastIndex / size).map {
        val fromIndex = it * size
        val toIndex = Math.min(fromIndex + size, this.size)
        subList(fromIndex, toIndex)
    }
}
fun <T : Any> List<T>.permutations() : Sequence<List<T>> = if (size == 1) sequenceOf(this) else {
    val iterator = iterator()
    var head = iterator.next()
    var permutations = (this - head).permutations().iterator()

    fun nextPermutation(): List<T>? = if (permutations.hasNext()) permutations.next() + head else {
        if (iterator.hasNext()) {
            head = iterator.next()
            permutations = (this - head).permutations().iterator()
            nextPermutation()
        } else null
    }

    generateSequence { nextPermutation() }
}
fun <T : Any> List<T>.combinations(n: Int) : Sequence<List<T>> = if (n == 0) sequenceOf(emptyList()) else {
    flatMapTails { subList -> subList.tail().combinations(n - 1).map { it + subList.first() } }
}
private fun <T> List<T>.flatMapTails(f: (List<T>) -> (Sequence<List<T>>)): Sequence<List<T>> = if (isEmpty()) emptySequence() else {
    f(this) + this.tail().flatMapTails(f)
}
fun <T> List<T>.tail(): List<T> = drop(1)
fun <T> List<T>.split(size: Int) = (0..this.size - size)
        .fold(mutableListOf<List<T>>(), { result, i ->
            result.add(subList(i, i + size))
            result
        })

inline fun <reified INNER> array2d(sizeOuter: Int, sizeInner: Int, noinline innerInit: (Int) -> INNER): Array<Array<INNER>>
        = Array(sizeOuter) { Array(sizeInner, innerInit) }

fun String.chunk(size: Int) = (0..length - size).map { i -> substring(i, i + size) }
fun String.splitToLines() = trim().split("\n").map(String::trim).filter(String::isNotEmpty)
fun String.toMD5() = MessageDigest.getInstance("MD5").digest(toByteArray())
fun String.indexOfAll(needle: String): List<Int> {
    val result = mutableListOf<Int>()
    var index = indexOf(needle)
    while (index >= 0) {
        if (index >= 0) result.add(index)
        index = indexOf(needle, index + 1)
    }

    return result
}
fun ByteArray.toHex() = String.format("%0" + (size shl 1) + "X", BigInteger(1, this))
