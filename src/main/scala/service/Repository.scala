package service

import cats.effect.IO

/** Репозиторий операций загрузки/сохранения через IO */
trait Repository[A] {
  def load(resource: String): IO[List[A]]
  def save(resource: String, items: List[A]): IO[Unit]
}
