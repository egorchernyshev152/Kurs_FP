file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
### scala.reflect.internal.FatalError: 
  ThisType(method readUntilValid) for sym which is not a class
     while compiling: file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.13.10
    compiler version: version 2.13.10
  reconstructed args: -classpath <WORKSPACE>\.bloop\v1\bloop-bsp-clients-classes\classes-Metals-Fbl15279Qnq7gxzk5JPu8g==;<HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.4\semanticdb-javac-0.10.4.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.10\scala-library-2.13.10.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-core_2.13\2.9.0\cats-core_2.13-2.9.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-effect_2.13\3.5.0\cats-effect_2.13-3.5.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\co\fs2\fs2-core_2.13\3.5.0\fs2-core_2.13-3.5.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\typesafe\config\1.4.2\config-1.4.2.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\ch\qos\logback\logback-classic\1.4.7\logback-classic-1.4.7.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-core_2.13\0.14.5\circe-core_2.13-0.14.5.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-generic_2.13\0.14.5\circe-generic_2.13-0.14.5.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-parser_2.13\0.14.5\circe-parser_2.13-0.14.5.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\apache\pdfbox\pdfbox\2.0.29\pdfbox-2.0.29.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-kernel_2.13\2.9.0\cats-kernel_2.13-2.9.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-effect-kernel_2.13\3.5.0\cats-effect-kernel_2.13-3.5.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-effect-std_2.13\3.5.0\cats-effect-std_2.13-3.5.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scodec\scodec-bits_2.13\1.1.34\scodec-bits_2.13-1.1.34.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\ch\qos\logback\logback-core\1.4.7\logback-core-1.4.7.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\slf4j\slf4j-api\2.0.4\slf4j-api-2.0.4.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-numbers_2.13\0.14.5\circe-numbers_2.13-0.14.5.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\chuusai\shapeless_2.13\2.3.10\shapeless_2.13-2.3.10.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-jawn_2.13\0.14.5\circe-jawn_2.13-0.14.5.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\apache\pdfbox\fontbox\2.0.29\fontbox-2.0.29.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\commons-logging\commons-logging\1.2\commons-logging-1.2.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\jawn-parser_2.13\1.4.0\jawn-parser_2.13-1.4.0.jar -Xplugin-require:semanticdb -Yrangepos -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: Ident(_CURSOR_parse)
       tree position: line 137 of file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
            tree tpe: <error>
              symbol: value <error: <none>>
   symbol definition: val <error: <none>>: <error> (a TermSymbol)
      symbol package: <empty>
       symbol owners: value <error: <none>> -> value artRepo -> method run -> object MainApp
           call site: <none> in <none>

