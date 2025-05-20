error id: file:///C:/Users/toorr/v1/src/main/scala/impl/SimpleInterpreter.scala:
file:///C:/Users/toorr/v1/src/main/scala/impl/SimpleInterpreter.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 773
uri: file:///C:/Users/toorr/v1/src/main/scala/impl/SimpleInterpreter.scala
text:
```scala
package impl

import cats.Monad
import cats.syntax.functor._
import core.RecommendationAlg
import model._
import scala.collection.immutable.LazyList

class SimpleInterpreter[F[_]: Monad](db: Database) extends RecommendationAlg[F] {

  private val synonyms: Map[String, Set[String]] = Map(
    "ml" -> Set("machine learning"),
    "cv" -> Set("computer vision"),
    "ai" -> Set("artificial intelligence")
  )

  private def normalize(tag: String): Set[String] = {
    val low = tag.toLowerCase.trim
    synonyms
      .find { case (k, syns) => k == low || syns.contains(low) }
      .map { case (k, syns) => syns + k }
      .getOrElse(Set(low))
  }

  private def matches(interest: String, tag: String): Boolean =
    normalize(interest).contains(tag.toLowerCase.trim)

  @@private def maxDifficultyFor(course: Int): Int = course match {
    case 1 | 2 => 1
    case 3     => 2
    case _     => 3
  }

  override def suggestTopics(student: Student): F[LazyList[Topic]] =
    Monad[F].pure(
      db.topics.to(LazyList)
        .filter(t => student.interests.exists(i => t.tags.exists(matches(i, _))))
    )

  override def suggestConferences(student: Student): F[LazyList[Conference]] = {
    val maxDiff = maxDifficultyFor(student.course)
    Monad[F].pure(
      db.conferences.to(LazyList)
        .filter(c => student.interests.exists(i => c.topics.exists(matches(i, _))))
        .filter(_.difficulty <= maxDiff)
        .sortBy(c => -c.rating)
    )
  }

  override def suggestProfessors(student: Student): F[LazyList[Professor]] = {
    val base = db.professors.to(LazyList)
      .filter(_.available)
      .filter(p => student.interests.exists(i => p.areas.exists(matches(i, _))))
    val sorted = if (student.course <= 2)
      base.sortBy(_.hIndex)
    else
      base.sortBy(p => -p.hIndex)
    Monad[F].pure(sorted)
  }

  override def suggestArticles(student: Student): F[LazyList[Article]] = {
    val maxDiff = maxDifficultyFor(student.course)
    Monad[F].pure(
      db.articles.to(LazyList)
        .filter(a => student.interests.exists(matches(_, a.topic)))
        .filter(_.difficulty <= maxDiff)
        .sortBy(a => -a.year)
    )
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 