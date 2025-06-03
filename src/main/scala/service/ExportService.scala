package service // объявление пакета для сервисов экспорта

import cats.effect.IO // импорт контекста IO из Cats Effect для описания эффектов
import model._ // импорт всех доменных моделей: Student, Topic, Conference, Professor, Article
import java.nio.file.{Files, Paths, StandardOpenOption} // API для работы с файлами
import java.nio.charset.StandardCharsets // набор кодировок для работы с текстом
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage} // классы PDFBox для документа и страницы
import org.apache.pdfbox.pdmodel.common.PDRectangle // прямоугольник (размер страницы)
import org.apache.pdfbox.pdmodel.font.PDType1Font // шрифты PDFBox
import org.apache.pdfbox.pdmodel.PDPageContentStream // поток контента для рисования текста

/**
  * Сервис для экспорта списка рекомендаций в CSV или PDF.
  */
object ExportService {

  /**
    * Экспортируем рекомендации в CSV:
    * path – путь к файлу,
    * student – информация о студенте,
    * recs – кортеж списков рекомендаций (тем, конференций, профессоров, статей)
    */
  def exportCsv(path: String, student: Student,
                recs: (List[Topic], List[Conference], List[Professor], List[Article])): IO[Unit] = IO {
    val (tps, cfs, prs, arts) = recs // распаковываем кортеж рекомендаций
    // формируем строки: первая строка с именем студента, далее пары (тип, имя)
    val lines = Seq(s"Student,${student.name}") ++
      tps.map(t => s"Topic,${t.name}") ++
      cfs.map(c => s"Conference,${c.name}") ++
      prs.map(p => s"Professor,${p.name}") ++
      arts.map(a => s"Article,${a.title}")
    // записываем все строки в файл с указанными опциями: создать или перезаписать
    Files.write(Paths.get(path), lines.mkString("\n").getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
  }

  /**
    * Экспортируем рекомендации в PDF:
    * простейший текстовый вывод на единственную страницу
    */
  def exportPdf(path: String, student: Student,
                recs: (List[Topic], List[Conference], List[Professor], List[Article])): IO[Unit] = IO {
    val (tps, cfs, prs, arts) = recs // распаковываем рекомендации
    val doc  = new PDDocument() // создаём новый PDF документ
    val page = new PDPage(PDRectangle.LETTER) // создаём страницу размера LETTER
    doc.addPage(page) // добавляем страницу в документ
    val cs = new PDPageContentStream(doc, page) // открываем поток для записи контента
    try {
      cs.beginText() // начинаем писать текст
      cs.setFont(PDType1Font.HELVETICA_BOLD, 14) // устанавливаем жирный шрифт размером 14
      cs.newLineAtOffset(50, 700) // перемещаемся на координаты (50,700)
      cs.showText(s"Recommendations for ${student.name}") // пишем заголовок с именем студента
      cs.newLineAtOffset(0, -20) // перешаг вниз на 20 пунктов
      // для каждой категории рекомендаций выводим метку и имя
      tps.foreach(t => { cs.showText(s"Topic: ${t.name}"); cs.newLineAtOffset(0, -15) })
      cfs.foreach(c => { cs.showText(s"Conference: ${c.name}"); cs.newLineAtOffset(0, -15) })
      prs.foreach(p => { cs.showText(s"Professor: ${p.name}"); cs.newLineAtOffset(0, -15) })
      arts.foreach(a => { cs.showText(s"Article: ${a.title}"); cs.newLineAtOffset(0, -15) })
      cs.endText() // завершаем блок текста
    } finally cs.close() // в любом случае закрываем поток
    doc.save(path) // сохраняем документ по указанному пути
    doc.close() // закрываем документ
  }
}
