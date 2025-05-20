package ui

import cats.effect.Sync
import cats.data.EitherT
import model.Student
import scala.io.StdIn
import model.Role

/**
 * Консольный интерфейс взаимодействия с пользователем:
 * - prompt     : общий метод запроса и валидации ввода
 * - readStudent: запрашивает данные студента и валидирует их
 */
object ConsoleUI {
  def prompt[F[_]: Sync, A](msg: String)(read: String => Either[String,A]): EitherT[F, String, A] =
    EitherT {
      Sync[F].delay {
        println(msg)
        read(StdIn.readLine()).left.map(err => s"Ошибка ввода: $err")
      }
    }

  def readStudent[F[_]: Sync]: EitherT[F, String, Student] = for {
    name  <- prompt[F, String]("Введите ваше имя:") { s => Either.cond(s.nonEmpty, s.trim, "имя не должно быть пустым") }
    course<- prompt[F, Int]   ("Введите курс (1-4):")  { s => s.toIntOption.filter(c=>c>=1 && c<=4).toRight("должно быть число от 1 до 4") }
    ints  <- prompt[F, List[String]]("Интересы (через запятую):") { s =>
                val l = s.split(",").map(_.trim).filter(_.nonEmpty).toList
                Either.cond(l.nonEmpty, l, "список интересов не должен быть пустым")
             }
    pub   <- prompt[F, Boolean]("Есть ли публикации? (y/n):") { s =>
                s.toLowerCase match {
                  case "y"|"yes" => Right(true)
                  case "n"|"no"  => Right(false)
                  case other     => Left(s"$other не распознано")
                }
             }
  } yield Student(name, course, ints, pub)
  
  def readRole[F[_]: Sync]: EitherT[F, String, Role] =
  prompt[F, Role]("Привет, тут ты сможешь подобрать рекомендации для твоих работ! " +
    " Введите роль (admin/student):") { s =>
    Role.fromString(s)
  }
}