import advent.Extension._

def isValid(input: String, part2: Boolean = false): Boolean =
  input.split(" ")
    .map(s => if (part2) s.sorted else s)
    .groupBy(it => it)
    .values
    .forall(_.length == 1)

isValid("aa bb cc dd ee")
!isValid("aa bb cc dd aa")
isValid("aa bb cc dd aaa")

isValid("abcde fghij", part2 = true)
!isValid("abcde xyz ecdab", part2 = true)
isValid("a ab abc abd abf abj", part2 = true)
isValid("iiii oiii ooii oooi oooo", part2 = true)
!isValid("oiii ioii iioi iiio", part2 = true)

val fileInput = "day4-input.txt".linesFromResource(getClass.getClassLoader)
fileInput.count(s => isValid(s))
fileInput.count(s => isValid(s, part2 = true))