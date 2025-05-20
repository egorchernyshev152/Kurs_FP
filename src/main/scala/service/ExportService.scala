package service

import cats.effect.IO
import model._
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.nio.charset.StandardCharsets
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage}
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.PDPageContentStream

/**
  * Сервис для экспорта списка рекомендаций в CSV или PDF.
  */
object ExportService {

  /** Экспортируем в CSV: каждая строка — пара (тип, имя) */
  def exportCsv(path: String, student: Student,
                recs: (List[Topic], List[Conference], List[Professor], List[Article])): IO[Unit] = IO {
    val (tps, cfs, prs, arts) = recs
    val lines = Seq(s"Student,${student.name}") ++
      tps.map(t => s"Topic,${t.name}") ++
      cfs.map(c => s"Conference,${c.name}") ++
      prs.map(p => s"Professor,${p.name}") ++
      arts.map(a => s"Article,${a.title}")
    Files.write(Paths.get(path), lines.mkString("\n").getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
  }

  /** Экспортируем в PDF — простейший текстовый вывод на страницу */
  def exportPdf(path: String, student: Student,
                recs: (List[Topic], List[Conference], List[Professor], List[Article])): IO[Unit] = IO {
    val (tps, cfs, prs, arts) = recs
    val doc  = new PDDocument()
    val page = new PDPage(PDRectangle.LETTER)
    doc.addPage(page)
    val cs = new PDPageContentStream(doc, page)
    try {
      cs.beginText()
      cs.setFont(PDType1Font.HELVETICA_BOLD, 14)
      cs.newLineAtOffset(50, 700)
      cs.showText(s"Recommendations for ${student.name}")
      cs.newLineAtOffset(0, -20)
      // пишем по секциям
      tps.foreach(t => { cs.showText(s"Topic: ${t.name}"); cs.newLineAtOffset(0, -15) })
      cfs.foreach(c => { cs.showText(s"Conference: ${c.name}"); cs.newLineAtOffset(0, -15) })
      prs.foreach(p => { cs.showText(s"Professor: ${p.name}"); cs.newLineAtOffset(0, -15) })
      arts.foreach(a => { cs.showText(s"Article: ${a.title}"); cs.newLineAtOffset(0, -15) })
      cs.endText()
    } finally cs.close()
    doc.save(path)
    doc.close()
  }
}
