package atto
package syntax

import scala.language.implicitConversions
import scalaz.syntax.Ops
import scalaz.\/
import atto.parser._

trait ParserOps[A] extends Ops[Parser[A]] {

  // Parsing

  def parse(b: String): ParseResult[A] = 
    Parser.parse(self, b)

  def parseOnly(b: String): ParseResult[A] = 
    Parser.parseOnly(self, b)

  // Combinator

  def ~>[B](n: Parser[B]): Parser[B] =
    combinator.discardLeft(self, n)

  def <~[B](n: Parser[B]): Parser[A] =
    combinator.discardRight(self, n)

  def ~[B](n: Parser[B]): Parser[(A, B)] =
    combinator.andThen(self, n)

  def |[B >: A](n: Parser[B]): Parser[B] =
    combinator.orElse(self, n)

  def ||[B](n: Parser[B]): Parser[\/[A, B]] =
    combinator.either(self, n)

  def ^^[B](f: A => B): Parser[B] =
    self map f

  def ^^^[B](b: => B): Parser[B] =
    self map (_ => b)

  /** Treat this parser as a token, followed by any amount of whitespace. */
  def t: Parser[A] = 
    this <~ combinator.many(character.spaceChar)

  // Other

  def as(s: => String): Parser[A] = 
    combinator.named(self, s)

  def asOpaque(s: => String): Parser[A] = 
    combinator.namedOpaque(self, s)

  def collect[B](pf: PartialFunction[A,B]): Parser[B] =
    combinator.collect(self, pf)

}

trait ToParserOps {

  implicit def toParserOps[A](p: Parser[A]): ParserOps[A] =
    new ParserOps[A] {
      val self = p
    }

}