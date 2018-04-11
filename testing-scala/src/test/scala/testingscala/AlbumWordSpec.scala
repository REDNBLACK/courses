package testingscala

import org.scalatest.{Matchers, WordSpec}

class AlbumWordSpec extends WordSpec with Matchers {
  "An Album" when {
    "created" should {
      "accept the title, the year, and a Band as a parameter, and be able to read those parameters back" in {
        new Album("Hotel California", 1977, new Band("The Eagles",
            new Artist("Don", "Henley"),
            new Artist("Glenn", "Frey"),
            new Artist("Joe", "Walsh"),
            new Artist("Randy", "Meisner"),
            new Artist("Don", "Felder")
        ))
      }

      "throw an IllegalArgumentException if there are no acts when created" in {
        intercept[IllegalArgumentException] {
          new Album("The Joy of Listening to Nothing", 1980, List(): _*)
        }
      }
    }
  }
}

