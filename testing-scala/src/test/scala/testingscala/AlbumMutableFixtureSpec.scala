package testingscala

import scala.collection.mutable._
import org.scalatest.{FunSpec, Matchers}

class AlbumMutableFixtureSpec extends FunSpec with Matchers {
  def fixture = new {
    val beachBoys = new Band("Beach Boys")
    val beachBoysDiscography = new ListBuffer[Album]()
    beachBoysDiscography += new Album("Surfin' Safari", 1962, beachBoys)
  }

  describe("Given a single fixture each beach boy discography initially contains a single album") {
    it("then after 1 album is added, the discography size should have 2") {
      val discographyDeBeachBoys = fixture.beachBoysDiscography
      discographyDeBeachBoys += new Album("Surfin' Safari", 1962, fixture.beachBoys)
      discographyDeBeachBoys.size should be(2)
    }

    it("then after 2 albums are added, the discography size should return 3") {
      val discographyDeBeachBoys = fixture.beachBoysDiscography
      discographyDeBeachBoys += new Album("Surfin' Safari", 1962, fixture.beachBoys)
      discographyDeBeachBoys += new Album("All Summer Long", 1964, fixture.beachBoys)
      discographyDeBeachBoys.size should be(3)
    }
  }
}