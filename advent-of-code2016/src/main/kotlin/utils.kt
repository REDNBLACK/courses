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

inline fun <reified INNER> array2d(sizeOuter: Int, sizeInner: Int, noinline innerInit: (Int) -> INNER): Array<Array<INNER>>
        = Array(sizeOuter) { Array(sizeInner, innerInit) }


fun String.toMD5() = MessageDigest.getInstance("MD5").digest(toByteArray())
fun ByteArray.toHex() = String.format("%0" + (size shl 1) + "X", BigInteger(1, this))
