package org.functionalkoans.forscala.support

import org.scalatest.exceptions.TestPendingException
import org.scalatest._
import org.scalatest.matchers.Matcher
import org.scalatest.events.{Event, TestFailed, TestIgnored, TestPending}

trait KoanSuite extends FunSuite with Matchers {
  def koan(name : String)(fun: => Unit) { test(name.stripMargin('|'))(fun) }

  def meditate() = pending

  def  __ : Matcher[Any] = {
    throw new TestPendingException
  }

  protected class ___ extends Exception {
    override def toString = "___"
  }

  private class ReportToTheMaster(other: Reporter) extends Reporter {
    var failed = false
    def failure(event: Master.HasTestNameAndSuiteName) {
      failed = true
      println("*****************************************")
      println("*****************************************")
      println()
      println()
      println()
      println(Master.studentFailed(event))
      println()
      println()
      println()
      println("*****************************************")
      println("*****************************************")
    }

    def apply(event: Event): Unit = event match {
      case e: TestIgnored => failure(e.asInstanceOf[Master.HasTestNameAndSuiteName])
      case e: TestFailed => failure(e.asInstanceOf[Master.HasTestNameAndSuiteName])
      case e: TestPending => failure(e.asInstanceOf[Master.HasTestNameAndSuiteName])
      case e => other(e)
    }
  }

  protected override def runTest(testName: String, args: Args) = {
    if (!Master.studentNeedsToMeditate) {
      super.runTest(testName, args.copy(reporter = new ReportToTheMaster(args.reporter)))
    }
    SucceededStatus
  }
}
