error id: file:///C:/Users/toorr/v1/src/main/scala/model/Models.scala:
file:///C:/Users/toorr/v1/src/main/scala/model/Models.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 699
uri: file:///C:/Users/toorr/v1/src/main/scala/model/Models.scala
text:
```scala
package model

/**
 * Доменные модели приложения:
 * Student      - студент (имя, курс, интересы, наличие публикаций)
 * Topic        - тема исследования (имя, теги)
 * Conference   - конференция (имя, рейтинг, темы)
 * Professor    - преподаватель (имя, области, h-index, доступность)
 * Article      - статья (заголовок, тема, DOI, год)
 * Database     - база данных (список тем, конференций, преподавателей, статей)
 */
final case class Student(
  name: String,
  course: Int,
  interests: List[String],
  hasPublications: Boolean
)

final case class Topic(
  name: String,
  tags: List[String]
)

final case class Conference(
  name: String,
  rating: Double,
  topics: List[String]
)

final cas@@e class Professor(
  name: String,
  areas: List[String],
  hIndex: Int,
  available: Boolean
)

final case class Article(
  title: String,
  topic: String,
  doi: Option[String],
  year: Int
)

final case class Database(
  topics: List[Topic],
  conferences: List[Conference],
  professors: List[Professor],
  articles: List[Article]
)
sealed trait Role
object Role {
  case object Admin   extends Role
  case object Student extends Role

  def fromString(s: String): Either[String, Role] = s.toLowerCase match {
    case "admin"   => Right(Admin)
    case "student" => Right(Student)
    case other     => Left(s"Unknown role '$other'; expected admin or student")
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 