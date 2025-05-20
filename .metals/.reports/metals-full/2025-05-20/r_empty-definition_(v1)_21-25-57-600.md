error id: file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala:cats/effect/IO.println().
file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
empty definition using pc, found symbol in pc: 
found definition using semanticdb; symbol cats/effect/IO.println().
empty definition using fallback
non-local guesses:

offset: 6900
uri: file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
text:
```scala
import cats.effect.{IO, IOApp, ExitCode, Ref}
import cats.syntax.all._
import core.RecommendationAlg
import impl.SimpleInterpreter
import model._
import service._
import ui.ConsoleUI
import scala.io.StdIn

object InputHelper {
  def readUntilValid[A](msg: String)(parse: String => Either[String, A]): IO[A] = for {
    _   <- IO.println(msg)
    in  <- IO(StdIn.readLine().trim)
    res =  parse(in)
    out <- res.fold(err => IO.println(s"Ошибка: $err") >> readUntilValid(msg)(parse), IO.pure)
  } yield out

  val readRole: IO[Role] = readUntilValid("Введите роль (admin/student):")(Role.fromString)
  val readContinue: IO[Boolean] = readUntilValid("Продолжить? (y/n):") {
    case s if Set("y","yes").contains(s.toLowerCase) => Right(true)
    case s if Set("n","no").contains(s.toLowerCase)  => Right(false)
    case _ => Left("Введите y или n")
  }
  def readStudentLoop: IO[Student] = for {
    either <- ConsoleUI.readStudent[IO].value
    s      <- either.fold(err => IO.println(s"Ошибка: $err") >> readStudentLoop, IO.pure)
  } yield s
}

object Formatter {
  def formatProfessor(p: Professor): List[String] = List(
    s"Профессор           : ${p.name}",
    s"Области             : ${p.areas.mkString(", ")}",
    s"Индекс цитируемости : ${p.hIndex}",
    s"Доступен            : ${if (p.available) "да" else "нет"}"
  )
  def formatTopic(t: Topic): String =
    s"• Тема              : ${t.name} (теги: ${t.tags.mkString(", ")})"
  def formatConference(c: Conference): List[String] = List(
    s"Конференция         : ${c.name}",
    s"Рейтинг             : ${c.rating}",
    s"Темы                : ${c.topics.mkString(", ")}"
  )
  def formatArticle(a: Article): List[String] = List(
    s"Статья              : ${a.title}",
    s"Тема                : ${a.topic}",
    s"DOI                 : ${a.doi.getOrElse("—")}",
    s"Год публикации      : ${a.year}"
  )
}

object AdminMenu {
  import InputHelper._
  import Formatter._

  def run(profRef: Ref[IO, List[Professor]], profRepo: Repository[Professor]): IO[Unit] = {
    def menu: IO[Unit] = for {
      cmd <- readUntilValid(
               "Админ-меню:\n1) Добавить\n2) Удалить\n3) Показать всех\n4) Выйти\nВыберите (1-4):"
             ) {
               case s @ ("1"|"2"|"3"|"4") => Right(s)
               case _ => Left("Ожидалось 1,2,3 или 4")
             }
      _ <- cmd match {
        case "1" => for {
          name   <- readUntilValid("Имя преподавателя:")(s => Either.cond(s.nonEmpty, s, "Не пусто"))
          areasS <- readUntilValid("Области (через ';'):")(s => Either.cond(s.nonEmpty, s, "Не пусто"))
          hIndex <- readUntilValid("hIndex (число):")(s => s.toIntOption.toRight("Не число"))
          avail  <- readUntilValid("Доступен? (y/n):"){ 
                       case "y"|"Y" => Right(true); case "n"|"N" => Right(false); case _ => Left("y или n") }
          prof    = Professor(name, areasS.split(";").map(_.trim).toList, hIndex, avail)
          _      <- profRef.update(_ :+ prof)
          list   <- profRef.get
          _      <- profRepo.save("professors.csv", list)
          _      <- IO.println(s"Добавлен: ${prof.name}")
          _      <- menu
        } yield ()
        case "2" => for {
          name <- readUntilValid("Имя для удаления:")(s => Either.cond(s.nonEmpty, s, "Не пусто"))
          _    <- profRef.update(_.filterNot(_.name == name))
          list <- profRef.get
          _    <- profRepo.save("professors.csv", list)
          _    <- IO.println(s"Удалён: $name")
          _    <- menu
        } yield ()
        case "3" => for {
          list <- profRef.get
          _    <- IO.println("=== Преподаватели ===")
          _    <- list.traverse_ { p =>
                    formatProfessor(p).traverse_(IO.println) >> IO.println("---------------------")
                  }
          _    <- menu
        } yield ()
        case "4" => IO.println("Выход из Admin-режима.")
      }
    } yield ()
    menu
  }
}

object MainApp extends IOApp {
  import InputHelper._
  import Formatter._

  def run(args: List[String]): IO[ExitCode] = {
    val profRepo = new CsvRepository[Professor](
      parse  = { case List(n,a,h,av) => for { hi <- h.toIntOption; avl <- av.toLowerCase match { case "y" => Some(true); case "n" => Some(false); case _ => None } } yield Professor(n, a.split(";").toList, hi, avl); case _ => None },
      format = p => List(p.name, p.areas.mkString(";"), p.hIndex.toString, if(p.available) "y" else "n")
    )
    val confRepo = new CsvRepository[Conference](
      parse  = { case List(n,r,ts,d) => for { rd <- r.toDoubleOption; diff <- d.toIntOption } yield Conference(n, rd, ts.split(";").toList, diff); case _ => None },
      format = c => List(c.name, c.rating.toString, c.topics.mkString(";"), c.difficulty.toString)
    )
    val topRepo = new CsvRepository[Topic](
      parse  = { case List(n,t) => Some(Topic(n, t.split(";").toList)); case _ => None },
      format = t => List(t.name, t.tags.mkString(";"))
    )
    val artRepo = new CsvRepository[Article](
      parse  = { case List(tl,top,doi,y,d) => for { yi <- y.toIntOption; diff <- d.toIntOption } yield Article(tl, top, if(doi.nonEmpty) Some(doi) else None, yi, diff); case _ => None },
      format = a => List(a.title, a.topic, a.doi.getOrElse(""), a.year.toString, a.difficulty.toString)
    )

    val loader = new DataLoader(profRepo, confRepo, topRepo, artRepo)

    def appLoop(db: Database, profRef: Ref[IO, List[Professor]]): IO[Unit] = for {
      role <- readRole
      _    <- IO.println(s"Роль: $role")

      _ <- role match {
        case Role.Student => for {
  student   <- readStudentLoop
  _         <- IO.println(s"Студент: ${student.name}")
  minRating <- readUntilValid("Минимальный рейтинг конференций (0.0–10.0):") { s =>
                  s.toDoubleOption.filter(r => r >= 0.0 && r <= 10.0)
                    .toRight("Введите число от 0.0 до 10.0")
                }
  minHidx   <- readUntilValid("Минимальный h-индекс преподавателя:") { s =>
                  s.toIntOption.filter(_ >= 0)
                    .toRight("Введите неотрицательное целое")
                }

  tps0      <- new SimpleInterpreter[IO](db).suggestTopics(student)
  cfs0      <- new SimpleInterpreter[IO](db).suggestConferences(student)
  arts      <- new SimpleInterpreter[IO](db).suggestArticles(student)
  prs0      <- profRef.get.flatMap(lst =>
                 new SimpleInterpreter[IO](db.copy(professors = lst)).suggestProfessors(student))

  // Фильтруем по порогам
  val cfs = cfs0.filter(_.rating >= minRating)
  val prs = prs0.filter(_.hIndex  >= minHidx)

  // Печать как обычно
  _ <- IO.println("\n--- Темы ---")
  _ <- tps0.toList.traverse_(t => IO.println(Formatter.formatTopic(t)))

  _ <- IO.println("\n--- Конференции ---")
  _ <- cfs.toList.traverse_(c => Formatter.formatConference(c).traverse_(IO.println) >> IO.println("-----"))

  _ <- IO.println@@("\n--- Преподаватели ---")
  _ <- prs.toList.traverse_(p => Formatter.formatProfessor(p).traverse_(IO.println) >> IO.println("-----"))

  _ <- IO.println("\n--- Статьи ---")
  _ <- arts.toList.traverse_(a => Formatter.formatArticle(a).traverse_(IO.println) >> IO.println("-----"))

  _ <- IO.println("Export to (csv/pdf)?")
  fmt <- IO(StdIn.readLine().trim.toLowerCase)
  _ <- fmt match {
         case "csv" => ExportService.exportCsv("recs.csv", student, (tps0.toList, cfs.toList, prs.toList, arts.toList))
         case "pdf" => ExportService.exportPdf("recs.pdf", student, (tps0.toList, cfs.toList, prs.toList, arts.toList))
         case _     => IO.println("Unknown format")
       }
} yield ()


        case Role.Admin =>
          AdminMenu.run(profRef, profRepo)
      }

      cont <- readContinue
      _    <- if (cont) appLoop(db, profRef) else IO.unit
    } yield ()

    for {
      _       <- IO.println("Загрузка данных...")
      db      <- loader.loadAll()
      profRef <- Ref.of[IO, List[Professor]](db.professors)
      _       <- appLoop(db, profRef)
    } yield ExitCode.Success
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 