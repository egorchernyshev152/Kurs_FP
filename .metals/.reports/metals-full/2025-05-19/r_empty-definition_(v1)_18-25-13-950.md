error id: file:///C:/Users/toorr/v1/src/main/scala/Main.scala:effect.
file:///C:/Users/toorr/v1/src/main/scala/Main.scala
empty definition using pc, found symbol in pc: effect.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -cats/effect/cats/effect.
	 -cats/effect.
	 -scala/Predef.cats.effect.
offset: 66
uri: file:///C:/Users/toorr/v1/src/main/scala/Main.scala
text:
```scala


// == Импорты для функционального стиля ==



import cats.@@effect._
import scala.io.StdIn
import scala.annotation.tailrec

// == Модели данных ==
final case class Publication(id: String, title: String, year: Int)

// Ошибки приложения
sealed trait AppError { def message: String }
case class InputError(message: String)    extends AppError
case class RepoError(message: String)     extends AppError
case class RecoError(message: String)     extends AppError

// == Тип для эффектов приложения ==
type AppF[A] = EitherT[IO, AppError, A]

// == Tagless Final алгебры ==
trait PublicationRepo[F[_]] {
  def loadAll(studentId: String): F[LazyList[Publication]]
}
object PublicationRepo {
  def apply[F[_]](implicit ev: PublicationRepo[F]): PublicationRepo[F] = ev
}

trait Recommender[F[_]] {
  def recommend(pubs: LazyList[Publication]): F[List[Publication]]
}
object Recommender {
  def apply[F[_]](implicit ev: Recommender[F]): Recommender[F] = ev
}

// == Реализация эффекта for IO + Either через EitherT ==
object AppF {
  def pure[A](a: A): AppF[A] = EitherT.pure[IO, AppError](a)
  def fromEither[A](e: Either[AppError, A]): AppF[A] = EitherT.fromEither[IO](e)
  def raiseError[A](err: AppError): AppF[A] = EitherT.leftT[IO, A](err)
}

// == Интерпретаторы алгебр ==
class InMemoryPublicationRepo(data: Map[String, List[Publication]]) extends PublicationRepo[AppF] {
  override def loadAll(studentId: String): AppF[LazyList[Publication]] =
    data.get(studentId)
      .map(lst => AppF.pure(LazyList.from(lst)))
      .getOrElse(AppF.raiseError(RepoError(s"No data for student '$studentId'")))
}

class SimpleRecommender extends Recommender[AppF] {
  override def recommend(pubs: LazyList[Publication]): AppF[List[Publication]] =
    // берём три самых свежих
    AppF.pure(pubs.sortBy(-_.year).take(3).toList)
}

// == Утилиты для ленивых списков ==
object LazyUtils {
  def fromRange(start: Int, end: Int): LazyList[Int] =
    LazyList.cons(start, if (start < end) fromRange(start + 1, end) else LazyList.empty)
}

// == CLI - диалоговое окно и обработка ошибок ==
object CLI {
  private def promptLine(msg: String): IO[String] = IO {
    print(msg); StdIn.readLine()
  }

  private def readNonEmpty(msg: String): AppF[String] =
    EitherT.liftF(promptLine(msg)).flatMap { input =>
      Option(input).filter(_.nonEmpty)
        .toRight(InputError("Empty input"))
        .toEitherT[IO]
    }

  private def printPublications(lst: List[Publication]): IO[Unit] = IO {
    println("\n=== Recommended Publications ===")
    lst.foreach(p => println(s"[${p.year}] ${p.title} (ID: ${p.id})"))
    println()
  }

  private def handleError(err: AppError): IO[Unit] = IO {
    println(s"Error: ${err.message}")
  }

  @tailrec
  def loop(repo: PublicationRepo[AppF], rec: Recommender[AppF]): IO[Unit] = {
    // Прочитать ID
    val action = for {
      sid <- readNonEmpty("Enter student ID or 'exit': ")
      res <- if (sid == "exit") EitherT.rightT[IO, AppError](Left("exit"))
             else
               for {
                 pubs <- repo.loadAll(sid)
                 recs <- rec.recommend(pubs)
               } yield Right(recs)
    } yield res

    // Выполнить и распечатать
    val ioNext = action.value.flatMap {
      case Left(err: AppError) => handleError(err) >> loop(repo, rec)
      case Right(Left("exit")) => IO(println("Exiting application."))
      case Right(Right(recs))    => printPublications(recs) >> loop(repo, rec)
    }
    ioNext.unsafeRunSync()
  }
}

// == Основное приложение ==
object RecommenderApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    // Пример данных
    val sampleData = Map(
      "student1" -> List(
        Publication("p1", "FP in Scala", 2022),
        Publication("p2", "Tagless Final Deep Dive", 2023),
        Publication("p3", "Lazy Lists and Performance", 2021),
        Publication("p4", "Higher-Kinded Data Types", 2023)
      ),
      "student2" -> List(
        Publication("x1", "Functional Error Handling", 2024),
        Publication("x2", "Cats Effect in Practice", 2023)
      )
    )

    val repo  = new InMemoryPublicationRepo(sampleData)
    val reco  = new SimpleRecommender

    IO(println("=== Student Publication Recommender ===")) *>
    IO(CLI.loop(repo, reco)) *> IO.pure(ExitCode.Success)
  }
}

// == Дополнительно можно расширить: ==
// - Файловый или HTTP-интерпретатор PublicationRepo
// - ADT Command и парсер команд
// - Интеграция с базой данных через Doobie
// - Unit-тесты на Cats-стеке (EitherT, IO)
// - Расширение CLI фичами: меню, пагинация


```


#### Short summary: 

empty definition using pc, found symbol in pc: effect.