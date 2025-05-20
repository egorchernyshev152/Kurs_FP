package impl

import cats.Monad
import cats.syntax.functor._
import core.RecommendationAlg
import model._
import scala.collection.immutable.LazyList

/**
 * Простая реализация RecommendationAlg в функциональном стиле:
 * - Использует LazyList для ленивых вычислений
 * - Нормализация тегов через синонимы
 * - Функции matches и normalize реализованы чисто
 */
class SimpleInterpreter[F[_]: Monad](db: Database) extends RecommendationAlg[F] {
  // Словарь синонимов для тегов
  private val synonyms: Map[String, Set[String]] = Map(
    "ml"   -> Set("machine learning", "машинное обучение"),
    "cv"   -> Set("computer vision", "компьютерное зрение"),
    "ai"   -> Set("artificial intelligence", "искусственный интеллект")
  )

  // Приводим тег к множеству нормализованных версий
  private def normalize(tag: String): Set[String] = {
    val low = tag.toLowerCase.trim
    synonyms
      .find { case (k, syns) => k == low || syns.contains(low) }
      .map { case (k, syns) => syns + k }
      .getOrElse(Set(low))
  }

  // Проверяем, соответствует ли interest тегу
  private def matches(interest: String, tag: String): Boolean =
    normalize(interest).contains(tag.toLowerCase.trim)

  override def suggestTopics(student: Student): F[LazyList[Topic]] =
    Monad[F].pure(
      db.topics.to(LazyList).filter(t =>
        student.interests.exists(i => t.tags.exists(matches(i, _)))
      )
    )

  override def suggestConferences(student: Student): F[LazyList[Conference]] =
    Monad[F].pure(
      db.conferences.to(LazyList).filter(c =>
        student.interests.exists(i => c.topics.exists(matches(i, _)))
      )
    )

 override def suggestProfessors(student: Student): F[LazyList[Professor]] =
  Monad[F].pure(
    db.professors.to(LazyList).filter { p =>
      student.interests.exists(matches(_, p.areas.head /*или другая логика*/) )
    }
  )


  override def suggestArticles(student: Student): F[LazyList[Article]] =
    Monad[F].pure(
     db.articles.to(LazyList).filter(a =>
  student.interests.exists(matches(_, a.topic))
)

    )
}