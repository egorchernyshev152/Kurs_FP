package model

/**
  * Студент с именем, курсом и списком интересов (теги тем).
  */
final case class Student(
  name: String,
  course: Int,
  interests: List[String]
)

/**
  * Тема исследования: имя + набор тегов, по которым фильтруем.
  */
final case class Topic(
  name: String,
  tags: List[String]
)

/**
  * Конференция: имя, рейтинг (0.0–10.0), 
  * список тем и сложность (1–3).
  */
final case class Conference(
  name: String,
  rating: Double,
  topics: List[String],
  difficulty: Int
)

/**
  * Преподаватель: имя, области (список тегов),
  * h-индекс и флаг доступности.
  */
final case class Professor(
  name: String,
  areas: List[String],
  hIndex: Int,
  available: Boolean
)

/**
  * Статья: заголовок, тема, DOI (опционально),
  * год издания и сложность (1–3).
  */
final case class Article(
  title: String,
  topic: String,
  doi: Option[String],
  year: Int,
  difficulty: Int
)

/**
  * Собирает всё в одну «базу данных» — удобная структура для DI.
  */
final case class Database(
  topics: List[Topic],
  conferences: List[Conference],
  professors: List[Professor],
  articles: List[Article]
)

/**
  * Роли пользователя — админ или студент.
  */
sealed trait Role
object Role {
  case object Admin   extends Role
  case object Student extends Role

  /** Преобразует ввод “admin”/“student” в Role или выдаёт ошибку. */
  def fromString(s: String): Either[String, Role] = s.toLowerCase match {
    case "admin"   => Right(Admin)
    case "student" => Right(Student)
    case other     => Left(s"Неизвестная роль '$other'; введите admin или student")
  }
}
