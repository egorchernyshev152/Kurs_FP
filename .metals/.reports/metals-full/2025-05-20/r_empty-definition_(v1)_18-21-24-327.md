file:///C:/Users/toorr/v1/src/main/scala/service/ExportService.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:

offset: 1887
uri: file:///C:/Users/toorr/v1/src/main/scala/service/ExportService.scala
text:
```scala
package service

import cats.effect.Sync
import model._
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.nio.charset.StandardCharsets
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font

object ExportService {
  def exportCsv[F[_]: Sync](
      path: String,
      student: Student,
      recs: (List[Topic], List[Conference], List[Professor], List[Article])
  ): F[Unit] = Sync[F].delay {
    val (topics, confs, profs, arts) = recs
    val lines = Seq(s"Student,${student.name}") ++
      topics.map(t => s"Topic,${t.name}") ++
      confs.map(c => s"Conference,${c.name}") ++
      profs.map(p => s"Professor,${p.name}") ++
      arts.map(a => s"Article,${a.title}")
    Files.write(
      Paths.get(path),
      lines.mkString("\n").getBytes(StandardCharsets.UTF_8),
      StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
    )
  }

  def exportPdf[F[_]: Sync](
      path: String,
      student: Student,
      recs: (List[Topic], List[Conference], List[Professor], List[Article])
  ): F[Unit] = Sync[F].delay {
    val doc = new PDDocument()
    val page = new PDPage(PDRectangle.LETTER)
    doc.addPage(page)
    val cs = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page)
    cs.beginText()
    cs.setFont(PDType1Font.HELVETICA_BOLD, 14)
    cs.newLineAtOffset(50, 700)
    cs.showText(s"Recommendations for ${student.name}")
    cs.newLineAtOffset(0, -20)
    recs._1.foreach(t => cs.showText(s"Topic: ${t.name}")) cs.newLineAtOffset(0,-15))
    recs._2.foreach(c => cs.showText(s"Conf : ${c.name}"); cs.newLineAtOffset(0,-15))
    recs._3.foreach(p => cs.showText(s"Prof : ${p.name}"); cs.newLineAtOffset(0,-15))
    recs._4.foreach(a => cs.showText(s"@@Art  : ${a.title}");cs.newLineAtOffset(0,-15))
    cs.endText(); cs.close()
    doc.save(path); doc.close()
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 