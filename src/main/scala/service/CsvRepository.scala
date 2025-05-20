package service

import cats.effect.IO
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.nio.charset.StandardCharsets
import scala.io.Source

/**
  * Простейшая реализация Repository[A] через CSV.
  * parse  — функция, превращающая List[String] (поля) в Option[A]
  * format — обратная функция: A → List[String]
  */
class CsvRepository[A](
    parse: List[String] => Option[A],
    format: A => List[String]
) extends Repository[A] {

  private def path(resource: String) = Paths.get(resource)

  override def load(resource: String): IO[List[A]] = IO {
    // Сначала пытаемся из classpath (src/main/resources)
    val maybeStream = Option(getClass.getClassLoader.getResourceAsStream(resource))
    val lines = maybeStream
      .map(is => Source.fromInputStream(is, StandardCharsets.UTF_8.name).getLines().toList)
      .getOrElse {
        // Если нет в classpath — читаем как файл рядом с проектом
        val p = path(resource)
        if (!Files.exists(p))
          throw new RuntimeException(s"Файл $resource не найден")
        Source.fromFile(p.toFile, StandardCharsets.UTF_8.name).getLines().toList
      }
    // Пропускаем заголовок и парсим каждую строку
    lines.drop(1).flatMap(line => parse(line.split(",", -1).map(_.trim).toList))
  }

  override def save(resource: String, items: List[A]): IO[Unit] = IO {
    // Собираем CSV: заголовок col0,col1,... и затем data
    val p = path(resource)
    val header = format(items.head).indices.map(i => s"col$i").mkString(",")
    val data = items.map(a => format(a).mkString(","))
    val content = (header +: data).mkString("\n")
    Files.write(p, content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
  }
}
