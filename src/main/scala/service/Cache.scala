
package service

import cats.effect.{Ref, Sync}
import cats.syntax.functor._        // даёт .map на F[_]
import scala.collection.immutable.LazyList

/** Простая кэш-служба:
  * - get : получить значение по ключу
  * - put : сохранить значение
  */
class Cache[F[_]: Sync, K, V](ref: Ref[F, Map[K,V]]) {
   def get(k: K): F[Option[V]]  = ref.get.map(_.get(k))
   def put(k: K, v: V): F[Unit] = ref.update(_ + (k -> v))
 }

 object Cache {
  def empty[F[_]: Sync, K, V]: F[Cache[F,K,V]] =
    Ref.of[F,Map[K,V]](Map.empty).map(new Cache[F,K,V](_))
 }
