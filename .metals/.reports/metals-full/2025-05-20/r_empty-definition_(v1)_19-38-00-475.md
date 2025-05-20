error id: file:///C:/Users/toorr/v1/src/main/scala/service/CsvRepository.scala:
file:///C:/Users/toorr/v1/src/main/scala/service/CsvRepository.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 194
uri: file:///C:/Users/toorr/v1/src/main/scala/service/CsvRepository.scala
text:
```scala
package service

import cats.effect.IO
import java.nio.file.{Files, Paths, StandardOpenOption}
import scala.io.Source
import java.nio.charset.StandardCharsets

/**
 * Репозиторий CSV, ра@@ботает только на IO
 */
class CsvRepository[A](
    parse: List[String] => Option[A],
    format: A => List[String]
) extends Repository[A] {

  private def path(resource: String) = Paths.get(resource)

 override def load(resource: String): IO[List[A]] = IO {
    // читаем всегда в UTF-8, и из ресурсов, и из файловой системы
    val lines: List[String] =
      Option(getClass.getResourceAsStream(s"/$resource"))
        .map { is =>
          Source
            .fromInputStream(is, StandardCharsets.UTF_8.name)
            .getLines()
            .toList
        }
        .getOrElse {
          val p = path(resource)
          if (Files.exists(p))
            Source
              .fromFile(p.toFile, StandardCharsets.UTF_8.name)
              .getLines()
              .toList
          else
            Nil
        }

    // Первый строка — заголовок, всё остальное парсим
    lines.drop(1).flatMap { line =>
      parse(line.split(",", -1).map(_.trim).toList)
    }
  }

  override def save(resource: String, items: List[A]): IO[Unit] = IO {
    val p      = path(resource)
    val header = format(items.head).indices.map(i => s"col$i").mkString(",")
    val content = (header +: items.map(a => format(a).mkString(","))).mkString("\n")
    Files.write(p, content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 