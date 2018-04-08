import advent.Extension._

import scala.annotation.tailrec

val testInput1 =
  """
    |0
    |3
    |0
    |1
    |-3
  """.stripMargin
    .linesAll


case class Move(value: Int) {
  def inc() = Move(value + 1)
  def incOrDec() = Move(if (value >= 3) value - 1 else value + 1)
  def nextPos(curPos: Int) = curPos + value
}

def solveMaze(input: List[String], part2: Boolean = false): Int = {
  @tailrec
  def iterate(moves: List[Move], pos: Int, counter: Int): Int = {
    val move = moves(pos)
    val nextPos = move.nextPos(pos)
    if (nextPos >= moves.length) return counter

    iterate(moves.updated(pos, if (part2) move.incOrDec() else move.inc()), nextPos, counter + 1)
  }

  iterate(input.map(n => Move(n.toInt)), 0, 1)
}

solveMaze(testInput1) == 5
solveMaze(testInput1, part2 = true) == 10

val fileInput = "day5-input.txt".linesFromResource(getClass.getClassLoader)
solveMaze(fileInput)
solveMaze(fileInput, part2 = true)