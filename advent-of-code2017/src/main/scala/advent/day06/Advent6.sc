case class MemoryBank(pos: Int, value: Int) extends Ordered[MemoryBank] {
  def inc() = MemoryBank(pos, value + 1)
  def reset() = MemoryBank(pos, 0)
  override def compare(that: MemoryBank) = value.compareTo(that.value)
}

case class Answer(first: Int, second: Int)

type MemoryBanks = List[MemoryBank]

def reallocateMemory(input: String): Answer = {
  def loop(banks: MemoryBanks) = Stream.iterate(banks) { banks =>
    val maxBank = banks.max
    val startValue = banks.updated(maxBank.pos, maxBank.reset())

    (1 to maxBank.value).foldLeft(startValue) { case (acc, offset) =>
      val bank = acc((maxBank.pos + offset) % acc.length)
      acc.updated(bank.pos, bank.inc())
    }
  }

  def find(stream: Stream[MemoryBanks], history: Set[MemoryBanks] = Set.empty): Int = stream match {
    case head #:: _ if history(head) => history.size
    case head #:: tail => find(tail, history + head)
  }

  val banks = input.split(" ").map(_.toInt).zipWithIndex.map(p => MemoryBank(p._2, p._1)).toList
  val stream = loop(banks)
  val first = find(stream)
  val second = first - stream.indexOf(stream(first))

  Answer(first, second)
}

reallocateMemory("0 2 7 0") == Answer(5, 4)
reallocateMemory("4 10 4 1 8 4 9 14 5 1 14 15 0 15 3 5")