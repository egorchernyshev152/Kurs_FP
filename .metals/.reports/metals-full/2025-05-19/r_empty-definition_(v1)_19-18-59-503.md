error id: file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala:
file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 558
uri: file:///C:/Users/toorr/v1/src/main/scala/MainApp.scala
text:
```scala
import cats.effect.{IO, IOApp, ExitCode}
import cats.syntax.all._
import core.RecommendationAlg
import impl.SimpleInterpreter
import service.{DataLoader, Cache, Logger}
import ui.ConsoleUI
import scala.collection.immutable.LazyList

/**
 * Основной класс приложения:
 * - Загружает данные
 * - Запрашивает информацию о студенте
 * - Вычисляет и показывает рекомендации
 * - Обрабатывает ошибки и логирует
 */
object MainApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val logger = Logger.console[IO]
    DataLoader.load[IO]("/professo@@rs.csv")
    val program = for {
      _      <- IO.println("Загрузка данных...")
      dbE    <- DataLoader.load[IO].value
      db     <- IO.fromEither(dbE.leftMap(err => new Exception(err)))
      cache  <- Cache.empty[IO, String, LazyList[Any]]
      alg     = new SimpleInterpreter[IO](db)
      student<- ConsoleUI.readStudent[IO].value
      st     <- IO.fromEither(student.leftMap(err => new Exception(err)))
      _      <- logger.info(s"Получен студент: ${st.name}")
      topics <- alg.suggestTopics(st)
      _      <- cache.put(st.name + "_topics", topics)
      confs  <- alg.suggestConferences(st)
      profs  <- alg.suggestProfessors(st)
      arts   <- alg.suggestArticles(st)
      _      <- IO.println("\n=== Рекомендации ===")
      _      <- IO.println(s"Темы       : ${topics.mkString(", ")}")
      _      <- IO.println(s"Конференции: ${confs.mkString(", ")}")
      _      <- IO.println(s"Преподаватели: ${profs.mkString(", ")}")
      _      <- IO.println(s"Статьи     : ${arts.mkString(", ")}")
    } yield ExitCode.Success

    program.handleErrorWith { e =>
      logger.error(e.getMessage) >> IO.pure(ExitCode.Error)
    }
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 