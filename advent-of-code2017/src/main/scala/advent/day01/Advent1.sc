import advent.Extension._

def solveCaptcha(input: String, part2: Boolean = false): Long = {
  val numbers = input.toList.map(_ - '0').map(_.toByte)
  val offset = if (part2) numbers.length / 2 else 1

  numbers.zip(numbers.drop(offset) ::: numbers.take(offset))
    .filter({ case (a, b) => a == b })
    .map(_._2)
    .foldLeft(0L) { (a, b) => a + b }
}

solveCaptcha("1122") == 3
solveCaptcha("1111") == 4
solveCaptcha("1234") == 0
solveCaptcha("91212129") == 9
solveCaptcha("11232321") == 2

solveCaptcha("1212", part2 = true) == 6
solveCaptcha("1221", part2 = true) == 0
solveCaptcha("123425", part2 = true) == 4
solveCaptcha("123123", part2 = true) == 12
solveCaptcha("12131415", part2 = true) == 4

val fileInput = "day1-input.txt".linesFromResource(getClass.getClassLoader).head
solveCaptcha(fileInput)
solveCaptcha(fileInput, part2 = true)