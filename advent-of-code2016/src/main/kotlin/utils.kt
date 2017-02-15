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
fun String.swap(a: Int, b: Int) = toCharArray()
        .apply {
            val tmp = this[a]
            this[a] = this[b]
            this[b] = tmp
        }
        .joinToString("")
fun String.move(from: Int, to: Int) = toMutableList().apply { add(to, removeAt(from)) }.joinToString("")
fun String.rotate(times: Int = 1) = (1..Math.abs(times))
        .fold(this, { s, _i ->
            if (times > 0) {
                s.substring(1) + s.substring(0, 1)
            } else {
                s.substring(length - 1, length) + s.substring(0, length - 1)
            }
        })
fun String.reverseSubstring(from: Int, to: Int) = substring(0, from) +
        substring(from..to).reversed() +
        substring(to + 1, length)

fun String.chunk(size: Int) = (0..length - size).map { i -> substring(i, i + size) }

inline fun <reified INNER> array2d(sizeOuter: Int, sizeInner: Int, noinline innerInit: (Int) -> INNER): Array<Array<INNER>>
        = Array(sizeOuter) { Array(sizeInner, innerInit) }
inline fun <reified T> matrix2d(height: Int, width: Int, init: (Int, Int) -> Array<T>) = Array<Array<T>>(height, { row -> init(row, width) })

fun String.splitToLines(splitBy: String = "\n") = trim().split(splitBy).map(String::trim).filter(String::isNotEmpty)
fun String.toMD5() = MessageDigest.getInstance("MD5").digest(toByteArray())
fun ByteArray.toHex() = String.format("%0" + (size shl 1) + "X", BigInteger(1, this))
