import java.math.BigInteger
import java.security.MessageDigest

fun parseInput(file: String): String {
    return Thread.currentThread()
            .contextClassLoader
            .getResourceAsStream(file)
            .bufferedReader()
            .use { it.readText() }
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
fun <T : Any> List<T>.combinations(n: Int) : List<List<T>> = if (n == 0) listOf(emptyList()) else {
    flatMapTails { subList -> subList.tail().combinations(n - 1).map { it + subList.first() } }
}
private fun <T> List<T>.flatMapTails(f: (List<T>) -> (List<List<T>>)): List<List<T>> = if (isEmpty()) emptyList() else {
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
fun String.toMD5() = MessageDigest.getInstance("MD5").digest(toByteArray())
fun ByteArray.toHex() = String.format("%0" + (size shl 1) + "X", BigInteger(1, this))
