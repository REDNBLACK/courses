package testingscala

import org.scalatest.{FunSpec, GivenWhenThen, Matchers, Tag}

class AlbumSpecAll extends FunSpec with Matchers with GivenWhenThen {
  describe("An Album") {
    it("can add an Artist to the album at construction time", Tag("construction")) {
      Given("The album Thriller by Michael Jackson")
      val album = new Album("Thriller", 1981, new Artist("Michael", "Jackson"))

      When("the artist of the album is obtained")
      val artist = album.acts.head

      Then("the artist should be an instance of Artist")
      artist.isInstanceOf[Artist] should be(true)

      And("the artist's first name and last name should be Michael Jackson")
      artist.asInstanceOf[Artist].firstName should be("Michael")
      artist.asInstanceOf[Artist].lastName should be("Jackson")
      info("This is still pending, since there may be more to accomplish in this test")
      pending
    }

    it("can add opt to not have any artists at construction time") {pending}

    ignore("can add a Producer to an album at construction time") {
      new Album("Breezin\'", 1976, new Artist("George", "Benson"))
      //TODO: Figure out the implementation of an album producer
    }
  }
}
