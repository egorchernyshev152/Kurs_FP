package core

import scala.collection.immutable.LazyList
import model._

/**
 * Tagless Final интерфейс рекомендательной системы:
 * F[_] - эффектный контекст (например, IO)
 * - suggestTopics       : предлагает темы по интересам студента
 * - suggestConferences  : предлагает конференции по темам
 * - suggestProfessors   : предлагает преподавателей по областям
 * - suggestArticles     : предлагает статьи по интересам
 */
trait RecommendationAlg[F[_]] {
  def suggestTopics(student: Student): F[LazyList[Topic]]
  def suggestConferences(student: Student): F[LazyList[Conference]]
  def suggestProfessors(student: Student): F[LazyList[Professor]]
  def suggestArticles(student: Student): F[LazyList[Article]]
}

object RecommendationAlg {
  def apply[F[_]](implicit ev: RecommendationAlg[F]): RecommendationAlg[F] = ev
}