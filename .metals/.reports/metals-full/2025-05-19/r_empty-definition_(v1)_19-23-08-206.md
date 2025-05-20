error id: file:///C:/Users/toorr/v1/src/main/scala/service/DataLoader.scala:
file:///C:/Users/toorr/v1/src/main/scala/service/DataLoader.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 333
uri: file:///C:/Users/toorr/v1/src/main/scala/service/DataLoader.scala
text:
```scala
package service

import cats.effect.Sync
import cats.data.EitherT
import model._
import scala.io.Source
import scala.util.Try

/**
 * Очень надёжный загрузчик преподавателей из resources:
 * — пропускает все строки, у которых не 4 столбца
 * — в случае любых исключений при разборе строки просто отбрасывает её
 * — возвращает только@@ корректно распарсенные Professor
 */
object DataLoader {

  def loadProfessors[F[_]: Sync](path: String): EitherT[F, String, List[Professor]] = {
    EitherT {
      Sync[F].delay {
        // откроем ресурс
        Option(getClass.getResourceAsStream(path))
          .toRight(s"Resource $path not found")
          .flatMap { is =>
            // читаем все строки и закрываем поток
            val lines     = Source.fromInputStream(is).getLines().toList
            is.close()

            // сбросим заголовок
            val dataLines = lines.drop(1)

            // для каждой строки попытаемся распарсить Professor
            val profs = dataLines.flatMap { line =>
              Try {
                val cols = line.split(",", -1).map(_.trim)
                if (cols.length != 4) throw new Exception("wrong columns count")

                val name    = cols(0)
                val areas   = cols(1).split(";").toList
                val hIndex  = cols(2).toInt            // NumberFormatException пойдёт в Try
                val avail   = cols(3).toLowerCase match {
                  case "y" | "yes" => true
                  case "n" | "no"  => false
                  case _           => throw new Exception(s"invalid available flag '${cols(3)}'")
                }

                Professor(name, areas, hIndex, avail)
              }.toOption  // в случае любой ошибки — None, и строка отбрасывается
            }

            Right(profs)
          }
      }
    }
  }

  def load[F[_]: Sync]: EitherT[F, String, Database] = for {
    profs <- loadProfessors[F]("/professors.csv")
  } yield Database(
    topics = List(
      Topic("Machine Learning", List("ml", "machine learning")),
      Topic("Computer Vision", List("cv", "computer vision")),
      Topic("Data Science", List("data", "science"))
    ),
    conferences = List(
      Conference("NeurIPS", 9.3, List("machine learning")),
      Conference("ICCV",   8.7, List("computer vision")),
      Conference("KDD",    7.9, List("data science"))
    ),
    professors = profs,
    articles = List(
      Article("Deep Learning Advances", "machine learning", Some("10.1000/dl"), 2024),
      Article("Vision Transformers",    "computer vision",  None,             2023),
      Article("Big Data Trends",        "data science",     Some("10.2000/bd"), 2022)
    )
  )
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 