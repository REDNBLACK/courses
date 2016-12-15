package day3

/**
--- Day 3: Squares With Three Sides ---

Now that you can think clearly, you move deeper into the labyrinth of hallways and office furniture that makes up this part of Easter Bunny HQ. This must be a graphic design department; the walls are covered in specifications for triangles.

Or are they?

The design document gives the side lengths of each triangle it describes, but... 5 10 25? Some of these aren't triangles. You can't help but mark the impossible ones.

In a valid triangle, the sum of any two sides must be larger than the remaining side. For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.

In your puzzle input, how many of the listed triangles are possible?

 */

fun main(args: Array<String>) {
    println(findPossible("5 10 25").size)

    val input = Thread.currentThread()
            .contextClassLoader
            .getResourceAsStream("day3-input.txt")
            .bufferedReader()
            .use { it.readText() }

    println(findPossible(input).size)
}

data class Triangle(val x: Int, val y: Int, val z: Int)

fun findPossible(input: String): List<Triangle> {
    val triangles = parseTriangles(input)

    return triangles.filter { t ->
        t.x + t.y > t.z && t.x + t.z > t.y && t.y + t.z > t.x
    }
}

fun parseTriangles(input: String): List<Triangle> {
    return input.split("\n")
            .map(String::trim)
            .filter(String::isNotEmpty)
            .map {
                val (x, y, z) = it.split(" ")
                        .map(String::trim)
                        .filter(String::isNotEmpty)
                        .map(String::toInt)

                Triangle(x, y, z)
            }
}
