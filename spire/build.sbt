name := "atto-spire"

libraryDependencies ++= Seq(
  "org.spire-math" %% "spire" % "0.7.1"
)

initialCommands :=
  """import scalaz._
     import Scalaz._
     import atto._
     import Atto._"""

