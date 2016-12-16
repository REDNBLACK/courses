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

fun String.isPalindrome(): Boolean {
    val length = this.length / 2
    val firstHalf = this.substring(0, length)
    val secondHalf = this.substring(length)

    if (firstHalf.groupBy { it }.size == 1) return false

    return firstHalf == secondHalf.reversed()
}

fun String.middleNChars(n: Int): String {
    val middle = this.length / 2
    val first = this.substring(middle - n / 2, middle)
    val second = this.substring(middle, middle + n / 2)

    return first + second
}
