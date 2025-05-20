package impl

import cats.Monad
import cats.syntax.functor._
import core.RecommendationAlg
import model._
import scala.collection.immutable.LazyList

/**
  * Простая реализация RecommendationAlg на базе списка
  * и функций из Cats (Monad[F].pure).
  */
class SimpleInterpreter[F[_]: Monad](db: Database) extends RecommendationAlg[F] {

  // Словарь синонимов для нормализации тегов
  private val synonyms: Map[String, Set[String]] = Map(
    "ml" -> Set("machine learning"),
    "cv" -> Set("computer vision"),
    "ai" -> Set("artificial intelligence")
  )

  /** Приводим тег к множеству нормализованных значений */
  private def normalize(tag: String): Set[String] = {
    val low = tag.toLowerCase.trim
    synonyms
      .find { case (k, syns) => k == low || syns.contains(low) }
      .map { case (k, syns) => syns + k }
      .getOrElse(Set(low))
  }

  /** Соответствие интереса студента и тега материала */
  private def matches(interest: String, tag: String): Boolean =
    normalize(interest).contains(tag.toLowerCase.trim)

  /** 
    * Градация сложности материалов по курсу:
    * курсы 1–2: сложность ≤ 2,
    * курсы 3–4: сложность ≤ 3 (максимум).
    */
  private def maxDifficultyFor(course: Int): Int =
    if (course <= 2) 2 else 3

  /** Рекомендуем темы: просто все, где есть пересечение тегов */
  override def suggestTopics(student: Student): F[LazyList[Topic]] =
    Monad[F].pure(
      db.topics.to(LazyList)
        .filter(t => student.interests.exists(i => t.tags.exists(matches(i, _))))
    )

  /** Рекомендуем конференции по тегам + сложности + сортируем по рейтингу */
  override def suggestConferences(student: Student): F[LazyList[Conference]] = {
    val md = maxDifficultyFor(student.course)
    Monad[F].pure(
      db.conferences.to(LazyList)
        .filter(c => student.interests.exists(i => c.topics.exists(matches(i, _))))
        .filter(_.difficulty <= md)
        .sortBy(c => -c.rating)
    )
  }

  /** Рекомендуем преподавателей по области + доступности + сортировка по hIndex */
  override def suggestProfessors(student: Student): F[LazyList[Professor]] = {
    val base = db.professors.to(LazyList)
      .filter(_.available)
      .filter(p => student.interests.exists(i => p.areas.exists(matches(i, _))))

    val sorted =
      if (student.course <= 2) base.sortBy(_.hIndex)      // новичкам — низкий hIndex
      else base.sortBy(p => -p.hIndex)                    // старшекурсникам — высокий

    Monad[F].pure(sorted)
  }

  /** Рекомендуем статьи по тегам + сложности + сортировка по году (самые новые выше) */
  override def suggestArticles(student: Student): F[LazyList[Article]] = {
    val md = maxDifficultyFor(student.course)
    Monad[F].pure(
      db.articles.to(LazyList)
        .filter(a => student.interests.exists(matches(_, a.topic)))
        .filter(_.difficulty <= md)
        .sortBy(a => -a.year)
    )
  }
}
