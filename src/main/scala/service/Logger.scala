package service

import cats.effect.Sync

/**
 * Интерфейс логгера:
 * - info  : информационные сообщения
 * - error : сообщения об ошибках
 */
trait Logger[F[_]] {
  def info(msg: String): F[Unit]
  def error(msg: String): F[Unit]
}

object Logger {
  def console[F[_]: Sync]: Logger[F] = new Logger[F] {
    override def info(msg: String): F[Unit]  = Sync[F].delay(println(s"[INFO] $msg"))
    override def error(msg: String): F[Unit] = Sync[F].delay(println(s"[ERROR] $msg"))
  }
}