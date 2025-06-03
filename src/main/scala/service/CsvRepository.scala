package service // пакет для сервисов и репозиториев

import cats.effect.IO // импорт IO для эффектов при чтении/записи
import java.nio.file.{Files, Paths, StandardOpenOption} // файловые операции
import java.nio.charset.StandardCharsets // кодировка UTF-8
import scala.io.Source // утилита для чтения текстовых ресурсов

/**
  * Простейшая реализация Repository[A] через CSV.
  * @param parse  Функция парсинга строки CSV в объект A (List[String] => Option[A])
  * @param format Функция форматирования объекта A в список полей (A => List[String])
  */
class CsvRepository[A](
    parse: List[String] => Option[A],
    format: A => List[String]
) extends Repository[A] {

  // helper: путь к ресурсу в файловой системе
  private def path(resource: String) = Paths.get(resource)

  /**
    * Загружает все объекты из CSV:
    * - пытаемся прочитать из classpath, иначе из файловой системы
    * - пропускаем заголовок
    * - парсим каждую строку через parse
    */
  override def load(resource: String): IO[List[A]] = IO {
    // пытаемся получить InputStream из classpath
    val maybeStream = Option(getClass.getClassLoader.getResourceAsStream(resource))
    val lines = maybeStream
      .map(is => Source.fromInputStream(is, StandardCharsets.UTF_8.name).getLines().toList)
      .getOrElse {
        // если в classpath нет, читаем как обычный файл
        val p = path(resource)
        if (!Files.exists(p))
          throw new RuntimeException(s"Файл $resource не найден")
        Source.fromFile(p.toFile, StandardCharsets.UTF_8.name).getLines().toList
      }
    // lines: первый элемент — заголовок, убираем его и пытаемся распарсить каждую запись
    lines.drop(1).flatMap { line =>
      val cols = line.split(",", -1).map(_.trim).toList // сплитим и обрезаем пробелы
      parse(cols) // возвращаем Some(A) или None (filter + flatMap отбрасывает невалидные)
    }
  }

  /**
    * Сохраняет список объектов в CSV:
    * - формируем заголовок автоматически по количеству полей
    * - форматируем каждый объект через format и записываем
    */
  override def save(resource: String, items: List[A]): IO[Unit] = IO {
    val p = path(resource) // путь к CSV
    // создаём заголовок: col0,col1,... по числу полей в первом объекте
    val header = format(items.head).indices.map(i => s"col$i").mkString(",")
    // форматируем каждый объект в строку CSV
    val data = items.map(a => format(a).mkString(","))
    val content = (header +: data).mkString("\n") // объединяем в единую строку
    Files.write(p, content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
  }
}