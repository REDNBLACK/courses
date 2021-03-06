package org.functionalkoans.forscala

import org.scalatest.Matchers
import support.KoanSuite

class AboutInfixTypes extends KoanSuite with Matchers {
  koan("""We can make a type infix, meaning that the type can be displayed in complement
           between two types in order to make a readable delaration""") {
    case class Person(name: String)
    class Loves[A, B](val a: A, val b: B)

    def announceCouple(couple: Person Loves Person) = {
      //Notice our type: Person loves Person!
      couple.a.name + " is in love with " + couple.b.name
    }

    val romeo = Person("Romeo")
    val juliet = Person("Juliet")

    announceCouple(new Loves(romeo, juliet)) should be("Romeo is in love with Juliet")
  }

  koan("""Of course we can make this a bit more elegant by creating an infix operator
           |  method to use with our infix type""") {
    case class Person(name: String) {
      def loves(person: Person) = new Loves(this, person)
    }

    class Loves[A, B](val a: A, val b: B)

    def announceCouple(couple: Person Loves Person) = {
      //Notice our type: Person loves Person!
      couple.a.name + " is in love with " + couple.b.name
    }

    val romeo = Person("Romeo")
    val juliet = Person("Juliet")

    announceCouple(romeo loves juliet) should be("Romeo is in love with Juliet")
  }
}