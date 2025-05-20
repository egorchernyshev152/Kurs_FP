package service

import cats.effect.IO
import model._
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.nio.charset.StandardCharsets
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage}
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.PDPageContentStream

/** Экспорт рекомендаций в CSV и PDF */
object ExportService {
  def exportCsv(
      path: String,
      student: Student,
      recs: (List[Topic], List[Conference], List[Professor], List[Article])
  ): IO[Unit] = IO {
    val (topics, confs, profs, arts) = recs
    val lines = Seq(s"Student,${student.name}") ++
      topics.map(t => s"Topic,${t.name}") ++
      confs.map(c => s"Conference,${c.name}") ++
      profs.map(p => s"Professor,${p.name}") ++
      arts.map(a => s"Article,${a.title}")
    Files.write(Paths.get(path), lines.mkString("\n").getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
  }

  def exportPdf(
      path: String,
      student: Student,
      recs: (List[Topic], List[Conference], List[Professor], List[Article])
  ): IO[Unit] = IO {
    val (topics, confs, profs, arts) = recs
    val doc = new PDDocument()
    val page = new PDPage(PDRectangle.LETTER)
    doc.addPage(page)
    val cs = new PDPageContentStream(doc, page)
    try {
      cs.beginText()
      cs.setFont(PDType1Font.HELVETICA_BOLD, 14)
      cs.newLineAtOffset(50, 700)
      cs.showText(s"Recommendations for ${student.name}")
      cs.newLineAtOffset(0, -20)
      topics.foreach(t => { cs.showText(s"Topic: ${t.name}"); cs.newLineAtOffset(0, -15) })
      confs.foreach(c => { cs.showText(s"Conference: ${c.name}"); cs.newLineAtOffset(0, -15) })
      profs.foreach(p => { cs.showText(s"Professor: ${p.name}"); cs.newLineAtOffset(0, -15) })
      arts.foreach(a => { cs.showText(s"Article: ${a.title}"); cs.newLineAtOffset(0, -15) })
      cs.endText()
    } finally {
      cs.close()
    }
    doc.save(path)
    doc.close()
  }
}
