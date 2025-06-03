error id: file:///C:/Users/toorr/v1/src/main/scala/core/RecommendationAlg.scala:core/RecommendationAlg.apply().[F]
file:///C:/Users/toorr/v1/src/main/scala/core/RecommendationAlg.scala
empty definition using pc, found symbol in pc: 
found definition using semanticdb; symbol core/RecommendationAlg.apply().[F]
empty definition using fallback
non-local guesses:

offset: 578
uri: file:///C:/Users/toorr/v1/src/main/scala/core/RecommendationAlg.scala
text:
```scala
package core

import scala.collection.immutable.LazyList
import model._

/**
  * Заходит как то Декард в бар..
  * Tagless-final интерфейс рекомендательной системы.
  * F[_] даёт возможность писать код, абстрагированный от конкретного эффекта.
  */
trait RecommendationAlg[F[_]] {
  def suggestTopics(student: Student): F[LazyList[Topic]]
  def suggestConferences(student: Student): F[LazyList[Conference]]
  def suggestProfessors(student: Student): F[LazyList[Professor]]
  def suggestArticles(student: Student): F[LazyList[Article]]
}

object RecommendationAlg {
  def apply[F@@[_]](implicit ev: RecommendationAlg[F]): RecommendationAlg[F] = ev
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 