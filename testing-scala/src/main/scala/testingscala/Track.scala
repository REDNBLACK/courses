package testingscala

import java.time.Duration

class Track(name: String, length: String) {
  require(name.trim().length() != 0, "Track name cannot be blank")

  def this(name: String) = this(name, "0:00")

  def period = {
    val Array(hours, seconds) = length.split(":")
    Duration.ofHours(hours.toInt).withSeconds(seconds.toInt)
  }
}
