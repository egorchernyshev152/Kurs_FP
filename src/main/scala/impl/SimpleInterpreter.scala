package impl // объявление пакета, где находится класс

import cats.Monad // импорт typeclass Monad для обёртывания результатов в абстрактный эффект F
import cats.syntax.functor._ // импорт синтаксиса для Functor (метод map)
import core.RecommendationAlg // импорт трейта с абстрактными методами рекомендаций
import model._ // импорт доменных моделей: Student, Topic, Conference, Professor, Article, Database
import scala.collection.immutable.LazyList // импорт ленивого списка для отложенных вычислений

/**
  * Простая реализация интерфейса
  */
class SimpleInterpreter[F[_]: Monad](db: Database) extends RecommendationAlg[F] {
  // конструктор класса принимает Database с данными и требует наличия Monad[F]

  private val synonyms: Map[String, Set[String]] = Map( // карта коротких тегов к их синонимам
    "ml" -> Set("machine learning"), // тег "ml" соответствует полному "machine learning"
    "cv" -> Set("computer vision"), // тег "cv" соответствует "computer vision"
    "ai" -> Set("artificial intelligence") // тег "ai" соответствует "artificial intelligence"
  )

  /** Преобразует тег в множество нормализованных значений */
  private def normalize(tag: String): Set[String] = {
    val low = tag.toLowerCase.trim // переводим тег в нижний регистр и обрезаем пробелы
    synonyms
      .find { case (k, syns) => k == low || syns.contains(low) } // ищем в карте, совпадает ли ключ или одно из значений
      .map { case (k, syns) => syns + k } // если нашли, возвращаем все синонимы + сам ключ
      .getOrElse(Set(low)) // иначе возвращаем множество из самого тега
  }

  /** Проверяет, соответствует ли интерес студента тегу */
  private def matches(interest: String, tag: String): Boolean =
    normalize(interest).contains(tag.toLowerCase.trim) // нормализуем интерес и проверяем наличие тега

  /** Возвращает максимальную допустимую сложность материалов по курсу */
  private def maxDifficultyFor(course: Int): Int =
    if (course <= 2) 2 // для 1–2 курса допускается сложность до 2
    else 3 // для 3–4 курса допускается сложность до 3

  /**
    * Рекомендует темы:
    * - конвертирует список тем в ленивый список
    * - фильтрует по наличию пересечения тегов и интересов студента
    * - оборачивает результат в F
    */
  override def suggestTopics(student: Student): F[LazyList[Topic]] =
    Monad[F].pure( // создаём эффект, содержащий результат
      db.topics.to(LazyList) // конвертирую List[Topic] в LazyList[Topic]
        .filter(t => student.interests.exists(i => t.tags.exists(matches(i, _)))) // фильтр по тегам
    )

  /**
    * Рекомендует конференции:
    * - фильтрует по тегам интересов
    * - фильтрует по сложности
    * - сортирует по убыванию рейтинга
    */
  override def suggestConferences(student: Student): F[LazyList[Conference]] = {
    val md = maxDifficultyFor(student.course) // вычисляем порог сложности
    Monad[F].pure(
      db.conferences.to(LazyList) // LazyList всех конференций
        .filter(c => student.interests.exists(i => c.topics.exists(matches(i, _)))) // фильтр по тегам
        .filter(_.difficulty <= md) // фильтр по сложности
        .sortBy(c => -c.rating) // сортировка по рейтингу (от большего к меньшему)
    )
  }

  /**
    * Рекомендует преподавателей:
    * - выбирает только доступных
    * - фильтрует по областям интересов
    * - сортирует по hIndex: новичкам по возрастанию, старшекурсникам по убыванию
    */
  override def suggestProfessors(student: Student): F[LazyList[Professor]] = {
    val base = db.professors.to(LazyList) // LazyList всех преподавателей
      .filter(_.available) // только доступные
      .filter(p => student.interests.exists(i => p.areas.exists(matches(i, _)))) // фильтр по областям

    val sorted =
      if (student.course <= 2) base.sortBy(_.hIndex) // для курсов 1–2: низкий hIndex сперва
      else base.sortBy(p => -p.hIndex) // для курсов 3–4: высокий hIndex сперва

    Monad[F].pure(sorted) // оборачиваем отсортированный список в эффект
  }

  /**
    * Рекомендует статьи:
    * - фильтрует по тегам и сложности
    * - сортирует по году публикации (новые первыми)
    */
  override def suggestArticles(student: Student): F[LazyList[Article]] = {
    val md = maxDifficultyFor(student.course) // порог сложности
    Monad[F].pure(
      db.articles.to(LazyList) // LazyList всех статей
        .filter(a => student.interests.exists(i => matches(i, a.topic))) // фильтр по тегам
        .filter(_.difficulty <= md) // фильтр по сложности
        .sortBy(a => -a.year) // сортировка по году (от нового к старому)
    )
  }
}
