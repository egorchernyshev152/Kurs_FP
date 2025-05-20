package service

import cats.effect.IO
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.nio.charset.StandardCharsets
import scala.io.Source

/** CSV-репозиторий, работающий только на IO и UTF-8 */
class CsvRepository[A](
    parse: List[String] => Option[A],
    format: A => List[String]
) extends Repository[A] {

  private def path(resource: String) = Paths.get(resource)

  override def load(resource: String): IO[List[A]] = IO {
    // 1) Попробовать из classpath
    val fromCP = Option(getClass.getClassLoader.getResourceAsStream(resource))
      .map(is => Source.fromInputStream(is, StandardCharsets.UTF_8.name).getLines().toList)

    // 2) Иначе — из файловой системы
    val lines = fromCP.getOrElse {
      val p = path(resource)
      if (Files.exists(p))
        Source.fromFile(p.toFile, StandardCharsets.UTF_8.name).getLines().toList
      else Nil
    }

    // Отбросить первую строку (заголовок) и распарсить остальные
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
