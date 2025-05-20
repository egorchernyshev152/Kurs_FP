package service

import cats.effect.IO
import model._

/**
  * Объект, который собирает все четыре репозитория
  * и загружает их в единую структуру Database.
  */
class DataLoader(
    profRepo: Repository[Professor],
    confRepo: Repository[Conference],
    topRepo:  Repository[Topic],
    artRepo:  Repository[Article]
) {
  def loadAll(): IO[Database] = for {
    professors  <- profRepo.load("professors.csv")
    conferences <- confRepo.load("conferences.csv")
    topics      <- topRepo.load("topics.csv")
    articles    <- artRepo.load("articles.csv")
  } yield Database(topics, conferences, professors, articles)
}
