import advent.Extension._

val testInput1 =
  """
    |5 1 9 5
    |7 5 3
    |2 4 6 8
  """.stripMargin
    .linesAll

val testInput2 =
  """
    |5 9 2 8
    |9 4 7 3
    |3 8 6 5
  """.stripMargin
    .linesAll

val fileInput = "day2-input.txt".linesFromResource(getClass.getClassLoader)

def spreadSheetCheckSum(input: List[String], func: Array[Int] => Long) = input
  .map(_.split(" "))
  .map(_.map(_.toInt))
  .map(func)
  .sum

def minMaxNumDiff(numbers: Array[Int]): Long = numbers.max - numbers.min

def evenlyDivisionResult(numbers: Array[Int]): Long = (for {
  n1 <- numbers
  n2 <- numbers
  if n1 != n2
  if n1 % n2 == 0
} yield n1 / n2).head

spreadSheetCheckSum(testInput1, minMaxNumDiff) == 18
spreadSheetCheckSum(testInput2, evenlyDivisionResult) == 9

spreadSheetCheckSum(fileInput, minMaxNumDiff)
spreadSheetCheckSum(fileInput, evenlyDivisionResult)
