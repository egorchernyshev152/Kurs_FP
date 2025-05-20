package service

import cats.effect.IO

/**
  * Интерфейс репозитория для чтения (load) и записи (save) сущностей.
  * Тип A — тип сущности (Topic, Conference, Professor, Article).
  */
trait Repository[A] {
  def load(resource: String): IO[List[A]]
  def save(resource: String, items: List[A]): IO[Unit]
}
