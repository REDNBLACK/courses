package testingscala

import org.scalatest.{FunSpec, Matchers}

class AlbumFixtureSpec extends FunSpec with Matchers {
  def fixture = new {
    val letterFromHome = new Album("Letter from Home", 1989, new Band("Pat Metheny Group"))
  }

  describe("The Letter From Home Album by Pat Metheny") {
    it("should get the year 1989 from the album") {
      val album = fixture.letterFromHome
      album.year should be (1989)
    }
  }
}
