package ui

import cats.effect.IO
import  cats.implicits._
import scala.io.StdIn

/**
  * Утилиты для безопасного чтения ввода в консоли.
  * Prompt + валидация пользовательского ввода.
  */
object ConsoleUI {

  /** Выводит msg и читает одну строку, обрезает пробелы */
  def prompt(msg: String): IO[String] =
    IO { println(msg); StdIn.readLine().trim }

  /** Повторяет prompt+parse, пока parse не вернёт Right */
  def readValid[A](msg: String)(parse: String => Either[String, A]): IO[A] = {
    def loop: IO[A] = for {
      in  <- prompt(msg)
      res =  parse(in)
      out <- res match {
               case Right(v) => IO.pure(v)
               case Left(err)=> IO { println(s"Ошибка: $err") } >> loop
             }
    } yield out
    loop
  }

  // Чтение имени студента (не пустое)
  def readName(): IO[String] =
    readValid("Введите имя студента:") { s =>
      if (s.nonEmpty) Right(s) else Left("Имя не должно быть пустым")
    }

  // Чтение курса (целое 1–4)
  def readCourse(): IO[Int] =
    readValid("Введите курс (1-4):") { s =>
      s.toIntOption.filter(c => c >= 1 && c <= 4)
        .toRight("Курс должен быть числом от 1 до 4")
    }

  // Выбор интересов из списка тем: показываем номера и читаем "1,3,5"
  def readInterests(topics: List[String]): IO[List[String]] = for {
    _   <- IO { println("Доступные темы:"); topics.zipWithIndex.foreach { case (t,i) =>
             println(s"${i+1}) $t") } }
    sel <- readValid("Выберите номера тем через запятую (например, 1,3,5):") { in =>
             val parts = in.split(",").toList
             parts.traverse { s =>
               s.trim.toIntOption.toRight("Номера должны быть числами")
             }.flatMap { nums =>
               val max = topics.length
               if (nums.forall(n => n>=1 && n<=max))
                 Right(nums.distinct.map(n => topics(n-1)))
               else Left(s"Номера должны быть в диапазоне 1 - $max")
             }
           }
  } yield sel

  // Чтение минимального рейтинга конференций (double 0.0–10.0)
  def readMinRating(): IO[Double] =
    readValid("Минимальный рейтинг конференций (0.0 - 10.0):") { s =>
      s.toDoubleOption.filter(r => r>=0.0 && r<=10.0)
        .toRight("Введите число от 0.0 до 10.0")
    }

  // Чтение минимального h-индекса (int ≥ 0)
  def readMinHidx(): IO[Int] =
    readValid("Минимальный индекс крутости преподавателя:") { s =>
      s.toIntOption.filter(_>=0)
        .toRight("Введите неотрицательное целое")
    }

  // Чтение минимального года публикации 
  def readMinYear(): IO[Int] = {
    val current = java.time.Year.now.getValue
    readValid(s"Статьи не старше какого года? :") { s =>
      s.toIntOption.filter(y => y>=1900 && y<=current)
        .toRight(s"ИЗВИНИТЭС $current")
    }
  }

  // Чтение «продолжить? (y/n)»
  def readContinue(): IO[Boolean] =
    readValid("Продолжить? (y/n):") {
      case s if Set("y","yes").contains(s.toLowerCase) => Right(true)
      case s if Set("n","no").contains(s.toLowerCase)  => Right(false)
      case _                                           => Left("Введите y или n")
    }
}
