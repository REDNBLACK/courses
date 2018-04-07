import advent.Extension._
import scala.reflect.runtime._
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox


val input = """b inc 5 if a > 1
              |a inc 1 if b < 5
              |c dec -10 if a >= 1
              |c inc -20 if c == 10
            """
  .stripMargin
  .linesAll

case class Operation(oType: String, value: Int) {
  val types = Map("inc" -> "+", "dec" -> "-")

  override def toString = s"${types(oType)} $value"
}
case class Variable(name: String) {
  override def toString = s"  var $name = 0"
}
case class Condition(expr: String) {
  override def toString = s"    if ($expr)"
}
case class Expr(
  variable: Variable,
  operation: Operation,
  condition: Condition
) {
  override def toString =
    s"""$condition {
       |${variable.name} = ${variable.name} $operation
       |max = math.max(max, ${variable.name})
       |}""".stripMargin
}

val toolBox = universe.runtimeMirror(getClass.getClassLoader).mkToolBox()

def execute(input: List[String]): (Int, Int) = {
  def getExpressions: List[Expr] = {
    val pattern = """(.+)\s(.{3})\s(-?\d+)\sif\s(.+)""".r
    input
      .map { case pattern(varName, opType, opValue, condition, _*) =>
        Expr(Variable(varName), Operation(opType, opValue.toInt), Condition(condition))
      }
  }

  def compileClass(expressions: List[Expr]): Class[_] = {
    val str = s"""
                 |class AdventSolution {
                 |  var max = 0
                 |${expressions.map(_.variable).distinct.mkString(System.lineSeparator())}
                 |
                 |  def run(): (Int, Int) = {
                 |${expressions.map(_.toString).mkString(System.lineSeparator())}
                 |    List(${expressions.map(_.variable.name).distinct.mkString(", ")}).max -> max
                 |  }
                 |}
                 |scala.reflect.classTag[AdventSolution].runtimeClass
               """.stripMargin

    toolBox.eval(toolBox.parse(str)).asInstanceOf[Class[_]]
  }

  def run(clazz: Class[_]): (Int, Int) = {
    val instance = clazz.getConstructors()(0).newInstance()

    clazz.getMethod("run").invoke(instance).asInstanceOf[(Int, Int)]
  }

  run(compileClass(getExpressions))
}

execute(input)
execute("day8-input.txt".linesFromResource(getClass.getClassLoader))