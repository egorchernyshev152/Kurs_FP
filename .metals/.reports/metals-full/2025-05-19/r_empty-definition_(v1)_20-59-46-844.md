error id: file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala:impl/SimpleInterpreter#
file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
empty definition using pc, found symbol in pc: 
found definition using semanticdb; symbol impl/SimpleInterpreter#
empty definition using fallback
non-local guesses:

offset: 651
uri: file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
text:
```scala
import cats.effect.{IO, IOApp, ExitCode}
import cats.syntax.all._
import core.RecommendationAlg
import impl.SimpleInterpreter
import model.Role
import service.{DataLoader, Cache, Logger}
import ui.ConsoleUI
import scala.collection.immutable.LazyList
import model.Professor
import cats.data.EitherT

object MainApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val logger = Logger.console[IO]
    val program = for {
      _      <- IO.println("Загрузка данных...")
      db     <- DataLoader.load[IO].value.flatMap(EitherT.fromEither[IO])
      cache  <- Cache.empty[IO, String, LazyList[Any]]
      alg     = new SimpleInterpret@@er[IO](db)
      roleE  <- ConsoleUI.readRole[IO].value
      role   <- IO.fromEither(roleE.leftMap(err => new Exception(err)))
      _      <- logger.info(s"Role selected: $role")
      _      <- role match {
        case Role.Student =>
          for {
            studentE <- ConsoleUI.readStudent[IO].value
            student  <- IO.fromEither(studentE.leftMap(e => new Exception(e)))
            _        <- logger.info(s"Got student: ${student.name}")
            topics   <- alg.suggestTopics(student)
            confs    <- alg.suggestConferences(student)
            profs    <- alg.suggestProfessors(student)
            arts     <- alg.suggestArticles(student)
            _        <- IO.println("\n=== Рекомендации ===")
            _        <- IO.println(s"Темы       : ${topics.mkString(", ")}")
            _        <- IO.println(s"Конференции: ${confs.mkString(", ")}")
            _        <- IO.println(s"Преподаватели: ${profs.mkString(", ")}")
            _        <- IO.println(s"Статьи     : ${arts.mkString(", ")}")
          } yield ()

        case Role.Admin =>
          def loop: IO[Unit] = for {
            _    <- IO.println(
                     """
Вы в режиме ADMIN:
1) Добавить преподавателя
2) Удалить преподавателя
3) Показать всех преподавателей
4) Выйти
Введите команду (1-4):
""".stripMargin)
            cmd  <- IO(scala.io.StdIn.readLine())
            _    <- cmd.trim match {
              case "1" =>
                for {
                  _     <- IO.println("Имя:")
                  name  <- IO(scala.io.StdIn.readLine())
                  _     <- IO.println("Области (через ;):")
                  areas <- IO(scala.io.StdIn.readLine())
                  _     <- IO.println("hIndex:")
                  hStr  <- IO(scala.io.StdIn.readLine())
                  _     <- IO.println("Доступен? (y/n):")
                  avl   <- IO(scala.io.StdIn.readLine())
                  prof  = Professor(
                            name.trim,
                            areas.split(";").map(_.trim).toList,
                            hStr.toIntOption.getOrElse(0),
                            avl.toLowerCase.startsWith("y")
                          )
                  // здесь нужно мутабельно обновить БД или кэш
                  _     <- IO.println(s"Добавлен: $prof")
                  _     <- loop
                } yield ()

              case "2" =>
                for {
                  _     <- IO.println("Имя преподавателя для удаления:")
                  name  <- IO(scala.io.StdIn.readLine())
                  // здесь реализация удаления из кэша или БД
                  _     <- IO.println(s"Удалён: $name")
                  _     <- loop
                } yield ()

              case "3" =>
                for {
                  _     <- IO.println("Список преподавателей:")
                  _     <- IO.println(db.professors.mkString("\n"))
                  _     <- loop
                } yield ()

              case "4" =>
                IO.println("Выход из режима ADMIN.")

              case other =>
                IO.println(s"Неверная команда '$other'") >> loop
            }
          } yield ()

          loop
      }
    } yield ExitCode.Success

    program.handleErrorWith { e =>
      logger.error(e.getMessage) >> IO.pure(ExitCode.Error)
    }
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 