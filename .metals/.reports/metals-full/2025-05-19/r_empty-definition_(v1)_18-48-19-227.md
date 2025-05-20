file:///C:/Users/toorr/v1/src/main/scala/service/Cache.scala
empty definition using pc, found symbol in pc: 
semanticdb not found
empty definition using fallback
non-local guesses:

offset: 88
uri: file:///C:/Users/toorr/v1/src/main/scala/service/Cache.scala
text:
```scala
package service

import cats.effect.Ref
import scala.collection.immutable.LazyList
impor@@t cats.syntax.functor._ // <-- добавьте этот импорт
import scala.collection.immutable.LazyList
/**
 * Простая кэш-служба:
 * - get : получить значение по ключу
 * - put : сохранить значение
 */
class Cache[F[_], K, V](ref: Ref[F, Map[K,V]]) {
  def get(k: K): F[Option[V]]    = ref.get.map(_.get(k))
  def put(k: K, v: V): F[Unit]   = ref.update(_ + (k -> v))
}

object Cache {
  def empty[F[_]: cats.effect.Sync, K, V]: F[Cache[F,K,V]] =
    Ref.of[F,Map[K,V]](Map.empty).map(new Cache(_))
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 