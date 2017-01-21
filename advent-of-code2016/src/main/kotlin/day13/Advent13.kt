package day13

import array2d
import parseInput

/**
--- Day 13: A Maze of Twisty Little Cubicles ---

You arrive at the first floor of this new building to discover a much less welcoming environment than the shiny atrium of the last one. Instead, you are in a maze of twisty little cubicles, all alike.

Every location in this area is addressed by a pair of non-negative integers (x,y). Each such coordinate is either a wall or an open space. You can't move diagonally. The cube maze starts at 0,0 and seems to extend infinitely toward positive x and y; negative values are invalid, as they represent a location outside the building. You are in a small waiting area at 1,1.

While it seems chaotic, a nearby morale-boosting poster explains, the layout is actually quite logical. You can determine whether a given x,y coordinate will be a wall or an open space using a simple system:

Find x*x + 3*x + 2*x*y + y + y*y.
Add the office designer's favorite number (your puzzle input).
Find the binary representation of that sum; count the number of bits that are 1.
If the number of bits that are 1 is even, it's an open space.
If the number of bits that are 1 is odd, it's a wall.
For example, if the office designer's favorite number were 10, drawing walls as # and open spaces as ., the corner of the building containing 0,0 would look like this:

0123456789
0 .#.####.##
1 ..#..#...#
2 #....##...
3 ###.#.###.
4 .##..#..#.
5 ..##....#.
6 #...##.###
Now, suppose you wanted to reach 7,4. The shortest route you could take is marked as O:

0123456789
0 .#.####.##
1 .O#..#...#
2 #OOO.##...
3 ###O#.###.
4 .##OO#OO#.
5 ..##OOO.#.
6 #...##.###
Thus, reaching 7,4 would take a minimum of 11 steps (starting from your current location, 1,1).

What is the fewest number of steps required for you to reach 31,39?

 */

fun main(args: Array<String>) {
    val matrix1 = array2d(7, 10) { false }

    matrix1.fillMatrix(10)
    matrix1.drawMatrix()
    findPath(matrix1)
}

data class Pos(val x: Int, val y: Int)

fun findPath(matrix: Array<Array<Boolean>>) {
    val resultPos = Pos(7, 4)
    var pos = Pos(1, 1)

    while (pos != resultPos) {
        pos = directions(pos.x, pos.y).filter { it.x >= 0 && it.y >= 0 && !matrix[it.x][it.y] }
                .first()
    }
}

private fun directions(x: Int, y: Int) = listOf(Pos(x - 1, y), Pos(x + 1, y), Pos(x, y - 1), Pos(x, y + 1))

private fun isWall(x: Int, y: Int, n: Int) = Integer.toBinaryString(x * x + 3 * x + 2 * x * y + y + y * y + n)
        .count { it == '1' } % 2 != 0

private fun Array<Array<Boolean>>.fillMatrix(n: Int) {
    for ((x, row) in withIndex()) {
        for ((y, _skip) in row.withIndex()) {
            this[x][y] = isWall(y, x, n)
        }
    }
}

private fun Array<Array<Boolean>>.drawMatrix() {
    println(map { it.map { if (it) '#' else '.' }.joinToString("") }.joinToString(System.lineSeparator()))
}
