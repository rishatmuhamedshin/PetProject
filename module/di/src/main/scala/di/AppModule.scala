package di

import com.google.inject.{Scope, Scopes}
import net.codingwell.scalaguice.ScalaModule

abstract class AppModule extends ScalaModule{
  protected def bindSingleton[T, C <: T](implicit t: Manifest[T], c: Manifest[C]) = {
    bindDiffernetScoped[T, C](Scopes.SINGLETON)
  }

  protected def bindPrototype[T, C <: T](implicit t: Manifest[T], c: Manifest[C]) = {
    bindDiffernetScoped[T, C](Scopes.NO_SCOPE)
  }

  private def bindDiffernetScoped[T, C <: T](scope : Scope)(implicit t: Manifest[T], c: Manifest[C]) = {
    bind(t.runtimeClass.asInstanceOf[Class[T]])
      .to(c.runtimeClass.asInstanceOf[Class[C]]).in(scope)
  }
}
