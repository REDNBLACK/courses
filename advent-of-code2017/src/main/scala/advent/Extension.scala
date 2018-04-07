package advent

import scala.io.Source

object Extension {
  implicit class ExtendedString(val value: String) extends AnyVal {
    def linesAll: List[String] = value.lines.map(_.trim).filter(!_.isEmpty).toList
    def linesFromResource(cl: ClassLoader): List[String] = Source.fromResource(value, cl)
      .getLines()
      .map(_.trim)
      .filter(!_.isEmpty)
      .toList
  }
}
