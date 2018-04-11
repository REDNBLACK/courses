package testingscala

class Album(val title: String, val year: Int, val tracks: Option[List[Track]], val acts: Act*)  {
  require(acts.nonEmpty)

  def this(title: String, year: Int, acts: Act*) = this(title, year, None, acts:_*)
}