== Source file context for tree position ==

   134     )
   135 
   136     val artRepo: Repository[Article] = new CsvRepository[IO, Article](
   137       _CURSOR_parse = {
   138         case List(tl, top, doi, y) => y.toIntOption.map(yi => Article(tl, top, if (doi.nonEmpty) Some(doi) else None, yi))
   139         case _ => None
   140       },

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.10
Classpath:
<WORKSPACE>\.bloop\v1\bloop-bsp-clients-classes\classes-Metals-Fbl15279Qnq7gxzk5JPu8g== [exists ], <HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.4\semanticdb-javac-0.10.4.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.10\scala-library-2.13.10.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-core_2.13\2.9.0\cats-core_2.13-2.9.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-effect_2.13\3.5.0\cats-effect_2.13-3.5.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\co\fs2\fs2-core_2.13\3.5.0\fs2-core_2.13-3.5.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\typesafe\config\1.4.2\config-1.4.2.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\ch\qos\logback\logback-classic\1.4.7\logback-classic-1.4.7.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-core_2.13\0.14.5\circe-core_2.13-0.14.5.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-generic_2.13\0.14.5\circe-generic_2.13-0.14.5.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-parser_2.13\0.14.5\circe-parser_2.13-0.14.5.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\apache\pdfbox\pdfbox\2.0.29\pdfbox-2.0.29.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-kernel_2.13\2.9.0\cats-kernel_2.13-2.9.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-effect-kernel_2.13\3.5.0\cats-effect-kernel_2.13-3.5.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-effect-std_2.13\3.5.0\cats-effect-std_2.13-3.5.0.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scodec\scodec-bits_2.13\1.1.34\scodec-bits_2.13-1.1.34.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\ch\qos\logback\logback-core\1.4.7\logback-core-1.4.7.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\slf4j\slf4j-api\2.0.4\slf4j-api-2.0.4.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-numbers_2.13\0.14.5\circe-numbers_2.13-0.14.5.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\chuusai\shapeless_2.13\2.3.10\shapeless_2.13-2.3.10.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-jawn_2.13\0.14.5\circe-jawn_2.13-0.14.5.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\apache\pdfbox\fontbox\2.0.29\fontbox-2.0.29.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\commons-logging\commons-logging\1.2\commons-logging-1.2.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\jawn-parser_2.13\1.4.0\jawn-parser_2.13-1.4.0.jar [exists ]
Options:
-Yrangepos -Xplugin-require:semanticdb


action parameters:
offset: 5276
uri: file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
text:
```scala
import cats.effect.{IO, IOApp, ExitCode, Ref}
import cats.syntax.all._
import core.RecommendationAlg
import impl.SimpleInterpreter
import model.{Role, Student, Topic, Conference, Professor, Article, Database}
import service.{DataLoader, CsvRepository, ExportService, Repository}
import ui.ConsoleUI
import scala.io.StdIn

/** Утилиты для повторного чтения и валидации ввода */
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

/** Форматирование вывода Professor */
object Formatter {
  def formatProfessor(p: Professor): List[String] = List(
    s"Профессор             : ${p.name}",
    s"Области               : ${p.areas.mkString(", ")}",
    s"Индекс цитируемости   : ${p.hIndex}",
    s"Доступен              : ${if (p.available) "да" else "нет"}"
  )
}

/** Меню администратора: обновляет Ref и сразу сохраняет в CSV */
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
          areasS <- readUntilValid("Области (через ';', напр.: ml;computer vision):")(s => Either.cond(s.nonEmpty, s, "Не пусто"))
          hIndex <- readUntilValid("hIndex (целое число):")(s => s.toIntOption.toRight("Не число"))
          avail  <- readUntilValid("Доступен? (y/n):") {
                       case "y"|"Y" => Right(true)
                       case "n"|"N" => Right(false)
                       case _       => Left("Введите y или n")
                     }
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
          _    <- IO.println("=== Список преподавателей ===")
          _    <- list.traverse_ { prof =>
                    formatProfessor(prof).traverse_(IO.println) >>
                    IO.println("-----------------------------")
                  }
          _    <- menu
        } yield ()

        case "4" =>
          IO.println("Выход из Admin-режима.")
      }
    } yield ()
    menu
  }
}

/** Основное приложение с меню для Admin и Student + экспортом */
object MainApp extends IOApp {
  import InputHelper._
  import Formatter._

  def run(args: List[String]): IO[ExitCode] = {
    // 1) Настройка CSV-репозиториев
    val profRepo: Repository[Professor] = new CsvRepository[IO, Professor](
      parse = {
        case List(n, areas, h, av) =>
          for {
            hi  <- h.toIntOption
            avl <- av.toLowerCase match {
                     case "y" => Some(true)
                     case "n" => Some(false)
                     case _   => None
                   }
          } yield Professor(n, areas.split(";").toList, hi, avl)
        case _ => None
      },
      format = p => List(p.name, p.areas.mkString(";"), p.hIndex.toString, if (p.available) "y" else "n")
    )

    val confRepo: Repository[Conference] = new CsvRepository[IO, Conference](
      parse = {
        case List(n, r, tops) => r.toDoubleOption.map(rd => Conference(n, rd, tops.split(";").toList))
        case _ => None
      },
      format = c => List(c.name, c.rating.toString, c.topics.mkString(";"))
    )

    val topRepo: Repository[Topic] = new CsvRepository[IO, Topic](
      parse = { case List(n, tags) => Some(Topic(n, tags.split(";").toList)); case _ => None },
      format = t => List(t.name, t.tags.mkString(";"))
    )

    val artRepo: Repository[Article] = new CsvRepository[IO, Article](
      @@parse = {
        case List(tl, top, doi, y) => y.toIntOption.map(yi => Article(tl, top, if (doi.nonEmpty) Some(doi) else None, yi))
        case _ => None
      },
      format = a => List(a.title, a.topic, a.doi.getOrElse(""), a.year.toString)
    )

    // 2) Загрузка данных
    val loader = new DataLoader[IO](profRepo, confRepo, topRepo, artRepo)

    // 3) Рекурсивный цикл
    def appLoop(db: Database, profRef: Ref[IO, List[Professor]]): IO[Unit] = for {
      role <- readRole
      _    <- IO.println(s"Роль: $role")

      _ <- role match {
        case Role.Student => for {
          student <- readStudentLoop
          _       <- IO.println(s"Студент: ${student.name}")
          tps     <- new SimpleInterpreter[IO](db).suggestTopics(student)
          cfs     <- new SimpleInterpreter[IO](db).suggestConferences(student)
          arts    <- new SimpleInterpreter[IO](db).suggestArticles(student)
          prs     <- profRef.get.flatMap(lst => new SimpleInterpreter[IO](db.copy(professors = lst)).suggestProfessors(student))
          _       <- IO.println("\n=== Рекомендации ===")
          _       <- IO.println(s"Темы       : ${tps.mkString(", ")}")
          _       <- IO.println(s"Конф.      : ${cfs.mkString(", ")}")
          _       <- IO.println("Преподаватели:")
          _       <- prs.traverse_ { prof =>
                       formatProfessor(prof).traverse_(IO.println) >>
                       IO.println("-----------------------------")
                     }
          _       <- IO.println(s"Статьи     : ${arts.mkString(", ")}")
          _       <- IO.println("Export to (csv/pdf)?")
          fmt     <- IO(StdIn.readLine().trim.toLowerCase)
          _       <- fmt match {
                       case "csv" => ExportService.exportCsv("recs.csv", student, (tps.toList, cfs.toList, prs.toList, arts.toList))
                       case "pdf" => ExportService.exportPdf("recs.pdf", student, (tps.toList, cfs.toList, prs.toList, arts.toList))
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



#### Error stacktrace:

```
scala.reflect.internal.Reporting.abort(Reporting.scala:69)
	scala.reflect.internal.Reporting.abort$(Reporting.scala:65)
	scala.reflect.internal.SymbolTable.abort(SymbolTable.scala:28)
	scala.reflect.internal.Types$ThisType.<init>(Types.scala:1394)
	scala.reflect.internal.Types$UniqueThisType.<init>(Types.scala:1414)
	scala.reflect.internal.Types$ThisType$.apply(Types.scala:1418)
	scala.meta.internal.pc.AutoImportsProvider$$anonfun$autoImports$3.applyOrElse(AutoImportsProvider.scala:74)
	scala.meta.internal.pc.AutoImportsProvider$$anonfun$autoImports$3.applyOrElse(AutoImportsProvider.scala:60)
	scala.collection.immutable.List.collect(List.scala:267)
	scala.meta.internal.pc.AutoImportsProvider.autoImports(AutoImportsProvider.scala:60)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$autoImports$1(ScalaPresentationCompiler.scala:299)
```
#### Short summary: 

scala.reflect.internal.FatalError: 
  ThisType(method readUntilValid) for sym which is not a class
     while compiling: file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.13.10
    compiler version: version 2.13.10
  reconstructed args: -classpath <WORKSPACE>\.bloop\v1\bloop-bsp-clients-classes\classes-Metals-Fbl15279Qnq7gxzk5JPu8g==;<HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.4\semanticdb-javac-0.10.4.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.10\scala-library-2.13.10.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-core_2.13\2.9.0\cats-core_2.13-2.9.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-effect_2.13\3.5.0\cats-effect_2.13-3.5.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\co\fs2\fs2-core_2.13\3.5.0\fs2-core_2.13-3.5.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\typesafe\config\1.4.2\config-1.4.2.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\ch\qos\logback\logback-classic\1.4.7\logback-classic-1.4.7.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-core_2.13\0.14.5\circe-core_2.13-0.14.5.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-generic_2.13\0.14.5\circe-generic_2.13-0.14.5.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-parser_2.13\0.14.5\circe-parser_2.13-0.14.5.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\apache\pdfbox\pdfbox\2.0.29\pdfbox-2.0.29.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-kernel_2.13\2.9.0\cats-kernel_2.13-2.9.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-effect-kernel_2.13\3.5.0\cats-effect-kernel_2.13-3.5.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\cats-effect-std_2.13\3.5.0\cats-effect-std_2.13-3.5.0.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scodec\scodec-bits_2.13\1.1.34\scodec-bits_2.13-1.1.34.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\ch\qos\logback\logback-core\1.4.7\logback-core-1.4.7.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\slf4j\slf4j-api\2.0.4\slf4j-api-2.0.4.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-numbers_2.13\0.14.5\circe-numbers_2.13-0.14.5.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\chuusai\shapeless_2.13\2.3.10\shapeless_2.13-2.3.10.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\io\circe\circe-jawn_2.13\0.14.5\circe-jawn_2.13-0.14.5.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\apache\pdfbox\fontbox\2.0.29\fontbox-2.0.29.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\commons-logging\commons-logging\1.2\commons-logging-1.2.jar;<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\typelevel\jawn-parser_2.13\1.4.0\jawn-parser_2.13-1.4.0.jar -Xplugin-require:semanticdb -Yrangepos -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: Ident(_CURSOR_parse)
       tree position: line 137 of file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
            tree tpe: <error>
              symbol: value <error: <none>>
   symbol definition: val <error: <none>>: <error> (a TermSymbol)
      symbol package: <empty>
       symbol owners: value <error: <none>> -> value artRepo -> method run -> object MainApp
           call site: <none> in <none>

== Source file context for tree position ==

   134     )
   135 
   136     val artRepo: Repository[Article] = new CsvRepository[IO, Article](
   137       _CURSOR_parse = {
   138         case List(tl, top, doi, y) => y.toIntOption.map(yi => Article(tl, top, if (doi.nonEmpty) Some(doi) else None, yi))
   139         case _ => None
   140       },