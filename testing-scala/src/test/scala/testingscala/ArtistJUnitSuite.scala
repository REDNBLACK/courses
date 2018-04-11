package testingscala

import org.scalatest.junit.JUnitSuite
import org.junit.{After, Before, Test}
import org.junit.Assert._

class ArtistJUnitSuite extends JUnitSuite {
  var artist: Artist = _

  @Before
  def startUp() {
    artist = new Artist("Kenny", "Rogers")
  }

  @After
  def shutDown() {
    artist = null
  }

  @Test
  def addOneAlbumAndGetCopy() {
//    val copyArtist = artist.addAlbum(new Album("Love will turn you around", 1982, artist))
//    assertEquals(copyArtist.albums.size, 1)
  }

  @Test
  def addTwoAlbumsAndGetCopy() {
//    val copyArtist = artist
//      .addAlbum(new Album("Love will turn you around", 1982, artist))
//      .addAlbum(new Album("We've got tonight", 1983, artist))
//    assertEquals(copyArtist.albums.size, 2)
  }
}
