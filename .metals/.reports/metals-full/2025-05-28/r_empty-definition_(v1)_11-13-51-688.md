error id: file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala:
file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 1542
uri: file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
text:
```scala
import cats.effect.{IO, IOApp, ExitCode, Ref}
import cats.syntax.all._
import model.{Role, Student, Topic, Conference, Professor, Article, Database}
import core.RecommendationAlg
import impl.SimpleInterpreter
import service.{DataLoader, CsvRepository, ExportService, Repository}
import ui.ConsoleUI
import scala.io.StdIn

/**
  * Обёртка для форматированного вывода.
  */
object Formatter {
  def formatProfessor(p: Professor): List[String] = List(
    s"Преподаватель       : ${p.name}",
    s"Области             : ${p.areas.mkString(", ")}",
    s"Индекс крутости     : ${p.hIndex}",
    s"Доступен            : ${if (p.available) "да" else "нет"}"
  )
  def formatTopic(t: Topic): String =
    s"Тема                : ${t.name} (теги: ${t.tags.mkString(", ")})"
  def formatConference(c: Conference): List[String] = List(
    s"Конференция         : ${c.name}",
    s"Рейтинг             : ${c.rating}",
    s"Темы                : ${c.topics.mkString(", ")}"
  )
  def formatArticle(a: Article): List[String] = List(
    s"Статья              : ${a.title}",
    s"Тема                : ${a.topic}",
    s"Бла бла             : ${a.doi.getOrElse("—")}",
    s"Год публикации      : ${a.year}"
  )
}

/**
  * Admin-меню: всякое с преподавателями.
  */
object AdminMenu {
  import ui.ConsoleUI._
  import Formatter._

  def run(profRef: Ref[IO, List[Professor]], profRepo: Repository[Professor]): IO[Unit] = {

    def loop: IO[Unit] = for {
      cmd <- readValid("Админ-меню:\n1) Добавить  \n2) Удалить  \n3) Показать всех  \n4) Выйти\nВы@@берите (1-4):") {
               case s @ ("1"|"2"|"3"|"4") => Right(s); case _ => Left("Ожидается 1 - 4")
             }
      _ <- cmd match {
        case "1" =>
          for {
            name   <- readName()
            areasS <- prompt("Области (через ';'):")
            hIndex <- readValid("Индекс крутости препода (целое число):")(s => s.toIntOption.toRight("Не число"))
            avail  <- readValid("Доступен? (y/n):") {
                        case "y"|"Y" => Right(true); case "n"|"N" => Right(false)
                        case _       => Left("Введите y или n")
                      }
            prof    = Professor(name, areasS.split(";").toList, hIndex, avail)
            _      <- profRef.update(_ :+ prof)
            all    <- profRef.get
            _      <- profRepo.save("professors.csv", all)
            _      <- IO.println(s"Добавлен: ${prof.name}")
            _      <- loop
          } yield ()

        case "2" =>
          for {
            name <- prompt("Имя для удаления:")
            _    <- profRef.update(_.filterNot(_.name == name))
            all  <- profRef.get
            _    <- profRepo.save("professors.csv", all)
            _    <- IO.println(s"Удалён: $name")
            _    <- loop
          } yield ()

        case "3" =>
          for {
            all <- profRef.get
            _   <- IO.println("=== Преподаватели ===")
            _   <- all.traverse_(p => formatProfessor(p).traverse_(IO.println) >> IO.println("----------------"))
            _   <- loop
          } yield ()

        case "4" =>
          IO.println("Выход из режима администратора.")
      }
    } yield ()

    loop
  }
}

/**
  * Точка входа приложения.
  */
object MainApp extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {

    // -------------------------------------------------------------------------
    // 1) Создаем репозитории для CSV
    // -------------------------------------------------------------------------
    val profRepo: Repository[Professor] = new CsvRepository[Professor](
      // parse: [name, areas..., hIndex, available] => Professor
      parse  = {
        case List(n, a, h, av) =>
          for {
            hi  <- h.toIntOption
            avl <- av.toLowerCase match {
                     case "y" => Some(true)
                     case "n" => Some(false)
                     case _   => None
                   }
          } yield Professor(n, a.split(";").toList, hi, avl)
        case _ => None
      },
      // format: Professor => List[String]
      format = p => List(p.name, p.areas.mkString(";"), p.hIndex.toString, if (p.available) "y" else "n")
    )

    val confRepo: Repository[Conference] = new CsvRepository[Conference](
      parse  = {
        case List(n, r, ts, d) =>
          for {
            rd   <- r.toDoubleOption
            diff <- d.toIntOption
          } yield Conference(n, rd, ts.split(";").toList, diff)
        case _ => None
      },
      format = c => List(c.name, c.rating.toString, c.topics.mkString(";"), c.difficulty.toString)
    )

    val topRepo: Repository[Topic] = new CsvRepository[Topic](
      parse  = { case List(n, ts) => Some(Topic(n, ts.split(";").toList)); case _ => None },
      format = t => List(t.name, t.tags.mkString(";"))
    )

    val artRepo: Repository[Article] = new CsvRepository[Article](
      parse  = {
        case List(tl, tp, doi, y, d) =>
          for {
            yi   <- y.toIntOption
            diff <- d.toIntOption
          } yield Article(tl, tp, if (doi.nonEmpty) Some(doi) else None, yi, diff)
        case _ => None
      },
      format = a => List(a.title, a.topic, a.doi.getOrElse(""), a.year.toString, a.difficulty.toString)
    )

    // -------------------------------------------------------------------------
    // 2) Загружаем всю базу данных
    // -------------------------------------------------------------------------
    val loader = new DataLoader(profRepo, confRepo, topRepo, artRepo)

    // -------------------------------------------------------------------------
    // 3) Главный цикл: выбор роли → команды → продолжить/выход
    // -------------------------------------------------------------------------
    def loop(db: Database, profRef: Ref[IO, List[Professor]]): IO[ExitCode] = for {
      _    <- IO.println("\n=== Система поддержки публикационной активности ===")
      _    <- IO.println("\nПредполагается, что ученик может найти статьти, концеренции и профессоров, чтобы найти идею для научной работы.")

      role <- ConsoleUI.readValid("Введите роль (admin/student):")(Role.fromString)
      _    <- IO.println(s"Вы выбрали: $role")

      // Ветвление по роли
      _    <- role match {
                case Role.Admin   => AdminMenu.run(profRef, profRepo)
                case Role.Student => studentFlow(db, profRef)
              }

      // Спрашиваем, хотим ли повторить
      cont <- ConsoleUI.readContinue()
      next <- if (cont) loop(db, profRef) else IO.pure(ExitCode.Success)
    } yield next

    // -------------------------------------------------------------------------
    // Запуск
    // -------------------------------------------------------------------------
    for {
      db      <- loader.loadAll()
      profRef <- Ref.of[IO, List[Professor]](db.professors)
      exit    <- loop(db, profRef)
    } yield exit
  }

  /**
    * Логика работы в режиме студента.
    */
  def studentFlow(db: Database, profRef: Ref[IO, List[Professor]]): IO[Unit] = for {
    // Читаем данные студента
    name      <- ConsoleUI.readName()
    course    <- ConsoleUI.readCourse()
    interests <- ConsoleUI.readInterests(db.topics.map(_.name))
    _         <- IO.println(s"Студент: $name, курс $course, интересы: ${interests.mkString(", ")}")

    // Читаем пороги фильтрации
    minRating <- ConsoleUI.readMinRating()
    minHidx   <- ConsoleUI.readMinHidx()
    minYear   <- ConsoleUI.readMinYear()

    // Получаем необработанные рекомендации
    student   = Student(name, course, interests)
    tps0      <- new SimpleInterpreter[IO](db).suggestTopics(student)
    cfs0      <- new SimpleInterpreter[IO](db).suggestConferences(student)
    prs0      <- profRef.get.flatMap(lst =>
                   new SimpleInterpreter[IO](db.copy(professors = lst)).suggestProfessors(student)
                 )
    arts0     <- new SimpleInterpreter[IO](db).suggestArticles(student)

    // Применяем пороги
    cfs       = cfs0.filter(_.rating >= minRating)
    prs       = prs0.filter(_.hIndex >= minHidx)
    arts      = arts0.filter(_.year   >= minYear)

    // Выводим результат
    _ <- IO.println("\n--- Рекомендованные темы ---")
    _ <- tps0.toList.traverse_(t => IO.println(Formatter.formatTopic(t)))

    _ <- IO.println("\n--- Рекомендованные конференции ---")
    _ <- cfs.toList.traverse_(c => Formatter.formatConference(c).traverse_(IO.println) >> IO.println("-----"))

    _ <- IO.println("\n--- Рекомендованные преподаватели ---")
    _ <- prs.toList.traverse_(p => Formatter.formatProfessor(p).traverse_(IO.println) >> IO.println("-----"))

    _ <- IO.println("\n--- Рекомендованные статьи ---")
    _ <- arts.toList.traverse_(a => Formatter.formatArticle(a).traverse_(IO.println) >> IO.println("-----"))

    // Экспорт
    _   <- IO.println("Экспорт результатов в (csv/pdf)? Если не хотите - ентер.")
    fmt <- IO(StdIn.readLine().trim.toLowerCase)
    _   <- fmt match {
             case "csv" => ExportService.exportCsv("recs.csv", student,
                               (tps0.toList, cfs.toList, prs.toList, arts.toList))
             case "pdf" => ExportService.exportPdf("recs.pdf", student,
                               (tps0.toList, cfs.toList, prs.toList, arts.toList))
             case _     => IO.println("Неверный формат экспорта")
           }
  } yield ()
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 