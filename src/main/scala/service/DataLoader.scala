package service

import cats.effect.IO
import model._

/** Загружает все доменные сущности из CSV-репозиториев */
class DataLoader(
    profRepo: Repository[Professor],
    confRepo: Repository[Conference],
    topRepo:  Repository[Topic],
    artRepo:  Repository[Article]
) {

  /** Чистый IO без EitherT — здесь исключения IO сами упадут, если что-то не так */
  def loadAll(): IO[Database] = for {
    profs <- profRepo.load("professors.csv")
    confs <- confRepo.load("conferences.csv")
    tops  <- topRepo.load("topics.csv")
    arts  <- artRepo.load("articles.csv")
  } yield Database(tops, confs, profs, arts)
}
