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

fun Iterable<Int>.mul() = reduce { a, b -> a * b }
fun Iterable<Long>.mul() = reduce { a, b -> a * b }
fun <T> List<T>.chunk(size: Int): List<List<T>> {
    return (0..lastIndex / size).map {
        val fromIndex = it * size
        val toIndex = Math.min(fromIndex + size, this.size)
        subList(fromIndex, toIndex)
    }
}

fun String.chunk(size: Int) = (0..length - size).map { i -> substring(i, i + size) }

inline fun <reified INNER> array2d(sizeOuter: Int, sizeInner: Int, noinline innerInit: (Int) -> INNER): Array<Array<INNER>>
        = Array(sizeOuter) { Array(sizeInner, innerInit) }


fun String.splitToLines(splitBy: String = "\n") = trim().split(splitBy).map(String::trim).filter(String::isNotEmpty)
fun String.toMD5() = MessageDigest.getInstance("MD5").digest(toByteArray())
fun ByteArray.toHex() = String.format("%0" + (size shl 1) + "X", BigInteger(1, this))
